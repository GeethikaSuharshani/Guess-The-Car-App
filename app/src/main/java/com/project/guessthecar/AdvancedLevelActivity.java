package com.project.guessthecar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdvancedLevelActivity extends AppCompatActivity {
    public static final String VIEWED_IMAGES = "com.project.guessthecar.extra.VIEWED_IMAGES";
    private List<String> viewedImagesList = new ArrayList<>();
    private TextView scoreText;
    private int userScore;
    private CountDownTimer timer;
    private boolean timerOn;
    private TextView countDown;
    private long timeRemaining;
    private ImageView carImageView1;
    private ImageView carImageView2;
    private ImageView carImageView3;
    private List<String> randomImage1Details = new ArrayList<>();
    private List<String> randomImage2Details = new ArrayList<>();
    private List<String> randomImage3Details = new ArrayList<>();
    private String carMake1;
    private String carMake2;
    private String carMake3;
    private EditText guess1InputText;
    private EditText guess2InputText;
    private EditText guess3InputText;
    private TextView correctCarMake1Text;
    private TextView correctCarMake2Text;
    private TextView correctCarMake3Text;
    private TextView guessResult;
    private int incorrectGuesses;
    private Button submitBtn;
    boolean answer1Correct = false;
    boolean answer2Correct = false;
    boolean answer3Correct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_level);

        scoreText = findViewById(R.id.advanced_level_score);
        countDown = findViewById(R.id.advanced_level_count_down);
        carImageView1 = findViewById(R.id.advanced_level_image1);
        carImageView2 = findViewById(R.id.advanced_level_image2);
        carImageView3 = findViewById(R.id.advanced_level_image3);
        guess1InputText = findViewById(R.id.advanced_level_image1_guess_input);
        guess2InputText = findViewById(R.id.advanced_level_image2_guess_input);
        guess3InputText = findViewById(R.id.advanced_level_image3_guess_input);
        correctCarMake1Text = findViewById(R.id.advanced_level_correct_car_make1);
        correctCarMake2Text = findViewById(R.id.advanced_level__correct_car_make2);
        correctCarMake3Text = findViewById(R.id.advanced_level__correct_car_make3);
        guessResult = findViewById(R.id.advanced_level_guess_result);
        submitBtn = findViewById(R.id.advanced_level_submit_btn);

        //get intent that started the activity, retrieve extended data from the intent
        Intent intent = getIntent();
        timerOn = intent.getBooleanExtra(MainActivity.TIMER_STATUS, false);
        if (intent.getStringArrayListExtra(VIEWED_IMAGES) != null) {
            viewedImagesList = intent.getStringArrayListExtra(VIEWED_IMAGES);
        }

        //navigate to a screen with level over message when the game level is over
        if(viewedImagesList.size() >= MainActivity.AVAILABLE_IMAGES) {
            finish();
            Intent gameOverIntent = new Intent(AdvancedLevelActivity.this, LevelOverActivity.class);
            startActivity(gameOverIntent);
        }

        //restore the preserved state of the AdvancedLevelActivity
        if (savedInstanceState != null) {
            userScore = savedInstanceState.getInt("user_score");
            scoreText.setText(String.format("Score : %s",userScore));
            timeRemaining = savedInstanceState.getLong("remaining_time");
            //get preserved data of random images
            randomImage1Details = savedInstanceState.getStringArrayList("random_image1_details");
            if (randomImage1Details.size() == 2) {
                carImageView1.setImageResource(getResources().getIdentifier(randomImage1Details.get(0), "drawable", "com.project.guessthecar"));
                carMake1 = randomImage1Details.get(1);
            }
            randomImage2Details = savedInstanceState.getStringArrayList("random_image2_details");
            if (randomImage2Details.size() == 2) {
                carImageView2.setImageResource(getResources().getIdentifier(randomImage2Details.get(0), "drawable", "com.project.guessthecar"));
                carMake2 = randomImage2Details.get(1);
            }
            randomImage3Details = savedInstanceState.getStringArrayList("random_image3_details");
            if (randomImage3Details.size() == 2) {
                carImageView3.setImageResource(getResources().getIdentifier(randomImage3Details.get(0), "drawable", "com.project.guessthecar"));
                carMake3 = randomImage3Details.get(1);
            }
            guess1InputText.setText(savedInstanceState.getString("guess1_input_text"));
            if (!guess1InputText.getText().toString().isEmpty()) {
                if (guess1InputText.getText().toString().toUpperCase().equals(carMake1.toUpperCase())) {
                    guess1InputText.setTextColor(Color.GREEN);
                    guess1InputText.setEnabled(false);
                    answer1Correct = true;
                } else {
                    guess1InputText.setTextColor(Color.RED);
                }
            }
            guess2InputText.setText(savedInstanceState.getString("guess2_input_text"));
            if (!guess2InputText.getText().toString().isEmpty()) {
                if (guess2InputText.getText().toString().toUpperCase().equals(carMake2.toUpperCase())) {
                    guess2InputText.setTextColor(Color.GREEN);
                    guess2InputText.setEnabled(false);
                    answer2Correct = true;
                } else {
                    guess2InputText.setTextColor(Color.RED);
                }
            }
            guess3InputText.setText(savedInstanceState.getString("guess3_input_text"));
            if (!guess3InputText.getText().toString().isEmpty()) {
                if (guess3InputText.getText().toString().toUpperCase().equals(carMake3.toUpperCase())) {
                    guess3InputText.setTextColor(Color.GREEN);
                    guess3InputText.setEnabled(false);
                    answer3Correct = true;
                } else {
                    guess3InputText.setTextColor(Color.RED);
                }
            }
            correctCarMake1Text.setText(savedInstanceState.getString("correct_car_make1"));
            if (!correctCarMake1Text.getText().toString().isEmpty()) {
                correctCarMake1Text.setTextColor(Color.YELLOW);
                correctCarMake1Text.setBackgroundColor(Color.BLACK);
            }
            correctCarMake2Text.setText(savedInstanceState.getString("correct_car_make2"));
            if (!correctCarMake2Text.getText().toString().isEmpty()) {
                correctCarMake2Text.setTextColor(Color.YELLOW);
                correctCarMake2Text.setBackgroundColor(Color.BLACK);
            }
            correctCarMake3Text.setText(savedInstanceState.getString("correct_car_make3"));
            if (!correctCarMake3Text.getText().toString().isEmpty()) {
                correctCarMake3Text.setTextColor(Color.YELLOW);
                correctCarMake3Text.setBackgroundColor(Color.BLACK);
            }
            guessResult.setText(savedInstanceState.getString("guess_result"));
            if (!guessResult.getText().toString().isEmpty()) {
                if (guessResult.getText().toString().equals(getString(R.string.correct_result_message))) {
                    guessResult.setTextColor(Color.GREEN);
                } else {
                    guessResult.setTextColor(Color.RED);
                }
            }
            incorrectGuesses = savedInstanceState.getInt("incorrect_guesses");
            submitBtn.setText(savedInstanceState.getString("button_text"));
            viewedImagesList = savedInstanceState.getStringArrayList("viewed_images");
        } else {
            Controller controller = (Controller) getApplicationContext();
            randomImage1Details = controller.getRandomCarImage();
            while (viewedImagesList.contains(randomImage1Details.get(0))) { //get a unique random car image from the images available in the application
                randomImage1Details = controller.getRandomCarImage();
            }
            if (randomImage1Details.size() == 2) {
                String carImage1 = randomImage1Details.get(0);
                int carImageId1 = getResources().getIdentifier(carImage1, "drawable", "com.project.guessthecar");
                carImageView1.setImageResource(carImageId1);
                viewedImagesList.add(carImage1);
                carMake1 = randomImage1Details.get(1);
            }

            randomImage2Details = controller.getRandomCarImage();
            while (viewedImagesList.contains(randomImage2Details.get(0))) { //get a unique random car image from the images available in the application
                randomImage2Details = controller.getRandomCarImage();
            }
            if (randomImage2Details.size() == 2) {
                String carImage2 = randomImage2Details.get(0);
                int carImageId2 = getResources().getIdentifier(carImage2, "drawable", "com.project.guessthecar");
                carImageView2.setImageResource(carImageId2);
                viewedImagesList.add(carImage2);
                carMake2 = randomImage2Details.get(1);
            }

            randomImage3Details = controller.getRandomCarImage();
            while (viewedImagesList.contains(randomImage3Details.get(0))) { //get a unique random car image from the images available in the application
                randomImage3Details = controller.getRandomCarImage();
            }
            if (randomImage3Details.size() == 2) {
                String carImage3 = randomImage3Details.get(0);
                int carImageId3 = getResources().getIdentifier(carImage3, "drawable", "com.project.guessthecar");
                carImageView3.setImageResource(carImageId3);
                viewedImagesList.add(carImage3);
                carMake3 = randomImage3Details.get(1);
            }
            scoreText.setText(String.format("Score : %s",userScore)); //set a text to view user score
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
                    validateAdvancedLevelAnswer(submitBtn); //invoke validateAdvancedLevelAnswer method when the time's up
                }
            }.start();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save the UI state of the AdvancedLevelActivity
        super.onSaveInstanceState(outState);
        outState.putInt("user_score", userScore);
        outState.putLong("remaining_time", timeRemaining);
        outState.putStringArrayList("random_image1_details", (ArrayList<String>) randomImage1Details);
        outState.putStringArrayList("random_image2_details", (ArrayList<String>) randomImage2Details);
        outState.putStringArrayList("random_image3_details", (ArrayList<String>) randomImage3Details);
        outState.putString("guess1_input_text", guess1InputText.getText().toString());
        outState.putString("guess2_input_text", guess2InputText.getText().toString());
        outState.putString("guess3_input_text", guess3InputText.getText().toString());
        outState.putString("correct_car_make1", correctCarMake1Text.getText().toString());
        outState.putString("correct_car_make2", correctCarMake2Text.getText().toString());
        outState.putString("correct_car_make3", correctCarMake3Text.getText().toString());
        outState.putString("guess_result", guessResult.getText().toString());
        outState.putInt("incorrect_guesses", incorrectGuesses);
        outState.putString("button_text", submitBtn.getText().toString());
        outState.putStringArrayList("viewed_images", (ArrayList<String>) viewedImagesList);
    }

    public void validateAdvancedLevelAnswer(View view) { //validate the answer of the user & provide the chance to navigate to a new game screen
        String buttonText = submitBtn.getText().toString();

        if(buttonText.equals(getString(R.string.next_btn_text))) { //execute if user want to navigate to a new game screen
            finish();
            Intent intent = new Intent(AdvancedLevelActivity.this, AdvancedLevelActivity.class);
            intent.putExtra(MainActivity.TIMER_STATUS, timerOn);
            intent.putStringArrayListExtra(VIEWED_IMAGES, (ArrayList<String>) viewedImagesList);
            startActivity(intent);
        } else if(buttonText.equals(getString(R.string.submit_btn_text))) {
            if (timerOn && timeRemaining > 1000) { //cancel the timer if user submit the answer before the timer end
                timer.cancel();
                timeRemaining = 0;
                countDown.setText("");
            }
            if (incorrectGuesses < 3) { //execute if user has made less than 3 incorrect guesses
                //check whether the provided answer is the same as the car make of the car & show correct answers in green while wrong answers marked in red
                if (guess1InputText.getText().toString().trim().toUpperCase().equals(carMake1.toUpperCase())) {
                    guess1InputText.setTextColor(Color.GREEN);
                    guess1InputText.setEnabled(false);
                    answer1Correct = true;
                } else {
                    guess1InputText.setTextColor(Color.RED);
                }
                if (guess2InputText.getText().toString().trim().toUpperCase().equals(carMake2.toUpperCase())) {
                    guess2InputText.setTextColor(Color.GREEN);
                    guess2InputText.setEnabled(false);
                    answer2Correct = true;
                } else {
                    guess2InputText.setTextColor(Color.RED);
                }
                if (guess3InputText.getText().toString().trim().toUpperCase().equals(carMake3.toUpperCase())) {
                    guess3InputText.setTextColor(Color.GREEN);
                    guess3InputText.setEnabled(false);
                    answer3Correct = true;
                } else {
                    guess3InputText.setTextColor(Color.RED);
                }

                if (answer1Correct && answer2Correct && answer3Correct) { //execute if all three answers are correct
                    userScore += 3; //give user 3 points for three correct answers
                    scoreText.setText(String.format("Score : %s",userScore));
                    guessResult.setText(R.string.correct_result_message);
                    guessResult.setTextColor(Color.GREEN);
                    submitBtn.setText(R.string.next_btn_text);
                } else { //execute if any of the provided answer is incorrect
                    incorrectGuesses++;
                    Toast.makeText(AdvancedLevelActivity.this, String.format(getString(R.string.incorrect_guess_message), incorrectGuesses), Toast.LENGTH_SHORT).show();
                    if (timerOn && incorrectGuesses < 3) { //start a timer again for next attempt if user has made less than 3 incorrect guesses
                        timeRemaining = 20000;
                        timer = new CountDownTimer(timeRemaining, 1000) {
                            public void onTick(long millisUntilFinished) {
                                countDown.setText(String.format(Locale.getDefault(),"%s%d%s", getString(R.string.time_remaining_text), millisUntilFinished / 1000, "s"));
                                timeRemaining = millisUntilFinished;
                            }
                            public void onFinish() {
                                countDown.setText(R.string.time_over_text);
                                validateAdvancedLevelAnswer(submitBtn);
                            }
                        }.start();
                    }
                }
            }
            if (incorrectGuesses >= 3) { //execute if user has made 3 incorrect guesses
                guessResult.setText(R.string.wrong_result_message);
                guessResult.setTextColor(Color.RED);
                //mark correct answers in green & wrong answers in red while giving 1 point for each correct answer
                if (!answer1Correct) {
                    correctCarMake1Text.setText(carMake1);
                    correctCarMake1Text.setTextColor(Color.YELLOW);
                    correctCarMake1Text.setBackgroundColor(Color.BLACK);
                } else {
                    userScore++;
                }
                if (!answer2Correct) {
                    correctCarMake2Text.setText(carMake2);
                    correctCarMake2Text.setTextColor(Color.YELLOW);
                    correctCarMake2Text.setBackgroundColor(Color.BLACK);
                } else {
                    userScore++;
                }
                if (!answer3Correct) {
                    correctCarMake3Text.setText(carMake3);
                    correctCarMake3Text.setTextColor(Color.YELLOW);
                    correctCarMake3Text.setBackgroundColor(Color.BLACK);
                } else {
                    userScore++;
                }
                scoreText.setText(String.format("Score : %s",userScore));
                submitBtn.setText(R.string.next_btn_text);
            }
        }
    }

}