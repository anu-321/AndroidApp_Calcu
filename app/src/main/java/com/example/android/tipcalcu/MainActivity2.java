package com.example.android.tipcalcu;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SnakeView snakeView = findViewById(R.id.snakeView);
        TextView scoreView = findViewById(R.id.snakeScore);
        TextView statusView = findViewById(R.id.snakeStatus);

        snakeView.setGameListener(new SnakeView.GameListener() {
            @Override
            public void onScoreChanged(int newScore) {
                scoreView.setText(getString(R.string.snake_score, newScore));
                statusView.setText(R.string.snake_status_playing);
            }

            @Override
            public void onGameOver() {
                statusView.setText(R.string.snake_status_game_over);
            }
        });

        Button up = findViewById(R.id.btnSnakeUp);
        Button down = findViewById(R.id.btnSnakeDown);
        Button left = findViewById(R.id.btnSnakeLeft);
        Button right = findViewById(R.id.btnSnakeRight);
        Button restart = findViewById(R.id.btnSnakeRestart);

        up.setOnClickListener(v -> snakeView.setDirection(Direction.UP));
        down.setOnClickListener(v -> snakeView.setDirection(Direction.DOWN));
        left.setOnClickListener(v -> snakeView.setDirection(Direction.LEFT));
        right.setOnClickListener(v -> snakeView.setDirection(Direction.RIGHT));
        restart.setOnClickListener(v -> snakeView.restart());

        snakeView.requestFocus();
        snakeView.restart();
    }
}
