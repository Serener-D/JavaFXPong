package com.github.serenerd.hellojavafx;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.github.serenerd.hellojavafx.PongApplication.WINDOW_HEIGHT;

public class PongController {

    private static final double INITIAL_SPEED = 5;
    private static final double MAX_SPEED = 100;
    private static final double ACCELERATION = 10;
    private Double currentUpSpeed = INITIAL_SPEED;
    private Double currentDownSpeed = INITIAL_SPEED;

    @FXML
    private Rectangle leftRectangle;
    @FXML
    private Circle ball;

    public void initialize() {
        ball.layoutXProperty().addListener((observable, oldValue, newValue) -> checkCollision());
        ball.layoutYProperty().addListener((observable, oldValue, newValue) -> checkCollision());

        leftRectangle.layoutXProperty().addListener((observable, oldValue, newValue) -> checkCollision());
        leftRectangle.layoutYProperty().addListener((observable, oldValue, newValue) -> checkCollision());
    }

    private void checkCollision() {
        boolean isColliding = ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent());
        if (isColliding) {
            ball.setFill(Color.GREEN);
        } else {
            ball.setFill(Color.WHITE);
        }
    }

    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> handleUpKey();
            case DOWN -> handleDownKey();
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        currentUpSpeed = INITIAL_SPEED;
        currentDownSpeed = INITIAL_SPEED;
    }

    private void handleUpKey() {
        if ((leftRectangle.getLayoutY() - currentUpSpeed) <= 0) {
            leftRectangle.setLayoutY(0);
        } else {
            leftRectangle.setLayoutY(leftRectangle.getLayoutY() - currentUpSpeed);
        }
        if (currentUpSpeed < MAX_SPEED) {
            currentUpSpeed += ACCELERATION;
        }
    }

    private void handleDownKey() {
        if ((leftRectangle.getLayoutY() + currentDownSpeed) >= (WINDOW_HEIGHT - leftRectangle.getHeight())) {
            leftRectangle.setLayoutY(WINDOW_HEIGHT - leftRectangle.getHeight());
        } else {
            leftRectangle.setLayoutY(leftRectangle.getLayoutY() + currentDownSpeed);
        }
        if (currentDownSpeed < MAX_SPEED) {
            currentDownSpeed += ACCELERATION;
        }
    }
}