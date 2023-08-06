module com.github.serenerd.hellojavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.github.serenerd.hellojavafx to javafx.fxml;
    exports com.github.serenerd.hellojavafx;
}