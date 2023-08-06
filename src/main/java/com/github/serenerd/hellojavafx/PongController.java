package com.github.serenerd.hellojavafx;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

public class PongController {

    private static final double INITIAL_SPEED = 1;
    private static final double MAX_SPEED = 200;
    private static final double ACCELERATION = 10;
    private double currentLeftSpeed = INITIAL_SPEED;
    @FXML
    private Rectangle leftRect;

    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W -> {
                // todo возможно надо двигать на один-пять пикселей, чтобы было плавнее, но сделать большое ускорение
                // todo тут надо более точно замерять оставшееся расстояние до границы, чтобы не улетать за границу
                if (leftRect.getLayoutY() - currentLeftSpeed >= 0) {
                    leftRect.setLayoutY(leftRect.getLayoutY() - currentLeftSpeed);
                }
            }
            case S -> {
                // fixme hardcode
                if (leftRect.getLayoutY() + currentLeftSpeed <= 400) {
                    leftRect.setLayoutY(leftRect.getLayoutY() + currentLeftSpeed);
                }
            }
        }
        if (currentLeftSpeed < MAX_SPEED) {
            currentLeftSpeed += ACCELERATION;
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        currentLeftSpeed = INITIAL_SPEED;
    }
}