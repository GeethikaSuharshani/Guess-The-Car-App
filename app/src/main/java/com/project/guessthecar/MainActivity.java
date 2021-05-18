package com.project.guessthecar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity {
    public static final int AVAILABLE_IMAGES = 60;
    public static final String TIMER_STATUS = "com.project.guessthecar.extra.TIMER_STATUS";
    private SwitchCompat timerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerSwitch = findViewById(R.id.timerSwitch);
        SharedPreferences preferences = getSharedPreferences("com.project.guessthecar", MODE_PRIVATE); //get preference data related to the application
        timerSwitch.setChecked(preferences.getBoolean("spinner_status", false));
        //restore the preserved state of the MainActivity
        if (savedInstanceState != null) {
            boolean timerOn = savedInstanceState.getBoolean("timer_on");
            timerSwitch.setChecked(timerOn);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //save preference data
        SharedPreferences.Editor editor = getSharedPreferences("com.project.guessthecar", MODE_PRIVATE).edit();
        editor.putBoolean("spinner_status", timerSwitch.isChecked());
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save the UI state of the MainActivity
        super.onSaveInstanceState(outState);
        outState.putBoolean("timer_on", timerSwitch.isChecked());
    }

    public void showIdentifyCarMake(View view) { //start IdentifyCarMakeActivity
        Intent intent = new Intent(MainActivity.this, IdentifyCarMakeActivity.class);
        intent.putExtra(TIMER_STATUS, timerSwitch.isChecked());
        startActivity(intent);
    }

    public void showHints(View view) { //start HintsActivity
        Intent intent = new Intent(MainActivity.this, HintsActivity.class);
        intent.putExtra(TIMER_STATUS, timerSwitch.isChecked());
        startActivity(intent);
    }

    public void showIdentifyCar(View view) { //start IdentifyCarImageActivity
        Intent intent = new Intent(MainActivity.this, IdentifyCarImageActivity.class);
        intent.putExtra(TIMER_STATUS, timerSwitch.isChecked());
        startActivity(intent);
    }

    public void showAdvancedLevel(View view) { //start AdvancedLevelActivity
        Intent intent = new Intent(MainActivity.this, AdvancedLevelActivity.class);
        intent.putExtra(TIMER_STATUS, timerSwitch.isChecked());
        startActivity(intent);
    }

}