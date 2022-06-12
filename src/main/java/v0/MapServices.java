package v0;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.*;
import com.google.maps.model.*;
import com.google.maps.PlaceAutocompleteRequest.SessionToken;

public class MapServices {
    public static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static GeocodingResult geocodeAddress(String address){
        GeoApiContext context = new GeoApiContext.Builder().apiKey(Config.apiKey).build();
        try{
            GeocodingResult[] results =  GeocodingApi.geocode(context, address).await();
            context.shutdown();
            return results[0];
        }
        catch(Exception e){
            context.shutdown();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static GeocodingResult geocodePlaceId(String placeId){
        GeoApiContext context = new GeoApiContext.Builder().apiKey(Config.apiKey).build();
        try{
            GeocodingResult[] results =  GeocodingApi.newRequest(context).place(placeId).await();
            context.shutdown();
            return results[0];
        }
        catch(Exception e){
            context.shutdown();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static GeolocationResult geolocation(){
        GeoApiContext context = new GeoApiContext.Builder().apiKey(Config.apiKey).build();
        try{
            GeolocationResult result = GeolocationApi.newRequest(context).CreatePayload().await();
            context.shutdown();
            return result;
        }
        catch(Exception e){
            context.shutdown();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static DirectionsResult direction(String origin, String destination){
        GeoApiContext context = new GeoApiContext.Builder().apiKey(Config.apiKey).build();
        try{
            DirectionsResult result = DirectionsApi.newRequest(context)
                    .mode(TravelMode.TRANSIT)
                    .transitMode(TransitMode.BUS)
                    .units(Unit.METRIC)
                    .region("vn")
                    .origin(origin)
                    .destination(destination)
                    .await();

            System.out.println("Direction results\nRoute count: " + result.routes.length + "Leg count" + result.routes[0].legs.length);

            context.shutdown();
            return result;
        }
        catch(Exception e){
            context.shutdown();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static AutocompletePrediction[] autocompleteSearch(String text, LatLng location){
        GeoApiContext context = new GeoApiContext.Builder().apiKey(Config.apiKey).build();
        System.out.println("okokokookoko");
        try{
            SessionToken session = new SessionToken();
            AutocompletePrediction[] results = PlacesApi.placeAutocomplete(context, text, session)
                    .components(ComponentFilter.country("vn"))
                    .language("vn")
                    .location(location)
                    .await();

            System.out.println("Place results: " + GSON.toJson(results));

            context.shutdown();
            return results;
        }
        catch(Exception e){
            context.shutdown();
            e.printStackTrace();
            return null;
        }
    }

    public static PlacesSearchResult[] textSearch(String text, LatLng location){
        GeoApiContext context = new GeoApiContext.Builder().apiKey(Config.apiKey).build();
        System.out.println("okokokookoko");
        try{
            SessionToken session = new SessionToken();
            PlacesSearchResponse results = PlacesApi.textSearchQuery(context, text).await();

            System.out.println("Place results: " +results.results);

            context.shutdown();
            return results.results;
        }
        catch(Exception e){
            context.shutdown();
            e.printStackTrace();
            return null;
        }
    }
}
