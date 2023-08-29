package com.github.serenerd.javafx.pong;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

public class KeyBoardHandler {

    private static final double RECTANGLE_SPEED = 8d;
    private static boolean wPressed;
    private static boolean sPressed;
    private static boolean upPressed;
    private static boolean downPressed;

    public static void handleKeyPressed(KeyEvent key) {
        if (key.getCode() == KeyCode.W) {
            wPressed = true;
        }
        if (key.getCode() == KeyCode.UP) {
            upPressed = true;
        }
        if (key.getCode() == KeyCode.S) {
            sPressed = true;
        }
        if (key.getCode() == KeyCode.DOWN) {
            downPressed = true;
        }
    }

    public static void handleKeyReleased(KeyEvent key) {
        if (key.getCode() == KeyCode.W) {
            wPressed = false;
        }
        if (key.getCode() == KeyCode.UP) {
            upPressed = false;
        }
        if (key.getCode() == KeyCode.S) {
            sPressed = false;
        }
        if (key.getCode() == KeyCode.DOWN) {
            downPressed = false;
        }
    }

    public static void handleRectangles(Rectangle rightRectangle, Rectangle leftRectangle) {
        if (wPressed) {
            handleWKey(leftRectangle);
        }
        if (sPressed) {
            handleSKey(leftRectangle);
        }
        if (upPressed) {
            handleUpKey(rightRectangle);
        }
        if (downPressed) {
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