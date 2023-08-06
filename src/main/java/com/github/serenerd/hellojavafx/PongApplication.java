package com.github.serenerd.hellojavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class PongApplication extends Application {

    public static final double WINDOW_WIDTH = 700D;
    public static final double WINDOW_HEIGHT = 400D;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(PongApplication.class.getResource("pong.fxml"));
        Scene scene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);

        PongController controller = loader.getController();
        scene.setOnKeyPressed(controller::handleKeyPressed);
        scene.setOnKeyReleased(controller::handleKeyReleased);

        scene.setFill(Color.BLACK);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Pong");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
