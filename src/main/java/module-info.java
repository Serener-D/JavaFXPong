module com.github.serenerd.javafx.pong {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.github.serenerd.javafx.pong to javafx.fxml;
    exports com.github.serenerd.javafx.pong;
}