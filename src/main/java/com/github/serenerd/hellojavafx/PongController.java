package com.github.serenerd.hellojavafx;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Random;

import static com.github.serenerd.hellojavafx.PongApplication.WINDOW_HEIGHT;
import static com.github.serenerd.hellojavafx.PongApplication.WINDOW_WIDTH;

// fixme сделай отдельный класс для контроля клавиатуры
// fixme сделай отдельный класс для контроля мяча
// fixme дичь, надо упростить все это
// fixme сейчас есть баги. скорее всего, из-за неправильного рассчета перемещений, из-за хардкода +ballSpeed
public class PongController {

    private static final double RECTANGLE_SPEED = 8d;
    private static final double INITIAL_BALL_SPEED = 4d;
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

    private Random random = new Random();

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean wPressed = false;
    private boolean sPressed = false;
    private int player1Score = 0;
    private int player2Score = 0;

    private double ballSpeed = INITIAL_BALL_SPEED;
    private static final double BALL_SPEED_LIMIT = 10d;
    private double ballNextX;
    private double ballNextY;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            handleBall();
            handleRectangles();
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
        isRectangleHit();
        if (!isRectangleHit()) {
            moveBallForward(vectorX, vectorY);
        } else {
            deflectBallOfRectangle(vectorY);
        }
        checkBallWallCollision();
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
                ballNextY += WINDOW_HEIGHT - boundsInParent.getMaxY();
            }
        } else {
            if (boundsInParent.getMinY() - ballSpeed > 0) {
                ballNextY -= ballSpeed;
            } else {
                ballNextY -= boundsInParent.getMinY() - ballSpeed;
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

    private void checkBallWallCollision() {
        Bounds ballBounds = ball.getBoundsInParent();
        if (ballBounds.getMinX() <= 0
                || ballBounds.getMaxX() >= WINDOW_WIDTH
                || ballBounds.getMinY() <= 0
                || ballBounds.getMaxY() >= WINDOW_HEIGHT) {
            // ball hit one of the walls
            deflectBallOfTheWall();
            ball.setFill(Color.GREEN);
        } else {
            // ball flies peacefully
            ball.setFill(Color.WHITE);
        }
    }

    private boolean isRectangleHit() {
        return ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent())
                || ball.getBoundsInParent().intersects(rightRectangle.getBoundsInParent());
    }

    public void deflectBallOfTheWall() {
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
        } else if (ball.getLayoutX() < ballNextX && (ballNextY + ball.getRadius()) >= WINDOW_HEIGHT) {
            // ball hits bottom side of the screen moving from left to right
            ballNextY -= 1;
        } else if (ball.getLayoutX() > ballNextX && (ballNextY + ball.getRadius()) >= WINDOW_HEIGHT) {
            // ball hits bottom side of the screen moving from right to left
            ballNextY -= 1;
        } else if (ball.getLayoutX() < ballNextX && (ballNextY - ball.getRadius()) <= 0) {
            // ball hits upper side of the screen moving from left to right
            ballNextY += 1;
        } else if (ball.getLayoutX() > ballNextX && (ballNextY - ball.getRadius()) <= 0) {
            // ball hits upper side of the screen moving from right to left
            ballNextY += 1;
        } else {
            throw new IllegalStateException("Unexpected ball window collision");
        }
    }

    private void deflectBallOfRectangle(double vectorY) {
        ball.setFill(Color.GREEN);
        if (ball.getBoundsInParent().getMinX() <= leftRectangle.getBoundsInParent().getMaxX() && vectorY > 0) {
            ballNextX += 1;
            ballNextY += 1;
        } else if (ball.getBoundsInParent().getMinX() <= leftRectangle.getBoundsInParent().getMaxX() && vectorY < 0) {
            ballNextX += 1;
            ballNextY -= 1;
        } else if (ball.getBoundsInParent().getMaxX() >= rightRectangle.getBoundsInParent().getMinX() && vectorY > 0) {
            ballNextX -= 1;
            ballNextY += 1;
        } else if (ball.getBoundsInParent().getMaxX() >= rightRectangle.getBoundsInParent().getMinX() && vectorY < 0) {
            ballNextX -= 1;
            ballNextY -= 1;
        } else {
            throw new IllegalStateException("Unexpected ball rectangle collision");
        }
        increaseBallSpeed();
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

    private void increaseBallSpeed() {
        if (ballSpeed != BALL_SPEED_LIMIT) {
            ballSpeed += 0.5d;
        }
    }

    // ДАЛЕЕ ИДУТ МЕТОДЫ ДЛЯ КОНТРОЛЯ ПЛАТФОРМ И КЛАВИАТУРЫ
    //fixme если один игрок отпускает кнопку, то сбивается движение второго
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

    public void handleKeyReleased(KeyEvent ignoredKey) {
        upPressed = false;
        downPressed = false;
        wPressed = false;
        sPressed = false;
    }

    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> upPressed = true;
            case DOWN -> downPressed = true;
            case W -> wPressed = true;
            case S -> sPressed = true;
        }
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