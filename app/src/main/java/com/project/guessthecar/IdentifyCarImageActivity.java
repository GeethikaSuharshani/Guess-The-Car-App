package com.project.guessthecar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class IdentifyCarImageActivity extends AppCompatActivity {
    public static final String VIEWED_IMAGES = "com.project.guessthecar.extra.VIEWED_IMAGES";
    private List<String> viewedImagesList = new ArrayList<>();
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
    private List<String> carMakes = new ArrayList<>();
    private TextView carMakeText;
    private TextView resultText;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_image);

        countDown = findViewById(R.id.identify_car_image_count_down);
        carImageView1 = findViewById(R.id.identify_car_image1);
        carImageView2 = findViewById(R.id.identify_car_image2);
        carImageView3 = findViewById(R.id.identify_car_image3);
        carMakeText = findViewById(R.id.car_make_text);
        resultText = findViewById(R.id.identify_car_image_result);
        nextBtn = findViewById(R.id.next_btn);

        //get intent that started the activity, retrieve extended data from the intent
        Intent intent = getIntent();
        timerOn = intent.getBooleanExtra(MainActivity.TIMER_STATUS, false);
        if (intent.getStringArrayListExtra(VIEWED_IMAGES) != null) {
            viewedImagesList = intent.getStringArrayListExtra(VIEWED_IMAGES);
        }

        //navigate to a screen with level over message when the game level is over
        if(viewedImagesList.size() >= MainActivity.AVAILABLE_IMAGES) {
            finish();
            Intent gameOverIntent = new Intent(IdentifyCarImageActivity.this, LevelOverActivity.class);
            startActivity(gameOverIntent);
        }

        //restore the preserved state of the IdentifyCarImageActivity
        if (savedInstanceState != null) {
            timeRemaining = savedInstanceState.getLong("remaining_time");
            randomImage1Details = savedInstanceState.getStringArrayList("random_image1_details");
            if (randomImage1Details.size() == 2) {
                carImageView1.setImageResource(getResources().getIdentifier(randomImage1Details.get(0), "drawable", "com.project.guessthecar"));
                carMake1 = randomImage1Details.get(1);
                carMakes.add(carMake1);
            }
            randomImage2Details = savedInstanceState.getStringArrayList("random_image2_details");
            if (randomImage2Details.size() == 2) {
                carImageView2.setImageResource(getResources().getIdentifier(randomImage2Details.get(0), "drawable", "com.project.guessthecar"));
                carMake2 = randomImage2Details.get(1);
                carMakes.add(carMake2);
            }
            randomImage3Details = savedInstanceState.getStringArrayList("random_image3_details");
            if (randomImage3Details.size() == 2) {
                carImageView3.setImageResource(getResources().getIdentifier(randomImage3Details.get(0), "drawable", "com.project.guessthecar"));
                carMake3 = randomImage3Details.get(1);
                carMakes.add(carMake3);
            }
            carMakeText.setText(savedInstanceState.getString("correct_car_make"));
            resultText.setText(savedInstanceState.getString("result"));
            if (!resultText.getText().toString().isEmpty()) {
                if (resultText.getText().toString().equals(carMakeText.getText().toString())) {
                    resultText.setTextColor(Color.GREEN);
                } else {
                    resultText.setTextColor(Color.RED);
                }
            }
            viewedImagesList = savedInstanceState.getStringArrayList("viewed_images");
        } else {
            Controller controller = (Controller) getApplicationContext();
            randomImage1Details = controller.getRandomCarImage();
            while (carMakes.contains(randomImage1Details.get(1))) { //get a unique random car image in terms of car make, from the images available in the application
                randomImage1Details = controller.getRandomCarImage();
            }
            if (randomImage1Details.size() == 2) {
                String carImage1 = randomImage1Details.get(0);
                int carImageId1 = getResources().getIdentifier(carImage1, "drawable", "com.project.guessthecar");
                carImageView1.setImageResource(carImageId1);
                viewedImagesList.add(carImage1);
                carMake1 = randomImage1Details.get(1);
                carMakes.add(carMake1);
            }

            randomImage2Details = controller.getRandomCarImage();
            while (carMakes.contains(randomImage2Details.get(1))) { //get a unique random car image in terms of car make, from the images available in the application
                randomImage2Details = controller.getRandomCarImage();
            }
            if (randomImage2Details.size() == 2) {
                String carImage2 = randomImage2Details.get(0);
                int carImageId2 = getResources().getIdentifier(carImage2, "drawable", "com.project.guessthecar");
                carImageView2.setImageResource(carImageId2);
                viewedImagesList.add(carImage2);
                carMake2 = randomImage2Details.get(1);
                carMakes.add(carMake2);
            }

            randomImage3Details = controller.getRandomCarImage();
            while (carMakes.contains(randomImage3Details.get(1))) { //get a unique random car image in terms of car make, from the images available in the application
                randomImage3Details = controller.getRandomCarImage();
            }
            if (randomImage3Details.size() == 2) {
                String carImage3 = randomImage3Details.get(0);
                int carImageId3 = getResources().getIdentifier(carImage3, "drawable", "com.project.guessthecar");
                carImageView3.setImageResource(carImageId3);
                viewedImagesList.add(carImage3);
                carMake3 = randomImage3Details.get(1);
                carMakes.add(carMake3);
            }

            Random randomNo = new Random();
            int carMakeIndex;
            if (!carMakes.isEmpty()) {
                carMakeIndex = randomNo.nextInt(carMakes.size()); //select a random car make from the car makes of the three selected images
                carMakeText.setText(carMakes.get(carMakeIndex));
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
                    validateIdentifyCarImageAnswer(nextBtn); //invoke validateIdentifyCarImageAnswer method when the time's up
                }
            }.start();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save the UI state of the IdentifyCarImageActivity
        super.onSaveInstanceState(outState);
        outState.putLong("remaining_time", timeRemaining);
        outState.putStringArrayList("random_image1_details", (ArrayList<String>) randomImage1Details);
        outState.putStringArrayList("random_image2_details", (ArrayList<String>) randomImage2Details);
        outState.putStringArrayList("random_image3_details", (ArrayList<String>) randomImage3Details);
        outState.putString("correct_car_make", carMakeText.getText().toString());
        outState.putString("result", resultText.getText().toString());
        outState.putStringArrayList("viewed_images", (ArrayList<String>) viewedImagesList);
    }

    public void validateIdentifyCarImageAnswer(View view) { //validate the answer of the user
        String correctCarMake = carMakeText.getText().toString();
        int correctImageId = 0;
        if (correctCarMake.equals(carMake1)) { //find the car image that matches with the viewed car make
            correctImageId = R.id.identify_car_image1;
        } else if(correctCarMake.equals(carMake2)) {
            correctImageId = R.id.identify_car_image2;
        } else if (correctCarMake.equals(carMake3)) {
            correctImageId = R.id.identify_car_image3;
        }
        if (timerOn && timeRemaining > 1000) { //cancel the timer if user submit the answer before the timer end
            timer.cancel();
            timeRemaining = 0;
            countDown.setText("");
        }
        //check whether the provided answer is correct & show respective result to the user
        if (view.getId() == correctImageId) {
            resultText.setText(R.string.correct_result_message);
            resultText.setTextColor(Color.GREEN);
        } else {
            resultText.setText(R.string.wrong_result_message);
            resultText.setTextColor(Color.RED);
        }
        //disable the clickable images after user clicked on an image once
        carImageView1.setEnabled(false);
        carImageView2.setEnabled(false);
        carImageView3.setEnabled(false);
    }

    public void navigateToNextQuestion(View view) { //execute if user want to navigate to a new game screen
        finish();
        Intent intent = new Intent(IdentifyCarImageActivity.this, IdentifyCarImageActivity.class);
        intent.putExtra(MainActivity.TIMER_STATUS, timerOn);
        intent.putStringArrayListExtra(VIEWED_IMAGES, (ArrayList<String>) viewedImagesList);
        startActivity(intent);
    }

}