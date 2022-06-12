package v0;

import com.dlsc.gmapsfx.javascript.object.LatLong;
import com.dlsc.gmapsfx.javascript.object.MapOptions;

import java.sql.Connection;
import java.sql.DriverManager;

public class Config {
    public final static String apiKey = "AIzaSyC11V9LAawWBL0sf_O-4oFWWNq6SG8t-2A";
    public final static String mapStyle = "[{\"featureType\":\"all\",\"elementType\":\"labels\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"transit\",\"elementType\":\"labels\",\"stylers\":[{\"visibility\":\"on\"}]}]";
    public final static String mapLanguage = "en-EN";
    public final static LatLong mapDefaultLocation = new LatLong(10.870244162717086, 106.80304914633336);
    public final static int mapDefaultZoom = 16;
    public final static String mapMarkerImage = "https://raw.githubusercontent.com/doonamm/BusApp/master/src/main/resources/v0/images/marker.svg";
    public final static String DB_URL = "jdbc:mysql://localhost:3306/busapp?user=root&password=&useUnicode=true&characterEncoding=utf8";
    public final static String driver = "com.mysql.cj.jdbc.Driver";
}
