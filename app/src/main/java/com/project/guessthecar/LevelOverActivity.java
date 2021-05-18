package com.project.guessthecar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LevelOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_over);
    }

    //go back to home screen
    public void backToHome(View view) {
        Intent intent = new Intent(LevelOverActivity.this, MainActivity.class);
        startActivity(intent);
    }

}