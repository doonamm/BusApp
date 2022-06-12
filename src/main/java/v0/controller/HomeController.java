package v0.controller;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.event.MapStateEventType;
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

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable, MapComponentInitializedListener {
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
    private List<LatLong> stopPositions;

    public void showNearbyStops(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //init connection
        try{
            Class.forName(Config.driver);
            connection = DriverManager.getConnection(Config.DB_URL);
            statement = connection.createStatement();
        }
        catch (Exception err){
            err.printStackTrace();
        }

        //init map
        googleMapView = new GoogleMapView(Config.mapLanguage, Config.apiKey);
        mapContainer.getChildren().add(googleMapView);

        AnchorPane.setTopAnchor(googleMapView, 0.0);
        AnchorPane.setBottomAnchor(googleMapView, 0.0);
        AnchorPane.setLeftAnchor(googleMapView, 0.0);
        AnchorPane.setRightAnchor(googleMapView, 0.0);

        googleMapView.toBack();
        googleMapView.addMapInitializedListener(this);
    }

    public void initMarker(){
        stopPositions = new ArrayList<>();

        String queryStops = "SELECT * FROM stops";

        try{
            ResultSet results = statement.executeQuery(queryStops);

            while (results.next()){
                LatLong stop = new LatLong(results.getDouble("lat"), results.getDouble("lng"));
                stopPositions.add(stop);
            }

            System.out.println("@@@" + stopPositions.size());
        }
        catch (Exception err){
            System.out.println(err.getMessage());
        }
    }

    @Override
    public void mapInitialized() {
        MapOptions options = new MapOptions();
        options.center(Config.mapDefaultLocation)
                .mapType(MapTypeIdEnum.ROADMAP)
                .styleString(Config.mapStyle2)
                .zoom(Config.mapDefaultZoom)
                .zoomControl(false)
                .streetViewControl(false)
                .mapTypeControl(false);

        googleMap = googleMapView.createMap(options);
        googleMap.addStateEventHandler(MapStateEventType.dragend, this::showAroundStops);
        googleMap.addStateEventHandler(MapStateEventType.zoom_changed, this::showAroundStops);

        initMarker();
        showAroundStops();
    }
    public  void showAroundStops(){
        googleMap.clearMarkers();
        if(googleMap.getZoom() < 16){
            return;
        };
        System.out.println("Zoom: " + googleMap.getZoom());

        LatLongBounds box = googleMap.getBounds();

        Collection<Marker> markers = new LinkedList<>();

        for (LatLong stop : stopPositions) {
            if(box.contains(stop)){
                MarkerOptions markerOptions = new MarkerOptions().position(stop).icon(Config.mapMarkerImage);
                markers.add(new Marker(markerOptions));
            }
        }

        System.out.println("Stops count: " + markers.size());

        googleMap.addMarkers(markers);
    }

    @FXML
    public void goToCurrentPosition(){
        GeolocationResult result = MapServices.geolocation();

        if(result != null){
            System.out.println("Success");
            LatLng location = result.location;
            googleMap.setCenter(new LatLong(location.lat, location.lng));
            googleMap.setZoom(16);

            showAroundStops();
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
