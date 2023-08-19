package com.github.serenerd.hellojavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MultipleButtonsMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MultipleButtonsMain.class.getResource("multipleButtons.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}