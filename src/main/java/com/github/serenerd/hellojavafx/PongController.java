package com.github.serenerd.hellojavafx;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Random;

import static com.github.serenerd.hellojavafx.BallHelper.INITIAL_BALL_SPEED;
import static com.github.serenerd.hellojavafx.BallHelper.increaseBallSpeed;
import static com.github.serenerd.hellojavafx.KeyBoardHelper.handleRectangles;
import static com.github.serenerd.hellojavafx.PongApplication.WINDOW_HEIGHT;
import static com.github.serenerd.hellojavafx.PongApplication.WINDOW_WIDTH;

public class PongController {

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

    private final Random random = new Random();

    private int player1Score = 0;
    private int player2Score = 0;

    private double ballSpeed = INITIAL_BALL_SPEED;
    private double ballNextX;
    private double ballNextY;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            handleBall();
            handleRectangles(rightRectangle, leftRectangle);
        }
    };

    public void initialize() {
        defineBallInitialDirection();
        timer.start();
    }

    private void handleBall() {
        double vectorX = ballNextX - ball.getLayoutX();
        double vectorY = ballNextY - ball.getLayoutY();
        ball.setLayoutX(ballNextX);
        ball.setLayoutY(ballNextY);
        if (isRectangleHit()) {
            deflectBallOfRectangle(vectorY);
            ballSpeed = increaseBallSpeed(ballSpeed);
        } else if (isScoreHit()) {
            scoreHit();
        } else {
            moveBallForward(vectorX, vectorY);
        }
    }

    private void moveBallForward(double vectorX, double vectorY) {
        Bounds boundsInParent = ball.getBoundsInParent();
        if (vectorX > 0) {
            if (boundsInParent.getMaxX() + ballSpeed < WINDOW_WIDTH) {
                ballNextX += ballSpeed;
            } else {
                ballNextX += WINDOW_WIDTH - boundsInParent.getMaxX();
            }
        } else {
            if (boundsInParent.getMinX() - ballSpeed > 0) {
                ballNextX -= ballSpeed;
            } else {
                ballNextX -= ballSpeed - boundsInParent.getMinX();
            }
        }
        if (vectorY > 0) {
            if (boundsInParent.getMaxY() + ballSpeed < WINDOW_HEIGHT) {
                ballNextY += ballSpeed;
            } else {
                // deflect off the lower side
                ballNextY += WINDOW_HEIGHT - boundsInParent.getMaxY();
            }
        } else {
            if (boundsInParent.getMinY() - ballSpeed > 0) {
                ballNextY -= ballSpeed;
            } else {
                // deflect off the upper side
                ballNextY += ballSpeed;
            }
        }
    }

    private void defineBallInitialDirection() {
        random.nextInt(2);
        if (random.nextInt(2) == 0) {
            this.ballNextX = ball.getLayoutX() + ballSpeed;
        } else {
            this.ballNextX = ball.getLayoutX() - ballSpeed;
        }
        if (random.nextInt(2) == 0) {
            this.ballNextY = ball.getLayoutY() + ballSpeed;
        } else {
            this.ballNextY = ball.getLayoutY() - ballSpeed;
        }
    }

    private boolean isScoreHit() {
        return ball.getBoundsInParent().getMinX() <= 0 || ball.getBoundsInParent().getMaxX() >= WINDOW_WIDTH;
    }

    private boolean isRectangleHit() {
        return ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent())
                || ball.getBoundsInParent().intersects(rightRectangle.getBoundsInParent());
    }

    public void scoreHit() {
        Bounds ballBounds = ball.getBoundsInParent();
        double minX = ballBounds.getMinX();
        double maxX = ballBounds.getMaxX();
        if (minX == 0) {
            // ball hits left side of the screen
            player2Score++;
            scoreText2.setText(String.valueOf(player2Score));
            resetBall();
        } else if (maxX == WINDOW_WIDTH) {
            // ball hits right side of the screen
            player1Score++;
            scoreText1.setText(String.valueOf(player1Score));
            resetBall();
        }
    }

    // fixme даг, если мяч одновременно коснулся платформы и левого края окна ???
    private void deflectBallOfRectangle(double vectorY) {
        if (ball.getBoundsInParent().getMinX() <= leftRectangle.getBoundsInParent().getMaxX() && vectorY > 0) {
            ballNextX += ballSpeed;
            ballNextY += ballSpeed;
        } else if (ball.getBoundsInParent().getMinX() <= leftRectangle.getBoundsInParent().getMaxX() && vectorY < 0) {
            ballNextX += ballSpeed;
            ballNextY -= ballSpeed;
        } else if (ball.getBoundsInParent().getMaxX() >= rightRectangle.getBoundsInParent().getMinX() && vectorY > 0) {
            ballNextX -= ballSpeed;
            ballNextY += ballSpeed;
        } else if (ball.getBoundsInParent().getMaxX() >= rightRectangle.getBoundsInParent().getMinX() && vectorY < 0) {
            ballNextX -= ballSpeed;
            ballNextY -= ballSpeed;
        } else {
            throw new IllegalStateException("Unexpected ball rectangle collision");
        }
    }

    private void resetBall() {
        ball.setLayoutX(WINDOW_WIDTH / 2);
        ball.setLayoutY(WINDOW_HEIGHT / 2);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ballSpeed = INITIAL_BALL_SPEED;
        defineBallInitialDirection();
    }

    public void handleKeyReleased(KeyEvent ignoredKey) {
        KeyBoardHelper.handleKeyReleased();
    }

    public void handleKeyPressed(KeyEvent event) {
        KeyBoardHelper.handleKeyPressed(event);
    }
}