package com.example.android.tipcalcu;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SnakeGameTest {
    @Test
    public void movesForward() {
        SnakeGame game = new SnakeGame(12, 12, new Random(0));
        SnakeGame.Position start = game.getSnake().get(0);
        game.step();
        SnakeGame.Position next = game.getSnake().get(0);
        assertEquals(start.y, next.y);
        assertEquals(start.x + 1, next.x);
    }

    @Test
    public void growsAndScoresWhenEatingFood() {
        SnakeGame game = new SnakeGame(12, 12, new Random(0));
        SnakeGame.Position head = game.getSnake().get(0);
        SnakeGame.Position food = new SnakeGame.Position(head.x + 1, head.y);
        game.setFoodForTesting(food);
        game.step();
        assertEquals(1, game.getScore());
        assertEquals(4, game.getSnake().size());
        SnakeGame.Position bufferedFood = game.getFood();
        for (SnakeGame.Position segment : game.getSnake()) {
            assertFalse(bufferedFood.equals(segment));
        }
    }

    @Test
    public void detectsWallCollision() {
        SnakeGame game = new SnakeGame(12, 12, new Random(0));
        game.changeDirection(Direction.LEFT);
        for (int i = 0; i < 7; i++) {
            game.step();
        }
        assertTrue(game.isGameOver());
    }

    @Test
    public void foodAlwaysAvoidsSnake() {
        SnakeGame game = new SnakeGame(12, 12, new Random(0));
        SnakeGame.Position food = game.getFood();
        for (SnakeGame.Position segment : game.getSnake()) {
            assertFalse(food.equals(segment));
        }
    }
}
