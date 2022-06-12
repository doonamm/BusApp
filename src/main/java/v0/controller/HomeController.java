package v0.controller;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.event.MapStateEventType;
import com.dlsc.gmapsfx.javascript.event.StateEventHandler;
import com.dlsc.gmapsfx.javascript.object.*;
import com.dlsc.gmapsfx.shapes.Polyline;
import com.dlsc.gmapsfx.shapes.PolylineOptions;
import com.google.maps.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import v0.Config;
import v0.MapServices;

import javax.xml.transform.Result;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;

public class HomeController implements Initializable, MapComponentInitializedListener, StateEventHandler {
    @FXML
    protected AnchorPane mapContainer;
    @FXML
    protected TextField from;
    @FXML
    protected TextField to;
    @FXML
    protected TextField find;

    protected GoogleMapView googleMapView;
    private GoogleMap googleMap;

    private Connection connection;
    private Statement statement;

    public void showNearbyStops(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            Class.forName(Config.driver);
            connection = DriverManager.getConnection(Config.DB_URL);
            statement = connection.createStatement();
        }
        catch (Exception err){
            err.printStackTrace();
        }

        googleMapView = new GoogleMapView(Config.mapLanguage, Config.apiKey);
        mapContainer.getChildren().add(googleMapView);

        AnchorPane.setTopAnchor(googleMapView, 0.0);
        AnchorPane.setBottomAnchor(googleMapView, 0.0);
        AnchorPane.setLeftAnchor(googleMapView, 0.0);
        AnchorPane.setRightAnchor(googleMapView, 0.0);

        googleMapView.toBack();
        googleMapView.addMapInitializedListener(this);
    }

    @Override
    public void mapInitialized() {
        MapOptions options = new MapOptions();
        options.center(Config.mapDefaultLocation)
                .mapType(MapTypeIdEnum.ROADMAP)
                .styleString(Config.mapStyle)
                .zoom(Config.mapDefaultZoom)
                .zoomControl(false)
                .streetViewControl(false)
                .mapTypeControl(false);

        googleMap = googleMapView.createMap(options);
        googleMap.addStateEventHandler(MapStateEventType.dragend, this);
    }

    @Override
    public void handle(){
        if(googleMap.getZoom() < 15){
            return;
        };

        System.out.println(googleMap.getZoom());
        System.out.println(googleMap.getCenter());

        LatLongBounds box = googleMap.getBounds();

        try{
            Collection<Marker> markers = new ArrayList<>();

            String queryStops = "SELECT * FROM stops";
            ResultSet results = statement.executeQuery(queryStops);
            while (results.next()){
                LatLong stop = new LatLong(results.getDouble("lat"), results.getDouble("lng"));
                if(box.contains(stop)){
                    System.out.println("Add marker!");
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(stop)
                            .icon(Config.mapMarkerImage);

                    Marker marker = new Marker(markerOptions);
                    markers.add(marker);
                }
            }

            googleMap.clearMarkers();
            if(markers.size() > 0){
                System.out.println(markers.size());
                googleMap.addMarkers(markers);
            }
        }
        catch (Exception err){
            System.out.println(err.getMessage());
        }
    }

    @FXML
    public void handleClickTarget(){
        GeolocationResult result = MapServices.geolocation();

        if(result != null){
            System.out.println("Success");
            LatLng location = result.location;
            googleMap.setCenter(new LatLong(location.lat, location.lng));
            googleMap.setZoom(15);
        }
        else{
            System.out.println("Can not find location");
        }
    }

    @FXML
    public void searchDirections(){

        DirectionsResult result = MapServices.direction(from.getText(), to.getText());

        if(result == null){
            return;
        }

        MVCArray paths = new MVCArray();

        Arrays.stream(result.routes[0].legs[0].steps).forEach(point -> {
            System.out.println(point.htmlInstructions);

            paths.push(new LatLong(point.startLocation.lat, point.startLocation.lng));
            paths.push(new LatLong(point.endLocation.lat, point.endLocation.lng));

            MarkerOptions option = new MarkerOptions().position(new LatLong(point.startLocation.lat, point.startLocation.lng));
            Marker marker = new Marker(option);
            googleMap.addMarker(marker);
        });

        PolylineOptions opts = new PolylineOptions();
        opts.strokeColor("#34B67A");
        opts.strokeWeight(5);
        Polyline polyline = new Polyline(opts);
        polyline.setPath(paths);

        googleMap.addMapShape(polyline);
    }

    @FXML
    public void searchAddress(){
        LatLng location = MapServices.geolocation().location;
        AutocompletePrediction[] result = MapServices.autocompleteSearch(find.getText(), location);

        Arrays.stream(result).forEach(place -> {
            System.out.println(place.description);
        });
    }
}
