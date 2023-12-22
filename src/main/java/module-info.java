module com.artsolo.musicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.rmi;
    requires com.google.gson;

    opens com.artsolo.musicplayer to javafx.fxml;
    exports com.artsolo.musicplayer;
    exports com.artsolo.musicplayer.services;
    opens com.artsolo.musicplayer.services to javafx.fxml;
    exports com.artsolo.musicplayer.singletons;
    opens com.artsolo.musicplayer.singletons to javafx.fxml;
    exports com.artsolo.musicplayer.models;
    opens com.artsolo.musicplayer.models to javafx.fxml;
}