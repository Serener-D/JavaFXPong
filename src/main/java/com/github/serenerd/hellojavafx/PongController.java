package com.github.serenerd.hellojavafx;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static com.github.serenerd.hellojavafx.PongApplication.WINDOW_HEIGHT;
import static com.github.serenerd.hellojavafx.PongApplication.WINDOW_WIDTH;

public class PongController {

    private static final double RECTANGLE_SPEED = 8;
    @FXML
    private Rectangle leftRectangle;
    @FXML
    private Rectangle rightRectangle;
    @FXML
    private Circle ball;
    @FXML
    private Text scoreText1;
    @FXML
    private Text scoreText2;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;
    private int score1 = 0;
    private int score2 = 0;

    private double ballSpeed = 5d;
    private double ballCurrentX = WINDOW_WIDTH / 2;
    private double ballCurrentY = WINDOW_HEIGHT / 2;
    private double ballNextX;
    private double ballNextY;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            handleBall();
            handleRectangles();
        }
    };

    private void handleBall() {
        if (ballCurrentX < ballNextX) {
            ballCurrentX += ballSpeed;
            ballNextX += ballSpeed;
        } else {
            ballCurrentX -= ballSpeed;
            ballNextX -= ballSpeed;
        }
        if (ballCurrentY < ballNextY) {
            ballCurrentY += ballSpeed;
            ballNextY += ballSpeed;
        } else {
            ballCurrentY -= ballSpeed;
            ballNextY -= ballSpeed;
        }
        ball.setLayoutX(ballCurrentX);
        ball.setLayoutY(ballCurrentY);
    }

    private void handleRectangles() {
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

    public void initialize() {
        defineBallInitialDirection();
        timer.start();
        ball.layoutXProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        ball.layoutYProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        leftRectangle.layoutXProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        leftRectangle.layoutYProperty().addListener((observable, oldValue, newValue) -> checkBallCollision());
        leftRectangle.layoutYProperty().addListener((observable, oldValue, newValue) -> checkRectangleCollision());
    }

    private void defineBallInitialDirection() {
        // fixme временный хардкод
        this.ballNextX = ballCurrentX + ballSpeed;
        this.ballNextY = ballCurrentY + ballSpeed;
    }

    private void checkRectangleCollision() {
        Bounds bounds = leftRectangle.getBoundsInParent();
        boolean isColliding = ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent()) ||
                bounds.getMaxY() >= WINDOW_HEIGHT ||
                bounds.getMinY() <= 0;
        if (isColliding) {
            scoreText1.setText(Integer.toString(++score1));
            ball.setFill(Color.GREEN);
        } else {
            ball.setFill(Color.WHITE);
        }
    }

    private void checkBallCollision() {
        Bounds ballBounds = ball.getBoundsInParent();
        double minX = ballBounds.getMinX();
        double maxX = ballBounds.getMaxX();
        double minY = ballBounds.getMinY();
        double maxY = ballBounds.getMaxY();
        if (minX <= 0 || maxX >= WINDOW_WIDTH || minY <= 0 || maxY >= WINDOW_HEIGHT) {
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

    //fixme если один игрок отпускает кнопку, то сбивается движение второго
    public void handleKeyReleased(KeyEvent ignoredKey) {
        upPressed = false;
        downPressed = false;
        wPressed = false;
        sPressed = false;
    }

    public void handleUpKey() {
        if ((rightRectangle.getLayoutY() - RECTANGLE_SPEED) <= 0) {
            rightRectangle.setLayoutY(0);
        } else {
            rightRectangle.setLayoutY(rightRectangle.getLayoutY() - RECTANGLE_SPEED);
        }
    }

    public void handleDownKey() {
        if ((rightRectangle.getLayoutY() + RECTANGLE_SPEED) >= (WINDOW_HEIGHT - rightRectangle.getHeight())) {
            rightRectangle.setLayoutY(WINDOW_HEIGHT - rightRectangle.getHeight());
        } else {
            rightRectangle.setLayoutY(rightRectangle.getLayoutY() + RECTANGLE_SPEED);
        }
    }

    public void handleWKey() {
        if ((leftRectangle.getLayoutY() - RECTANGLE_SPEED) <= 0) {
            leftRectangle.setLayoutY(0);
        } else {
            leftRectangle.setLayoutY(leftRectangle.getLayoutY() - RECTANGLE_SPEED);
        }
    }

    public void handleSKey() {
        if ((leftRectangle.getLayoutY() + RECTANGLE_SPEED) >= (WINDOW_HEIGHT - leftRectangle.getHeight())) {
            leftRectangle.setLayoutY(WINDOW_HEIGHT - leftRectangle.getHeight());
        } else {
            leftRectangle.setLayoutY(leftRectangle.getLayoutY() + RECTANGLE_SPEED);
        }
    }
}