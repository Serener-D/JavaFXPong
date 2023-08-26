package com.github.serenerd.hellojavafx;

public class BallHelper {

    public static final double INITIAL_BALL_SPEED = 4d;
    private static final double BALL_SPEED_LIMIT = 10d;

    public static double increaseBallSpeed(double ballSpeed) {
        if (ballSpeed != BALL_SPEED_LIMIT) {
            ballSpeed += 0.5d;
        }
        return ballSpeed;
    }
}