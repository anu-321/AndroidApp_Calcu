package com.example.android.tipcalcu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

public class SnakeView extends View {
    private static final int GRID_SIZE = 16;
    private static final long TICK_MS = 250L;

    private final SnakeGame game = new SnakeGame(GRID_SIZE, GRID_SIZE);
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Paint snakePaint = new Paint();
    private final Paint headPaint = new Paint();
    private final Paint foodPaint = new Paint();
    private final Paint gridPaint = new Paint();
    private final Paint backgroundPaint = new Paint();

    private final Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            if (game.isGameOver()) {
                dispatchState();
                running = false;
                return;
            }
            game.step();
            dispatchState();
            invalidate();
            if (!game.isGameOver()) {
                handler.postDelayed(this, TICK_MS);
            } else {
                running = false;
            }
        }
    };

    private boolean running;
    private boolean gameOverNotified;
    private GameListener listener;
    private int lastScore = -1;

    public SnakeView(Context context) {
        this(context, null);
    }

    public SnakeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaints();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public void setGameListener(GameListener listener) {
        this.listener = listener;
        dispatchState();
    }

    public void restart() {
        handler.removeCallbacks(tickRunnable);
        running = false;
        game.reset();
        gameOverNotified = false;
        lastScore = -1;
        dispatchState();
        invalidate();
        startLoop();
    }

    public void setDirection(Direction direction) {
        game.changeDirection(direction);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!running) {
            startLoop();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(tickRunnable);
        running = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(backgroundPaint);
        float cellWidth = (float) getWidth() / game.getWidth();
        float cellHeight = (float) getHeight() / game.getHeight();
        drawGrid(canvas, cellWidth, cellHeight);
        drawFood(canvas, cellWidth, cellHeight);
        drawSnake(canvas, cellWidth, cellHeight);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Direction direction = null;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_W:
                direction = Direction.UP;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_S:
                direction = Direction.DOWN;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_A:
                direction = Direction.LEFT;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_D:
                direction = Direction.RIGHT;
                break;
        }
        if (direction != null) {
            setDirection(direction);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startLoop() {
        running = true;
        handler.removeCallbacks(tickRunnable);
        handler.postDelayed(tickRunnable, TICK_MS);
    }

    private void dispatchState() {
        if (listener == null) {
            return;
        }
        int score = game.getScore();
        if (score != lastScore) {
            listener.onScoreChanged(score);
            lastScore = score;
        }

        if (game.isGameOver() && !gameOverNotified) {
            listener.onGameOver();
            gameOverNotified = true;
        } else if (!game.isGameOver()) {
            gameOverNotified = false;
        }
    }

    private void drawGrid(Canvas canvas, float cellWidth, float cellHeight) {
        for (int x = 0; x <= game.getWidth(); x++) {
            canvas.drawLine(x * cellWidth, 0, x * cellWidth, getHeight(), gridPaint);
        }
        for (int y = 0; y <= game.getHeight(); y++) {
            canvas.drawLine(0, y * cellHeight, getWidth(), y * cellHeight, gridPaint);
        }
    }

    private void drawSnake(Canvas canvas, float cellWidth, float cellHeight) {
        List<SnakeGame.Position> segments = game.getSnake();
        if (segments.isEmpty()) {
            return;
        }
        for (int i = 0; i < segments.size(); i++) {
            SnakeGame.Position pos = segments.get(i);
            Paint paint = i == 0 ? headPaint : snakePaint;
            drawCell(canvas, cellWidth, cellHeight, pos, paint);
        }
    }

    private void drawFood(Canvas canvas, float cellWidth, float cellHeight) {
        SnakeGame.Position food = game.getFood();
        if (food != null) {
            drawCell(canvas, cellWidth, cellHeight, food, foodPaint);
        }
    }

    private void drawCell(Canvas canvas, float cellWidth, float cellHeight, SnakeGame.Position position, Paint paint) {
        float left = position.x * cellWidth;
        float top = position.y * cellHeight;
        canvas.drawRect(left, top, left + cellWidth, top + cellHeight, paint);
    }

    private void initPaints() {
        backgroundPaint.setColor(0xFF121212);
        snakePaint.setColor(0xFF388E3C);
        headPaint.setColor(0xFF6ABF69);
        foodPaint.setColor(0xFFE53935);
        gridPaint.setColor(0x22000000);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1f);
    }

    public interface GameListener {
        void onScoreChanged(int newScore);

        void onGameOver();
    }
}
