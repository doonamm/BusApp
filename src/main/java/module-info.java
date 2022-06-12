module v0.busapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    requires java.base;
    requires google.maps.services;
    requires com.google.gson;
    requires com.dlsc.gmapsfx;
    requires jdk.jsobject;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;

    opens v0 to javafx.fxml;
    exports v0;
    exports v0.controller;
    opens v0.controller to javafx.fxml;
}