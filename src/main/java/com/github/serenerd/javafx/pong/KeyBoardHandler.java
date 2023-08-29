package com.github.serenerd.javafx.pong;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

public class KeyBoardHandler {

    private static final double RECTANGLE_SPEED = 8d;
    private static final BooleanProperty wPressed = new SimpleBooleanProperty();
    private static final BooleanProperty sPressed = new SimpleBooleanProperty();
    private static final BooleanProperty upPressed = new SimpleBooleanProperty();
    private static final BooleanProperty downPressed = new SimpleBooleanProperty();

    public static void handleKeyPressed(KeyEvent key) {
        if (key.getCode() == KeyCode.W) {
            wPressed.set(true);
        }
        if (key.getCode() == KeyCode.UP) {
            upPressed.set(true);
        }
        if (key.getCode() == KeyCode.S) {
            sPressed.set(true);
        }
        if (key.getCode() == KeyCode.DOWN) {
            downPressed.set(true);
        }
    }

    public static void handleKeyReleased(KeyEvent key) {
        if (key.getCode() == KeyCode.W) {
            wPressed.set(false);
        }
        if (key.getCode() == KeyCode.UP) {
            upPressed.set(false);
        }
        if (key.getCode() == KeyCode.S) {
            sPressed.set(false);
        }
        if (key.getCode() == KeyCode.DOWN) {
            downPressed.set(false);
        }
    }

    public static void handleRectangles(Rectangle rightRectangle, Rectangle leftRectangle) {
        if (wPressed.get()) {
            handleWKey(leftRectangle);
        }
        if (sPressed.get()) {
            handleSKey(leftRectangle);
        }
        if (upPressed.get()) {
            handleUpKey(rightRectangle);
        }
        if (downPressed.get()) {
            handleDownKey(rightRectangle);
        }
    }

    public static void handleUpKey(Rectangle rightRectangle) {
        if ((rightRectangle.getLayoutY() - RECTANGLE_SPEED) <= 0) {
            rightRectangle.setLayoutY(0);
        } else {
            rightRectangle.setLayoutY(rightRectangle.getLayoutY() - RECTANGLE_SPEED);
        }
    }

    public static void handleDownKey(Rectangle rightRectangle) {
        if ((rightRectangle.getLayoutY() + RECTANGLE_SPEED) >= (PongApplication.WINDOW_HEIGHT - rightRectangle.getHeight())) {
            rightRectangle.setLayoutY(PongApplication.WINDOW_HEIGHT - rightRectangle.getHeight());
        } else {
            rightRectangle.setLayoutY(rightRectangle.getLayoutY() + RECTANGLE_SPEED);
        }
    }

    public static void handleWKey(Rectangle leftRectangle) {
        if ((leftRectangle.getLayoutY() - RECTANGLE_SPEED) <= 0) {
            leftRectangle.setLayoutY(0);
        } else {
            leftRectangle.setLayoutY(leftRectangle.getLayoutY() - RECTANGLE_SPEED);
        }
    }

    public static void handleSKey(Rectangle leftRectangle) {
        if ((leftRectangle.getLayoutY() + RECTANGLE_SPEED) >= (PongApplication.WINDOW_HEIGHT - leftRectangle.getHeight())) {
            leftRectangle.setLayoutY(PongApplication.WINDOW_HEIGHT - leftRectangle.getHeight());
        } else {
            leftRectangle.setLayoutY(leftRectangle.getLayoutY() + RECTANGLE_SPEED);
        }
    }
}