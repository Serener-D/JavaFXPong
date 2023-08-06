package com.github.serenerd.hellojavafx;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
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