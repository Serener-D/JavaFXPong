package com.github.serenerd.hellojavafx;

import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

import static com.github.serenerd.hellojavafx.PongApplication.WINDOW_HEIGHT;

//fixme если один игрок отпускает кнопку, то сбивается движение второго
public class KeyBoardHelper {

    private static final double RECTANGLE_SPEED = 8d;

    private static boolean upPressed = false;
    private static boolean downPressed = false;
    private static boolean wPressed = false;
    private static boolean sPressed = false;

    public static void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> upPressed = true;
            case DOWN -> downPressed = true;
            case W -> wPressed = true;
            case S -> sPressed = true;
        }
    }

    public static void handleKeyReleased() {
        upPressed = false;
        downPressed = false;
        wPressed = false;
        sPressed = false;
    }

    public static void handleRectangles(Rectangle rightRectangle, Rectangle leftRectangle) {
        if (upPressed) {
            KeyBoardHelper.handleUpKey(rightRectangle);
        } else if (downPressed) {
            KeyBoardHelper.handleDownKey(rightRectangle);
        }
        if (wPressed) {
            KeyBoardHelper.handleWKey(leftRectangle);
        } else if (sPressed) {
            KeyBoardHelper.handleSKey(leftRectangle);
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
        if ((rightRectangle.getLayoutY() + RECTANGLE_SPEED) >= (WINDOW_HEIGHT - rightRectangle.getHeight())) {
            rightRectangle.setLayoutY(WINDOW_HEIGHT - rightRectangle.getHeight());
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
        if ((leftRectangle.getLayoutY() + RECTANGLE_SPEED) >= (WINDOW_HEIGHT - leftRectangle.getHeight())) {
            leftRectangle.setLayoutY(WINDOW_HEIGHT - leftRectangle.getHeight());
        } else {
            leftRectangle.setLayoutY(leftRectangle.getLayoutY() + RECTANGLE_SPEED);
        }
    }
}
