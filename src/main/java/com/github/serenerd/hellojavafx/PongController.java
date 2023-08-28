package com.github.serenerd.hellojavafx;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Random;

import static com.github.serenerd.hellojavafx.AudioHandler.playAudio;
import static com.github.serenerd.hellojavafx.KeyBoardHandler.handleRectangles;
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

    public static final double INITIAL_BALL_SPEED = 4d;
    private static final double BALL_SPEED_LIMIT = 10d;
    private double ballSpeed = INITIAL_BALL_SPEED;
    private double ballNextX;
    private double ballNextY;

    public void initialize() {
        defineBallInitialDirection();
        timer.start();
    }

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            handleBall();
            handleRectangles(rightRectangle, leftRectangle);
        }

        private void handleBall() {
            double vectorX = ballNextX - ball.getLayoutX();
            double vectorY = ballNextY - ball.getLayoutY();
            ball.setLayoutX(ballNextX);
            ball.setLayoutY(ballNextY);
            if (isRectangleHit()) {
                deflectBallOfRectangle();
                increaseBallSpeed();
            } else if (isScoreHit()) {
                scoreHit();
                resetBall();
            } else {
                moveBallForward(vectorX, vectorY);
            }
        }

        private void increaseBallSpeed() {
            if (ballSpeed != BALL_SPEED_LIMIT) {
                ballSpeed += 0.5d;
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
                    ballNextX -= boundsInParent.getMinX();
                }
            }
            if (vectorY > 0) {
                if (boundsInParent.getMaxY() + ballSpeed < WINDOW_HEIGHT) {
                    ballNextY += ballSpeed;
                } else {
                    // deflect off the lower side
                    ballNextY -= ballSpeed;
                }
            } else if (vectorY < 0) {
                if (boundsInParent.getMinY() - ballSpeed > 0) {
                    ballNextY -= ballSpeed;
                } else {
                    // deflect off the upper side
                    ballNextY += ballSpeed;
                }
            }
        }

        private void deflectBallOfRectangle() {
            playAudio();
            double ballY = ball.getBoundsInParent().getCenterY();
            if (ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent())) {
                ballNextX += ballSpeed;
                calculateRectangleHitYDirection(ballY, leftRectangle.getBoundsInParent().getMinY());
            } else if (ball.getBoundsInParent().intersects(rightRectangle.getBoundsInParent())) {
                ballNextX -= ballSpeed;
                calculateRectangleHitYDirection(ballY, rightRectangle.getBoundsInParent().getMinY());
            }
        }

        private void calculateRectangleHitYDirection(double ballY, double rectangleMinY) {
            if (ballY < rectangleMinY + 40) {
                // upper part of rectangle
                ballNextY -= ballSpeed;
            } else if (ballY < rectangleMinY + 50) {
                // middle part of rectangle
                ballNextY = ballY;
            } else {
                // lower part of rectangle
                ballNextY += ballSpeed;
            }
        }

        private boolean isScoreHit() {
            return ball.getBoundsInParent().getMinX() <= 0 || ball.getBoundsInParent().getMaxX() >= WINDOW_WIDTH;
        }

        private boolean isRectangleHit() {
            return ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent())
                    || ball.getBoundsInParent().intersects(rightRectangle.getBoundsInParent());
        }

        private void resetBall() {
            ball.setLayoutX(WINDOW_WIDTH / 2);
            ball.setLayoutY(WINDOW_HEIGHT / 2);
            ballSpeed = INITIAL_BALL_SPEED;
            defineBallInitialDirection();
        }
    };

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

    public void scoreHit() {
        Bounds ballBounds = ball.getBoundsInParent();
        double minX = ballBounds.getMinX();
        double maxX = ballBounds.getMaxX();
        if (minX == 0) {
            // ball hits left side of the screen
            player2Score++;
            scoreText2.setText(String.valueOf(player2Score));
        } else if (maxX == WINDOW_WIDTH) {
            // ball hits right side of the screen
            player1Score++;
            scoreText1.setText(String.valueOf(player1Score));
        }
    }

    public void handleKeyReleased(KeyEvent key) {
        KeyBoardHandler.handleKeyReleased(key);
    }

    public void handleKeyPressed(KeyEvent key) {
        KeyBoardHandler.handleKeyPressed(key);
    }
}