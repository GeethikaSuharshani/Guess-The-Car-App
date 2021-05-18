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

public class HintsActivity extends AppCompatActivity {
    public static final String VIEWED_IMAGES = "com.project.guessthecar.extra.VIEWED_IMAGES";
    private List<String> viewedImagesList = new ArrayList<>();
    private CountDownTimer timer;
    private boolean timerOn;
    private TextView countDown;
    private long timeRemaining;
    private ImageView carImageView;
    private List<String> randomImageDetails = new ArrayList<>();
    private String carMake;
    private TextView guessResultUpdateText;
    private EditText guessInputText;
    private int incorrectGuesses;
    private TextView resultText;
    private TextView correctAnswer;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hints);

        countDown = findViewById(R.id.hints_count_down);
        carImageView = findViewById(R.id.hints_image);
        guessResultUpdateText = findViewById(R.id.guess_result_update_text);
        guessInputText = findViewById(R.id.user_guess_input);
        resultText = findViewById(R.id.hints_result);
        correctAnswer = findViewById(R.id.hints_correct_answer);
        submitBtn = findViewById(R.id.submit_btn);

        //get intent that started the activity, retrieve extended data from the intent
        Intent intent = getIntent();
        timerOn = intent.getBooleanExtra(MainActivity.TIMER_STATUS, false);
        if (intent.getStringArrayListExtra(VIEWED_IMAGES) != null) {
            viewedImagesList = intent.getStringArrayListExtra(VIEWED_IMAGES);
        }

        //navigate to a screen with level over message when the game level is over
        if(viewedImagesList.size() >= MainActivity.AVAILABLE_IMAGES) {
            finish();
            Intent gameOverIntent = new Intent(HintsActivity.this, LevelOverActivity.class);
            startActivity(gameOverIntent);
        }

        //restore the preserved state of the HintsActivity
        if (savedInstanceState != null) {
            timeRemaining = savedInstanceState.getLong("remaining_time");
            randomImageDetails = savedInstanceState.getStringArrayList("random_image_details");
            if (randomImageDetails.size() == 2) {
                carImageView.setImageResource(getResources().getIdentifier(randomImageDetails.get(0), "drawable", "com.project.guessthecar"));
                carMake = randomImageDetails.get(1);
            }
            guessResultUpdateText.setText(savedInstanceState.getString("result_update_text"));
            guessInputText.setText(savedInstanceState.getString("guess_input_text"));
            incorrectGuesses = savedInstanceState.getInt("incorrect_guesses");
            resultText.setText(savedInstanceState.getString("result"));
            if (!resultText.getText().toString().isEmpty()) {
                if (resultText.getText().toString().equals(carMake)) {
                    resultText.setTextColor(Color.GREEN);
                } else {
                    resultText.setTextColor(Color.RED);
                }
                timeRemaining = 0;
            }
            correctAnswer.setText(savedInstanceState.getString("correct_answer"));
            if (!correctAnswer.getText().toString().isEmpty()) {
                correctAnswer.setTextColor(Color.YELLOW);
                correctAnswer.setBackgroundColor(Color.BLACK);
            }
            submitBtn.setText(savedInstanceState.getString("button_text"));
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
            if (carMake != null) {
                String guessTextWithDashes = "";
                for (int i=0; i<carMake.length(); i++) { //create a string that holds dashes equals to no of letters the car make contain
                    guessTextWithDashes = guessTextWithDashes.concat("-");
                }
                guessResultUpdateText.setText(guessTextWithDashes);
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
                    validateHintsAnswer(submitBtn); //invoke validateHintsAnswer method when the time's up
                }
            }.start();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save the UI state of the HintsActivity
        super.onSaveInstanceState(outState);
        outState.putLong("remaining_time", timeRemaining);
        outState.putStringArrayList("random_image_details", (ArrayList<String>) randomImageDetails);
        outState.putString("result_update_text", guessResultUpdateText.getText().toString());
        outState.putString("guess_input_text", guessInputText.getText().toString());
        outState.putInt("incorrect_guesses", incorrectGuesses);
        outState.putString("result", resultText.getText().toString());
        outState.putString("correct_answer", correctAnswer.getText().toString());
        outState.putString("button_text", submitBtn.getText().toString());
        outState.putStringArrayList("viewed_images", (ArrayList<String>) viewedImagesList);
    }

    public void validateHintsAnswer(View view) { //validate the answer of the user & provide the chance to navigate to a new game screen
        String buttonText = submitBtn.getText().toString();
        String guessResultText = guessResultUpdateText.getText().toString();

        if(buttonText.equals(getString(R.string.next_btn_text))) { //execute if user want to navigate to a new game screen
            finish();
            Intent intent = new Intent(HintsActivity.this, HintsActivity.class);
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
                String userGuessInput = guessInputText.getText().toString().trim().toUpperCase();
                if (carMake != null) {
                    String[] carMakeCharacters = carMake.toUpperCase().split("");
                    if (!userGuessInput.equals("")) {
                        //check whether the car make contain the character user provided & if contain replace the corresponding dashes with the character
                        for (int i = 0; i < carMakeCharacters.length; i++) {
                            if (carMakeCharacters[i].equals(userGuessInput)) {
                                guessResultText = guessResultText.substring(0, i) + userGuessInput + guessResultText.substring(i + 1);
                            }
                        }
                    }
                    if (guessResultText.equals(guessResultUpdateText.getText().toString())) { //execute if user has provided an incorrect guess
                        incorrectGuesses++;
                        Toast.makeText(HintsActivity.this, String.format(getString(R.string.incorrect_guess_message), incorrectGuesses), Toast.LENGTH_SHORT).show();
                    }
                    guessResultUpdateText.setText(guessResultText);
                    guessInputText.getText().clear();
                    if (carMake.toUpperCase().equals(guessResultText)) { //execute if the user has correctly guess the car make
                        resultText.setText(R.string.correct_result_message);
                        resultText.setTextColor(Color.GREEN);
                        submitBtn.setText(R.string.next_btn_text);
                    } else {
                        if (timerOn && incorrectGuesses < 3) { //start a timer again for next attempt if user has made less than 3 incorrect guesses
                            timeRemaining = 20000;
                            timer = new CountDownTimer(timeRemaining, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    countDown.setText(String.format(Locale.getDefault(),"%s%d%s", getString(R.string.time_remaining_text), millisUntilFinished / 1000, "s"));
                                    timeRemaining = millisUntilFinished;
                                }
                                public void onFinish() {
                                    countDown.setText(R.string.time_over_text);
                                    validateHintsAnswer(submitBtn);
                                }
                            }.start();
                        }
                    }
                }
            }
            if (incorrectGuesses >= 3) { //execute if user has made 3 incorrect guesses
                if (carMake != null) {
                    //check whether the provided answer is correct & show respective result to the user
                    if (carMake.toUpperCase().equals(guessResultText)) {
                        resultText.setText(R.string.correct_result_message);
                        resultText.setTextColor(Color.GREEN);
                    } else {
                        resultText.setText(R.string.wrong_result_message);
                        resultText.setTextColor(Color.RED);
                        correctAnswer.setText(carMake);
                        correctAnswer.setTextColor(Color.YELLOW);
                        correctAnswer.setBackgroundColor(Color.BLACK);
                    }
                }
                submitBtn.setText(R.string.next_btn_text);
            }
        }
    }

}