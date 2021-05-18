package com.project.guessthecar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IdentifyCarMakeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String VIEWED_IMAGES = "com.project.guessthecar.extra.VIEWED_IMAGES";
    private List<String> viewedImagesList = new ArrayList<>();
    private CountDownTimer timer;
    private boolean timerOn;
    private TextView countDown;
    private long timeRemaining;
    private ImageView carImageView;
    private List<String> randomImageDetails = new ArrayList<>();
    private String carMake;
    private Spinner spinner;
    private String selectedCarMake;
    private TextView resultText;
    private TextView correctAnswer;
    private Button identifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_make);

        countDown = findViewById(R.id.identify_car_make_count_down);
        carImageView = findViewById(R.id.identify_car_make_image);
        spinner = findViewById(R.id.car_make_spinner);
        resultText = findViewById(R.id.identify_car_make_result);
        correctAnswer = findViewById(R.id.identify_car_make_correct_answer);
        identifyBtn = findViewById(R.id.identify_btn);

        if (spinner != null) {
            spinner.setOnItemSelectedListener(this); //register a callback to be invoked when an item in the AdapterView has been selected
        }
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.car_make_array, android.R.layout.simple_spinner_item); //create a new ArrayAdapter
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (spinner != null) {
            spinner.setAdapter(arrayAdapter);
        }

        //get intent that started the activity, retrieve extended data from the intent
        Intent intent = getIntent();
        timerOn = intent.getBooleanExtra(MainActivity.TIMER_STATUS, false);
        if (intent.getStringArrayListExtra(VIEWED_IMAGES) != null) {
            viewedImagesList = intent.getStringArrayListExtra(VIEWED_IMAGES);
        }

        //navigate to a screen with level over message when the game level is over
        if(viewedImagesList.size() >= MainActivity.AVAILABLE_IMAGES) {
            finish();
            Intent gameOverIntent = new Intent(IdentifyCarMakeActivity.this, LevelOverActivity.class);
            startActivity(gameOverIntent);
        }

        //restore the preserved state of the IdentifyCarMakeActivity
        if (savedInstanceState != null) {
            timeRemaining = savedInstanceState.getLong("remaining_time");
            randomImageDetails = savedInstanceState.getStringArrayList("random_image_details");
            if (randomImageDetails.size() == 2) {
                carImageView.setImageResource(getResources().getIdentifier(randomImageDetails.get(0), "drawable", "com.project.guessthecar"));
                carMake = randomImageDetails.get(1);
            }
            //get the previously selected item from the spinner and set it as the selected item in the spinner
            selectedCarMake = spinner.getItemAtPosition(savedInstanceState.getInt("spinner_selection")).toString();
            spinner.setSelection(savedInstanceState.getInt("spinner_selection"));
            resultText.setText(savedInstanceState.getString("result"));
            if (!resultText.getText().toString().isEmpty()) {
                if (resultText.getText().toString().equals(carMake)) {
                    resultText.setTextColor(Color.GREEN);
                } else {
                    resultText.setTextColor(Color.RED);
                }
            }
            correctAnswer.setText(savedInstanceState.getString("correct_answer"));
            if (!correctAnswer.getText().toString().isEmpty()) {
                correctAnswer.setTextColor(Color.YELLOW);
                correctAnswer.setBackgroundColor(Color.BLACK);
            }
            identifyBtn.setText(savedInstanceState.getString("button_text"));
            viewedImagesList = savedInstanceState.getStringArrayList("viewed_images");
        } else {
            Controller controller = (Controller) getApplicationContext();
            randomImageDetails = controller.getRandomCarImage();
            while (viewedImagesList.contains(randomImageDetails.get(0))) { //get a unique random car image from the images available in the application
                randomImageDetails = controller.getRandomCarImage();
            }
            if (randomImageDetails.size() == 2) {
                String carImage = randomImageDetails.get(0);
                int carImageId = getResources().getIdentifier(carImage, "drawable", "com.project.guessthecar");
                carImageView.setImageResource(carImageId);
                viewedImagesList.add(carImage);
                carMake = randomImageDetails.get(1);
            }
            timeRemaining = 20000; //set remaining time of the timer as 20s
        }

        if (timerOn) { //start a countdown timer according to the respective remaining time
            timer = new CountDownTimer(timeRemaining, 1000) {
                public void onTick(long millisUntilFinished) {
                    countDown.setText(String.format(Locale.getDefault(),"%s%d%s", getString(R.string.time_remaining_text), millisUntilFinished / 1000, "s"));
                    timeRemaining = millisUntilFinished;
                }
                public void onFinish() {
                    countDown.setText(R.string.time_over_text);
                    validateIdentifyCarMakeAnswer(identifyBtn); //invoke validateIdentifyCarMakeAnswer method when the time's up
                }
            }.start();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save the UI state of the IdentifyCarMakeActivity
        super.onSaveInstanceState(outState);
        outState.putLong("remaining_time", timeRemaining);
        outState.putStringArrayList("random_image_details", (ArrayList<String>) randomImageDetails);
        outState.putInt("spinner_selection", spinner.getSelectedItemPosition());
        outState.putString("result", resultText.getText().toString());
        outState.putString("correct_answer", correctAnswer.getText().toString());
        outState.putString("button_text", identifyBtn.getText().toString());
        outState.putStringArrayList("viewed_images", (ArrayList<String>) viewedImagesList);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCarMake = parent.getItemAtPosition(position).toString(); //get the selected item from the spinner
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void validateIdentifyCarMakeAnswer(View view) { //validate the answer of the user & provide the chance to navigate to a new game screen
        String buttonText = identifyBtn.getText().toString();
        if (buttonText.equals(getString(R.string.identify_btn_text))) { //execute if the user want to submit the answer
            if (selectedCarMake != null) {
                //check whether the provided answer is correct & show respective result to the user
                if (selectedCarMake.equals(carMake)) {
                    resultText.setText(R.string.correct_result_message);
                    resultText.setTextColor(Color.GREEN);
                } else {
                    resultText.setText(R.string.wrong_result_message);
                    resultText.setTextColor(Color.RED);
                    correctAnswer.setText(carMake);
                    correctAnswer.setTextColor(Color.YELLOW);
                    correctAnswer.setBackgroundColor(Color.BLACK);
                }
                if (timerOn && timeRemaining > 1000) { //cancel the timer if user submit the answer before the timer end
                    timer.cancel();
                    timeRemaining = 0;
                    countDown.setText("");
                }
            }
            identifyBtn.setText(R.string.next_btn_text);
        } else if (buttonText.equals(getString(R.string.next_btn_text))){ //execute if user want to navigate to a new game screen
            finish();
            Intent intent = new Intent(IdentifyCarMakeActivity.this, IdentifyCarMakeActivity.class);
            intent.putExtra(MainActivity.TIMER_STATUS, timerOn);
            intent.putStringArrayListExtra(VIEWED_IMAGES, (ArrayList<String>) viewedImagesList);
            startActivity(intent);
        }
    }

}