package com.example.android.tipcalcu;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class SnakeGame {
    private final int width;
    private final int height;
    private final Random random;
    private final Deque<Position> snake = new ArrayDeque<>();
    private Direction direction = Direction.RIGHT;
    private Position food;
    private int score;
    private boolean gameOver;

    public SnakeGame(int width, int height, Random random) {
        this.width = width;
        this.height = height;
        this.random = random;
        reset();
    }

    public SnakeGame(int width, int height) {
        this(width, height, new Random());
    }

    public void reset() {
        snake.clear();
        int centerX = width / 2;
        int centerY = height / 2;
        snake.add(new Position(centerX, centerY));
        snake.add(new Position(centerX - 1, centerY));
        snake.add(new Position(centerX - 2, centerY));
        direction = Direction.RIGHT;
        score = 0;
        gameOver = false;
        spawnFood();
    }

    public void changeDirection(Direction requested) {
        if (requested == null || requested.isOpposite(direction)) {
            return;
        }
        direction = requested;
    }

    public void step() {
        if (gameOver) {
            return;
        }

        Position head = snake.peekFirst();
        if (head == null) {
            reset();
            return;
        }

        Position next = head.move(direction);
        if (isOutOfBounds(next)) {
            gameOver = true;
            return;
        }

        boolean shouldGrow = next.equals(food);
        if (!shouldGrow) {
            snake.removeLast();
        }

        if (snake.contains(next)) {
            gameOver = true;
            return;
        }

        snake.addFirst(next);

        if (shouldGrow) {
            score++;
            spawnFood();
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public List<Position> getSnake() {
        return new ArrayList<>(snake);
    }

    public Position getFood() {
        return food;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    void setFoodForTesting(Position position) {
        this.food = position;
    }

    private boolean isOutOfBounds(Position position) {
        return position.x < 0 || position.y < 0 || position.x >= width || position.y >= height;
    }

    private void spawnFood() {
        while (true) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Position candidate = new Position(x, y);
            if (!snake.contains(candidate)) {
                food = candidate;
                return;
            }
        }
    }

    public static final class Position {
        public final int x;
        public final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position move(Direction direction) {
            return new Position(x + direction.dx(), y + direction.dy());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Position)) {
                return false;
            }
            Position other = (Position) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }
}
