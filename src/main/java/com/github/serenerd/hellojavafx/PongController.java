package com.github.serenerd.hellojavafx;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.github.serenerd.hellojavafx.PongApplication.WINDOW_HEIGHT;

public class PongController {

    private static final double SPEED = 8;

    @FXML
    private Rectangle leftRectangle;
    @FXML
    private Rectangle rightRectangle;
    @FXML
    private Circle ball;
    @FXML
    private Label score;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;

    private int playerScore1 = 0;
    private int playerScore2 = 0;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (upPressed) {
                handleUpKey();
            } else if (downPressed) {
                handleDownKey();
            }
            if (wPressed) {
                handleWKey();
            } else if (sPressed) {
                handleSKey();
            }
        }
    };

    public void initialize() {
        timer.start();
        score.setText(playerScore1 + " : " + playerScore2);
        ball.layoutXProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        ball.layoutYProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        leftRectangle.layoutXProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        leftRectangle.layoutYProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        leftRectangle.layoutYProperty().addListener((observable, oldValue, newValue) -> checkRectangleCollision());
    }

    private void checkRectangleCollision() {
        Bounds bounds = leftRectangle.getBoundsInParent();
        boolean isColliding = ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent()) ||
                bounds.getMaxY() >= WINDOW_HEIGHT ||
                bounds.getMinY() <= 0;
        if (isColliding) {
            score.setText(++playerScore1 + " : " + playerScore2);
            ball.setFill(Color.GREEN);
        } else {
            ball.setFill(Color.WHITE);
        }
    }

    private void checkBallCollision() {
        boolean isColliding = ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent());
        if (isColliding) {
            ball.setFill(Color.GREEN);
        } else {
            ball.setFill(Color.WHITE);
        }
    }

    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> upPressed = true;
            case DOWN -> downPressed = true;
            case W -> wPressed = true;
            case S -> sPressed = true;
        }
    }

    public void handleKeyReleased(KeyEvent ignoredKey) {
        upPressed = false;
        downPressed = false;
        wPressed = false;
        sPressed = false;
    }

    public void handleUpKey() {
        if ((rightRectangle.getLayoutY() - SPEED) <= 0) {
            rightRectangle.setLayoutY(0);
        } else {
            rightRectangle.setLayoutY(rightRectangle.getLayoutY() - SPEED);
        }
    }

    public void handleDownKey() {
        if ((rightRectangle.getLayoutY() + SPEED) >= (WINDOW_HEIGHT - rightRectangle.getHeight())) {
            rightRectangle.setLayoutY(WINDOW_HEIGHT - rightRectangle.getHeight());
        } else {
            rightRectangle.setLayoutY(rightRectangle.getLayoutY() + SPEED);
        }
    }

    public void handleWKey() {
        if ((leftRectangle.getLayoutY() - SPEED) <= 0) {
            leftRectangle.setLayoutY(0);
        } else {
            leftRectangle.setLayoutY(leftRectangle.getLayoutY() - SPEED);
        }
    }

    public void handleSKey() {
        if ((leftRectangle.getLayoutY() + SPEED) >= (WINDOW_HEIGHT - leftRectangle.getHeight())) {
            leftRectangle.setLayoutY(WINDOW_HEIGHT - leftRectangle.getHeight());
        } else {
            leftRectangle.setLayoutY(leftRectangle.getLayoutY() + SPEED);
        }
    }
}