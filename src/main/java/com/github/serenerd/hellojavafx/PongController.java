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
public class PongController {

    private static final double RECTANGLE_SPEED = 8d;
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

    private double ballSpeed = 5d;
    private static final double BALL_SPEED_LIMIT = 10d;
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
    }

    private void defineBallInitialDirection() {
        random.nextInt(2);
        if (random.nextInt(2) == 0) {
            this.ballNextX = ballCurrentX + ballSpeed;
        } else {
            this.ballNextX = ballCurrentX - ballSpeed;
        }
        if (random.nextInt(2) == 0) {
            this.ballNextY = ballCurrentY + ballSpeed;
        } else {
            this.ballNextY = ballCurrentY - ballSpeed;
        }
    }

    private void checkBallCollision() {
        Bounds ballBounds = ball.getBoundsInParent();
        double minX = ballBounds.getMinX();
        double maxX = ballBounds.getMaxX();
        double minY = ballBounds.getMinY();
        double maxY = ballBounds.getMaxY();
        if (minX <= 0 || maxX >= WINDOW_WIDTH || minY <= 0 || maxY >= WINDOW_HEIGHT) {
            // ball hit one of the walls
            deflectBallOfTheWall();
            ball.setFill(Color.GREEN);
        } else if (ball.getBoundsInParent().intersects(leftRectangle.getBoundsInParent())
                || ball.getBoundsInParent().intersects(rightRectangle.getBoundsInParent())) {
            // ball hit rectangles
            deflectBallOfRectangle();
            ball.setFill(Color.GREEN);
        } else {
            // ball flies peacefully
            ball.setFill(Color.WHITE);
        }
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
        } else if (ballCurrentX < ballNextX && (ballNextY + ball.getRadius()) >= WINDOW_HEIGHT) {
            // ball hits bottom side of the screen moving from left to right
            ballNextY -= 2 * ballSpeed;
        } else if (ballCurrentX > ballNextX && (ballNextY + ball.getRadius()) >= WINDOW_HEIGHT) {
            // ball hits bottom side of the screen moving from right to left
            ballNextY -= 2 * ballSpeed;
        } else if (ballCurrentX < ballNextX && (ballNextY - ball.getRadius()) <= 0) {
            // ball hits upper side of the screen moving from left to right
            ballNextY += 2 * ballSpeed;
        } else if (ballCurrentX > ballNextX && (ballNextY - ball.getRadius()) <= 0) {
            // ball hits upper side of the screen moving from right to left
            ballNextY += 2 * ballSpeed;
        } else {
            throw new IllegalStateException("Unexpected ball window collision");
        }
    }

    // fixme переделать под умное определение, части плафтормы, в которую попал мяч
    private void deflectBallOfRectangle() {
        Bounds ballBounds = ball.getBoundsInParent();
        double minX = ballBounds.getMinX();
        double maxX = ballBounds.getMaxX();
        if (minX <= leftRectangle.getBoundsInParent().getMaxX()) {
            if (ballNextY > ballCurrentY) {
                // ball hits left rectangle going upwards
                ballNextX += ballSpeed;
            } else {
                ballNextX += ballSpeed;
                // ball hits left rectangle going downwards
            }
            increaseBallSpeed();
        } else if (maxX >= rightRectangle.getBoundsInParent().getMinX()) {
            if (ballNextY > ballCurrentY) {
                ballNextX -= ballSpeed;
                // ball hits right rectangle going upwards
            } else {
                ballNextX -= ballSpeed;
                // ball hits right rectangle going downwards
            }
            increaseBallSpeed();
        } else {
            throw new IllegalStateException("Unexpected ball rectangle collision");
        }
    }

    //fixme если один игрок отпускает кнопку, то сбивается движение второго
    public void handleKeyReleased(KeyEvent ignoredKey) {
        upPressed = false;
        downPressed = false;
        wPressed = false;
        sPressed = false;
    }

    private void resetBall() {
        ballCurrentX = WINDOW_WIDTH / 2;
        ballCurrentY = WINDOW_HEIGHT / 2;
        ball.setLayoutX(ballCurrentX);
        ball.setLayoutY(ballCurrentY);
        defineBallInitialDirection();
    }

    private void increaseBallSpeed() {
        if (ballSpeed != BALL_SPEED_LIMIT) {
            ballSpeed += 1d;
        }
    }

    // ДАЛЕЕ ИДУТ МЕТОДЫ ДЛЯ КОНТРОЛЯ ПЛАТФОРМ И КЛАВИАТУРЫ
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