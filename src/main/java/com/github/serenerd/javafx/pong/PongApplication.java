package com.github.serenerd.javafx.pong;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class PongApplication extends Application {

    public static final double WINDOW_WIDTH = 700d;
    public static final double WINDOW_HEIGHT = 400d;

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
        stage.getIcons().add(new Image("icon.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}