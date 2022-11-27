package com.example.scoutingapp2022_pie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
//import android.app.Activity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    private long pauseOffset;
    private boolean running;

    ImageButton autonButton, teleopButton, endgameButton, qrCodeButton, upperAutonShotsIncrease, upperAutonShotsDecrease, lowerAutonShotsIncrease, lowerAutonShotsDecrease, upperTeleopShotsIncrease, upperTeleopShotsDecrease, lowerTeleopShotsIncrease, lowerTeleopShotsDecrease, tarmacNumberIncrease, tarmacNumberDecrease,
            increaseMatchNum, decreaseMatchNum, timerStart, timerStop, timerReset, upperAutonMissedIncrease, upperAutonMissedDecrease, lowerAutonMissedIncrease, lowerAutonMissedDecrease, upperTeleopMissedIncrease, upperTeleopMissedDecrease, lowerTeleopMissedIncrease, lowerTeleopMissedDecrease;

    Button generateQr, nextMatch;

    EditText upperAutonEditTextIN, lowerAutonEditTextIN, upperTeleopEditTextIN, lowerTeleopEditTextIN, upperAutonEditTextOUT, lowerAutonEditTextOUT, upperTeleopEditTextOUT, lowerTeleopEditTextOut, tarmacEditText, teamNumEditText, matchNumEditText, comments; //creating the editText
    TextView upperTextView, lowerTextView, loadsFromTextView, tarmacNumberTextView, commentsTxt, climberTimeText; //creating the textViews

    Chronometer timerChronometer;

    CheckBox groundCheckBox, hpCheckBox, hpShotCheckBox, leftTarmacCheckBox; //creating the CheckBox Code
    String upperShotsAuton, upperMissedAuton, lowerMissedAuton, lowerShotsAuton, upperShotsTeleOP, upperMissedTeleOP, lowerMissedTeleOP, lowerShotsTeleOP, tarmacNumber,  teamNumber, matchNumber, climbAttempted, climbTime;  //strings to put into SharedPreferences

    Boolean humanShot, leftTarmac, hpLoads, groundLoads; //auton tasks completed

    ImageView qrCodeImage, fieldImage; //imageView for qr code

    RadioGroup climb;

    RadioButton traversal, high, mid, low, none;

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //instantiating the imagebuttons
        autonButton = findViewById(R.id.autonButton);
        autonButton.setImageResource(R.drawable.auton_1);
        teleopButton = findViewById(R.id.teleOPButton);
        endgameButton = findViewById(R.id.endgameButton);
        qrCodeButton = findViewById(R.id.qrCodeButton);


        timerStart = findViewById(R.id.startTimer);
        timerStop = findViewById(R.id.stopTimer);
        timerReset = findViewById(R.id.resetTimer);
        climberTimeText = findViewById(R.id.climberTimeText);
        timerChronometer = findViewById(R.id.timerChronometer);


        upperAutonShotsIncrease = findViewById(R.id.upperAutonPlus);
        upperAutonShotsDecrease = findViewById(R.id.upperAutonPlusDel);
        lowerAutonShotsIncrease = findViewById(R.id.lowerAutonPlus);
        lowerAutonShotsDecrease = findViewById(R.id.lowerAutonPlusDel);

        upperTeleopShotsIncrease = findViewById(R.id.upperTeleopPlus);
        upperTeleopShotsDecrease = findViewById(R.id.upperTeleopPlusDel);
        lowerTeleopShotsIncrease = findViewById(R.id.lowerTeleopPlus);
        lowerTeleopShotsDecrease = findViewById(R.id.lowerTeleopPlusDel);


        upperAutonMissedIncrease = findViewById(R.id.upperAutonX);
        upperAutonMissedDecrease = findViewById(R.id.upperAutonXDel);
        lowerAutonMissedIncrease = findViewById(R.id.lowerAutonX);
        lowerAutonMissedDecrease = findViewById(R.id.lowerAutonXDel);

        upperTeleopMissedIncrease = findViewById(R.id.upperTeleopX);
        upperTeleopMissedDecrease = findViewById(R.id.upperTeleopXDel);
        lowerTeleopMissedIncrease = findViewById(R.id.lowerTeleopX);
        lowerTeleopMissedDecrease = findViewById(R.id.lowerTeleopXDel);


        tarmacNumberIncrease = findViewById(R.id.tarmacNumberIncrease);
        tarmacNumberDecrease = findViewById(R.id.tarmacNumberDecrease);
        increaseMatchNum = findViewById(R.id.increaseMatchNumber);
        decreaseMatchNum = findViewById(R.id.decreaseMatchNumber);

        //instantiating sharedPreferences
        sp = getSharedPreferences("TeamData", MODE_PRIVATE);

        //instantiating editText
        upperAutonEditTextIN = findViewById(R.id.upperAutonEditText_IN);
        lowerAutonEditTextIN = findViewById(R.id.lowerAutonEditText_IN);
        upperTeleopEditTextIN = findViewById(R.id.upperTeleopEditText_IN);
        lowerTeleopEditTextIN = findViewById(R.id.lowerTeleopEditText_IN);

        upperAutonEditTextOUT = findViewById(R.id.upperAutonEditText_OUT);
        lowerAutonEditTextOUT = findViewById(R.id.lowerAutonEditText_OUT);
        upperTeleopEditTextOUT = findViewById(R.id.upperTeleopEditText_OUT);
        lowerTeleopEditTextOut = findViewById(R.id.lowerTeleopEditText_OUT);

        tarmacEditText = findViewById(R.id.tarmacNumEditText);
        teamNumEditText = findViewById(R.id.teamNumberEditText);
        matchNumEditText = findViewById(R.id.matchNumberEditText);
        comments = findViewById(R.id.comments);
        commentsTxt = findViewById(R.id.commentsTxt);
        generateQr = findViewById(R.id.generateQr);
        nextMatch = findViewById(R.id.nextMatch);

        //instantiating TextViews
        upperTextView = findViewById(R.id.upperText);
        lowerTextView = findViewById(R.id.lowerText);
        loadsFromTextView = findViewById(R.id.loadsText);
        tarmacNumberTextView = findViewById(R.id.tarmacNumText);

        //instantiating CheckBox
        groundCheckBox = findViewById(R.id.groundCheckBox);
        hpCheckBox = findViewById(R.id.hpCheckBox);
        hpShotCheckBox = findViewById(R.id.hpShotCheckBox);
        leftTarmacCheckBox = findViewById(R.id.leftTarmacCheckBox);

        //instantiating QR code
        qrCodeImage = findViewById(R.id.qrCodeIV);
        fieldImage = findViewById(R.id.fieldImage);

        climb = findViewById(R.id.hangarButtons);
        traversal = findViewById(R.id.traversal);
        high = findViewById(R.id.high);
        mid = findViewById(R.id.mid);
        low = findViewById(R.id.low);
        none = findViewById(R.id.none);

        generateQr = findViewById(R.id.generateQr);
        nextMatch = findViewById(R.id.nextMatch);


        timerChronometer.setVisibility(View.INVISIBLE);
        timerStart.setVisibility(View.INVISIBLE);
        timerStop.setVisibility(View.INVISIBLE);
        timerReset.setVisibility(View.INVISIBLE);
        climberTimeText.setVisibility(View.INVISIBLE);



        SharedPreferences new_SP = getApplicationContext().getSharedPreferences("TeamData", MODE_PRIVATE);


        tarmacEditText.setText(new_SP.getString("StartTarmacNum", ""));
        upperAutonEditTextIN.setText(new_SP.getString("NumUpperShotsAuton", ""));
        lowerAutonEditTextIN.setText(new_SP.getString("NumLowerShotsAuton", ""));
        upperAutonEditTextOUT.setText(new_SP.getString("NumUpperMissedAuton", ""));
        lowerAutonEditTextOUT.setText(new_SP.getString("NumUpperMissedAuton", ""));
        teamNumEditText.setText(new_SP.getString("TeamNumber", ""));
        matchNumEditText.setText(new_SP.getString("MatchNumber", ""));
        hpShotCheckBox.setChecked(new_SP.getBoolean("HumanShotAuton", false));
//        hpCheckBox.setChecked(new_SP.getBoolean("HPLoads", false));
//        groundCheckBox.setChecked(new_SP.getBoolean("GroundLoads", false));
        leftTarmacCheckBox.setChecked(new_SP.getBoolean("LeftTarmacAuton", false));


        //ON CLICK EVENTS

        timerChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(MainActivity.this, "BING BONG!", Toast.LENGTH_SHORT);
                }
            }
        });

        //AUTON SHOTS INCREASE DECREASE
        upperAutonShotsIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentUpperShots;
                if(upperAutonEditTextIN.getText().toString().equals("")) {
                    currentUpperShots = 0;
                } else {
                    currentUpperShots = Integer.parseInt(upperAutonEditTextIN.getText().toString());
                }
                if(currentUpperShots >= 0) {
                    currentUpperShots++;
                }
                String currentUpperShotsString = "" + currentUpperShots;
                upperAutonEditTextIN.setText(currentUpperShotsString);
            }
        });

        upperAutonShotsDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentUpperShots;
                if(upperAutonEditTextIN.getText().toString().equals("")) {
                    currentUpperShots = 0;
                } else {
                    currentUpperShots = Integer.parseInt(upperAutonEditTextIN.getText().toString());
                }
                if(currentUpperShots > 0) {
                    currentUpperShots--;
                }
                String currentUpperShotsString = "" + currentUpperShots;
                upperAutonEditTextIN.setText(currentUpperShotsString);
            }
        });

        lowerAutonShotsIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLowerShots;
                if(lowerAutonEditTextIN.getText().toString().equals("")) {
                    currentLowerShots = 0;
                } else {
                    currentLowerShots = Integer.parseInt(lowerAutonEditTextIN.getText().toString());
                }
                if(currentLowerShots >= 0) {
                    currentLowerShots++;
                }
                String currentLowerShotsString = "" + currentLowerShots;
                lowerAutonEditTextIN.setText(currentLowerShotsString);
            }
        });


        lowerAutonShotsDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLowerShots;
                if(lowerAutonEditTextIN.getText().toString().equals("")) {
                    currentLowerShots = 0;
                } else {
                    currentLowerShots = Integer.parseInt(lowerAutonEditTextIN.getText().toString());
                }
                if(currentLowerShots > 0) {
                    currentLowerShots--;
                }
                String currentLowerShotsString = "" + currentLowerShots;
                lowerAutonEditTextIN.setText(currentLowerShotsString);
            }
        });


        //AUTON MISSED INCREASE/DECREASE

        upperAutonMissedIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentUpperShots;
                if(upperAutonEditTextOUT.getText().toString().equals("")) {
                    currentUpperShots = 0;
                } else {
                    currentUpperShots = Integer.parseInt(upperAutonEditTextOUT.getText().toString());
                }
                if(currentUpperShots >= 0) {
                    currentUpperShots++;
                }
                String currentUpperShotsString = "" + currentUpperShots;
                upperAutonEditTextOUT.setText(currentUpperShotsString);
            }
        });

        upperAutonMissedDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentUpperShots;
                if(upperAutonEditTextOUT.getText().toString().equals("")) {
                    currentUpperShots = 0;
                } else {
                    currentUpperShots = Integer.parseInt(upperAutonEditTextOUT.getText().toString());
                }
                if(currentUpperShots > 0) {
                    currentUpperShots--;
                }
                String currentUpperShotsString = "" + currentUpperShots;
                upperAutonEditTextOUT.setText(currentUpperShotsString);
            }
        });

        lowerAutonMissedIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLowerShots;
                if(lowerAutonEditTextOUT.getText().toString().equals("")) {
                    currentLowerShots = 0;
                } else {
                    currentLowerShots = Integer.parseInt(lowerAutonEditTextOUT.getText().toString());
                }
                if(currentLowerShots >= 0) {
                    currentLowerShots++;
                }
                String currentLowerShotsString = "" + currentLowerShots;
                lowerAutonEditTextOUT.setText(currentLowerShotsString);
            }
        });


        lowerAutonMissedDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLowerShots;
                if(lowerAutonEditTextOUT.getText().toString().equals("")) {
                    currentLowerShots = 0;
                } else {
                    currentLowerShots = Integer.parseInt(lowerAutonEditTextOUT.getText().toString());
                }
                if(currentLowerShots > 0) {
                    currentLowerShots--;
                }
                String currentLowerShotsString = "" + currentLowerShots;
                lowerAutonEditTextOUT.setText(currentLowerShotsString);
            }
        });

        //TELEOP SHOTS INCREASE DECREASE

        upperTeleopShotsIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentUpperShots;
                if(upperTeleopEditTextIN.getText().toString().equals("")) {
                    currentUpperShots = 0;
                } else {
                    currentUpperShots = Integer.parseInt(upperTeleopEditTextIN.getText().toString());
                }
                if(currentUpperShots >= 0) {
                    currentUpperShots++;
                }
                String currentUpperShotsString = "" + currentUpperShots;
                upperTeleopEditTextIN.setText(currentUpperShotsString);
            }
        });

        upperTeleopShotsDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentUpperShots;
                if(upperTeleopEditTextIN.getText().toString().equals("")) {
                    currentUpperShots = 0;
                } else {
                    currentUpperShots = Integer.parseInt(upperTeleopEditTextIN.getText().toString());
                }
                if(currentUpperShots > 0) {
                    currentUpperShots--;
                }
                String currentUpperShotsString = "" + currentUpperShots;
                upperTeleopEditTextIN.setText(currentUpperShotsString);
            }
        });

        lowerTeleopShotsIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLowerShots;
                if(lowerTeleopEditTextIN.getText().toString().equals("")) {
                    currentLowerShots = 0;
                } else {
                    currentLowerShots = Integer.parseInt(lowerTeleopEditTextIN.getText().toString());
                }
                if(currentLowerShots >= 0) {
                    currentLowerShots++;
                }
                String currentLowerShotsString = "" + currentLowerShots;
                lowerTeleopEditTextIN.setText(currentLowerShotsString);
            }
        });


        lowerTeleopShotsDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLowerShots;
                if(lowerTeleopEditTextIN.getText().toString().equals("")) {
                    currentLowerShots = 0;
                } else {
                    currentLowerShots = Integer.parseInt(lowerTeleopEditTextIN.getText().toString());
                }
                if(currentLowerShots > 0) {
                    currentLowerShots--;
                }
                String currentLowerShotsString = "" + currentLowerShots;
                lowerTeleopEditTextIN.setText(currentLowerShotsString);
            }
        });

        //TELEOP MISSED INCREASE / DECREASE
        upperTeleopMissedIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentUpperShots;
                if(upperTeleopEditTextOUT.getText().toString().equals("")) {
                    currentUpperShots = 0;
                } else {
                    currentUpperShots = Integer.parseInt(upperTeleopEditTextOUT.getText().toString());
                }
                if(currentUpperShots >= 0) {
                    currentUpperShots++;
                }
                String currentUpperShotsString = "" + currentUpperShots;
                upperTeleopEditTextOUT.setText(currentUpperShotsString);
            }
        });

        upperTeleopMissedDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentUpperShots;
                if(upperTeleopEditTextOUT.getText().toString().equals("")) {
                    currentUpperShots = 0;
                } else {
                    currentUpperShots = Integer.parseInt(upperTeleopEditTextOUT.getText().toString());
                }
                if(currentUpperShots > 0) {
                    currentUpperShots--;
                }
                String currentUpperShotsString = "" + currentUpperShots;
                upperTeleopEditTextOUT.setText(currentUpperShotsString);
            }
        });

        lowerTeleopMissedIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLowerShots;
                if(lowerTeleopEditTextOut.getText().toString().equals("")) {
                    currentLowerShots = 0;
                } else {
                    currentLowerShots = Integer.parseInt(lowerTeleopEditTextOut.getText().toString());
                }
                if(currentLowerShots >= 0) {
                    currentLowerShots++;
                }
                String currentLowerShotsString = "" + currentLowerShots;
                lowerTeleopEditTextOut.setText(currentLowerShotsString);
            }
        });


        lowerTeleopMissedDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLowerShots;
                if(lowerTeleopEditTextOut.getText().toString().equals("")) {
                    currentLowerShots = 0;
                } else {
                    currentLowerShots = Integer.parseInt(lowerTeleopEditTextOut.getText().toString());
                }
                if(currentLowerShots > 0) {
                    currentLowerShots--;
                }
                String currentLowerShotsString = "" + currentLowerShots;
                lowerTeleopEditTextOut.setText(currentLowerShotsString);
            }
        });


        //TARMAC INCREASE DECREASE
        tarmacNumberIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentTarmacNumber;
                if(tarmacEditText.getText().toString().equals("")) {
                    currentTarmacNumber = 0;
                } else {
                    currentTarmacNumber = Integer.parseInt(tarmacEditText.getText().toString());
                }
                if(currentTarmacNumber >= 0 && currentTarmacNumber < 4) {
                    currentTarmacNumber++;
                }
                String currentTarmacNumberString = "" + currentTarmacNumber;
                tarmacEditText.setText(currentTarmacNumberString);
            }
        });

        tarmacNumberDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentTarmacNumber;
                if(tarmacEditText.getText().toString().equals("")) {
                    currentTarmacNumber = 0;
                } else {
                    currentTarmacNumber = Integer.parseInt(tarmacEditText.getText().toString());
                }
                if(currentTarmacNumber > 1) {
                    currentTarmacNumber--;
                }
                String currentTarmacNumberString = "" + currentTarmacNumber;
                tarmacEditText.setText(currentTarmacNumberString);
            }
        });

        increaseMatchNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentMatchNumber;
                if(matchNumEditText.getText().toString().equals("")) {
                    currentMatchNumber = 0;
                } else {
                    currentMatchNumber = Integer.parseInt(matchNumEditText.getText().toString());
                }
                if(currentMatchNumber >= 0 && currentMatchNumber < 99) {
                    currentMatchNumber++;
                }
                String currentTarmacNumberString = "" + currentMatchNumber;
                matchNumEditText.setText(currentTarmacNumberString);
            }
        });

        decreaseMatchNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentMatchNumber;
                if(matchNumEditText.getText().toString().equals("")) {
                    currentMatchNumber = 0;
                }
                else {
                    currentMatchNumber = Integer.parseInt(matchNumEditText.getText().toString());
                }
                if(currentMatchNumber > 0) {
                    currentMatchNumber--;
                }
                String currentTarmacNumberString = "" + currentMatchNumber;
                matchNumEditText.setText(currentTarmacNumberString);
            }
        });

        timerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChronometer(view);
                timerStart.setVisibility(View.INVISIBLE);
                timerStop.setVisibility(View.VISIBLE);
            }
        });

        timerStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseChronometer(view);
                timerStop.setVisibility(View.INVISIBLE);
                timerStart.setVisibility(View.VISIBLE);
            }
        });
        autonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences new_SP = getApplicationContext().getSharedPreferences("TeamData", MODE_PRIVATE);


                tarmacEditText.setText(new_SP.getString("StartTarmacNum", ""));;
                upperAutonEditTextIN.setText(new_SP.getString("NumUpperShotsAuton", ""));;
                lowerAutonEditTextIN.setText(new_SP.getString("NumLowerShotsAuton", ""));;
                upperAutonEditTextOUT.setText(new_SP.getString("NumUpperMissedAuton", ""));
                lowerAutonEditTextOUT.setText(new_SP.getString("NumUpperMissedAuton", ""));
                hpShotCheckBox.setChecked(new_SP.getBoolean("HumanShotAuton", false));
                hpCheckBox.setChecked(new_SP.getBoolean("HPLoads", false));
                groundCheckBox.setChecked(new_SP.getBoolean("GroundLoads", false));
                leftTarmacCheckBox.setChecked(new_SP.getBoolean("LeftTarmacAuton", false));


                autonButton.setImageResource(R.drawable.auton_1);
                teleopButton.setImageResource(R.drawable.teleop_0);
                endgameButton.setImageResource(R.drawable.endgame_0);
                qrCodeButton.setImageResource(R.drawable.qrcode_0);

                timerChronometer.setVisibility(View.INVISIBLE);
                timerStart.setVisibility(View.INVISIBLE);
                timerStop.setVisibility(View.INVISIBLE);
                timerReset.setVisibility(View.INVISIBLE);
                climberTimeText.setVisibility(View.INVISIBLE);

                qrCodeImage.setVisibility(View.INVISIBLE);
                climb.setVisibility(View.INVISIBLE);
                comments.setVisibility(View.INVISIBLE);
                commentsTxt.setVisibility(View.INVISIBLE);
                generateQr.setVisibility(View.INVISIBLE);
                nextMatch.setVisibility(View.INVISIBLE);

                upperTeleopEditTextIN.setVisibility(View.INVISIBLE);
                lowerTeleopEditTextIN.setVisibility(View.INVISIBLE);
                upperTeleopEditTextOUT.setVisibility(View.INVISIBLE);
                lowerTeleopEditTextOut.setVisibility(View.INVISIBLE);


                lowerTeleopShotsIncrease.setVisibility(View.INVISIBLE);
                lowerTeleopShotsDecrease.setVisibility(View.INVISIBLE);
                upperTeleopShotsIncrease.setVisibility(View.INVISIBLE);
                upperTeleopShotsDecrease.setVisibility(View.INVISIBLE);

                lowerTeleopMissedIncrease.setVisibility(View.INVISIBLE);
                lowerTeleopMissedDecrease.setVisibility(View.INVISIBLE);
                upperTeleopMissedIncrease.setVisibility(View.INVISIBLE);
                lowerTeleopMissedDecrease.setVisibility(View.INVISIBLE);



                tarmacNumberTextView.setVisibility(View.VISIBLE);
                tarmacEditText.setVisibility(View.VISIBLE);
                tarmacNumberIncrease.setVisibility(View.VISIBLE);
                tarmacNumberDecrease.setVisibility(View.VISIBLE);
                hpShotCheckBox.setVisibility(View.VISIBLE);
                leftTarmacCheckBox.setVisibility(View.VISIBLE);
                fieldImage.setVisibility(View.VISIBLE);
                upperTextView.setVisibility(View.VISIBLE);
                upperAutonEditTextIN.setVisibility(View.VISIBLE);
                upperAutonEditTextOUT.setVisibility(View.VISIBLE);
                lowerTextView.setVisibility(View.VISIBLE);
                lowerAutonEditTextIN.setVisibility(View.VISIBLE);
                lowerAutonEditTextOUT.setVisibility(View.VISIBLE);
                loadsFromTextView.setVisibility(View.VISIBLE);
                groundCheckBox.setVisibility(View.VISIBLE);
                hpCheckBox.setVisibility(View.VISIBLE);


                lowerAutonShotsIncrease.setVisibility(View.VISIBLE);
                lowerAutonShotsDecrease.setVisibility(View.VISIBLE);
                upperAutonShotsIncrease.setVisibility(View.VISIBLE);
                upperAutonShotsDecrease.setVisibility(View.VISIBLE);

                lowerAutonMissedIncrease.setVisibility(View.VISIBLE);
                lowerAutonMissedDecrease.setVisibility(View.VISIBLE);
                upperAutonMissedIncrease.setVisibility(View.VISIBLE);
                upperAutonMissedDecrease.setVisibility(View.VISIBLE);

            }
        });

            teleopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences new_SP = getApplicationContext().getSharedPreferences("TeamData", MODE_PRIVATE);


                    upperTeleopEditTextIN.setText(new_SP.getString("NumUpperShotsTeleOP", ""));
                    lowerTeleopEditTextIN.setText(new_SP.getString("NumLowerShotsTeleOP", ""));

//                    hpCheckBox.setChecked(new_SP.getBoolean("HPLoads", false));
//                    groundCheckBox.setChecked(new_SP.getBoolean("GroundLoads", false));
//                    leftTarmacCheckBox.setChecked(new_SP.getBoolean("LeftTarmacAuton", false));
//                    hpCheckBox.setChecked(new_SP.getBoolean("HPLoads", false));
//                    groundCheckBox.setChecked(new_SP.getBoolean("GroundLoads", false));


                    autonButton.setImageResource(R.drawable.auton_0);
                    teleopButton.setImageResource(R.drawable.teleop_1);
                    endgameButton.setImageResource(R.drawable.endgame_0);
                    qrCodeButton.setImageResource(R.drawable.qrcode_0);

                    //Visible/Invisible Toggle
                    tarmacNumberTextView.setVisibility(View.INVISIBLE);
                    tarmacEditText.setVisibility(View.INVISIBLE);
                    tarmacNumberIncrease.setVisibility(View.INVISIBLE);
                    tarmacNumberDecrease.setVisibility(View.INVISIBLE);
                    hpShotCheckBox.setVisibility(View.INVISIBLE);
                    leftTarmacCheckBox.setVisibility(View.INVISIBLE);
                    qrCodeImage.setVisibility(View.INVISIBLE);
                    climb.setVisibility(View.INVISIBLE);
                    comments.setVisibility(View.INVISIBLE);
                    commentsTxt.setVisibility(View.INVISIBLE);
                    generateQr.setVisibility(View.INVISIBLE);
                    nextMatch.setVisibility(View.INVISIBLE);
                    upperAutonEditTextIN.setVisibility(View.INVISIBLE);
                    lowerAutonEditTextIN.setVisibility(View.INVISIBLE);
                    upperAutonEditTextOUT.setVisibility(View.INVISIBLE);
                    lowerAutonEditTextOUT.setVisibility(View.INVISIBLE);
                    lowerAutonShotsIncrease.setVisibility(View.INVISIBLE);
                    lowerAutonShotsDecrease.setVisibility(View.INVISIBLE);
                    upperAutonShotsIncrease.setVisibility(View.INVISIBLE);
                    upperAutonShotsDecrease.setVisibility(View.INVISIBLE);

                    lowerAutonMissedIncrease.setVisibility(View.INVISIBLE);
                    lowerAutonMissedDecrease.setVisibility(View.INVISIBLE);
                    upperAutonMissedIncrease.setVisibility(View.INVISIBLE);
                    upperAutonMissedDecrease.setVisibility(View.INVISIBLE);

                    timerChronometer.setVisibility(View.INVISIBLE);
                    timerStart.setVisibility(View.INVISIBLE);
                    timerStop.setVisibility(View.INVISIBLE);
                    timerReset.setVisibility(View.INVISIBLE);
                    climberTimeText.setVisibility(View.INVISIBLE);

                    fieldImage.setVisibility(View.VISIBLE);
                    upperTextView.setVisibility(View.VISIBLE);
                    commentsTxt.setVisibility(View.INVISIBLE);
                    upperTeleopEditTextIN.setVisibility(View.VISIBLE);
                    upperTeleopEditTextOUT.setVisibility(View.VISIBLE);
                    lowerTextView.setVisibility(View.VISIBLE);
                    lowerTeleopEditTextIN.setVisibility(View.VISIBLE);
                    lowerTeleopEditTextOut.setVisibility(View.VISIBLE);
                    loadsFromTextView.setVisibility(View.VISIBLE);
                    groundCheckBox.setVisibility(View.VISIBLE);
                    hpCheckBox.setVisibility(View.VISIBLE);

                    lowerTeleopShotsIncrease.setVisibility(View.VISIBLE);
                    lowerTeleopShotsDecrease.setVisibility(View.VISIBLE);
                    upperTeleopShotsIncrease.setVisibility(View.VISIBLE);
                    upperTeleopShotsDecrease.setVisibility(View.VISIBLE);

                    lowerTeleopMissedIncrease.setVisibility(View.VISIBLE);
                    lowerTeleopMissedDecrease.setVisibility(View.VISIBLE);
                    upperTeleopMissedIncrease.setVisibility(View.VISIBLE);
                    upperTeleopMissedDecrease.setVisibility(View.VISIBLE);

                    upperShotsAuton = upperAutonEditTextIN.getText().toString();
                    lowerShotsAuton = lowerAutonEditTextIN.getText().toString();
                    upperMissedAuton = upperAutonEditTextOUT.getText().toString();
                    lowerMissedAuton = lowerAutonEditTextOUT.getText().toString();
                    tarmacNumber = tarmacEditText.getText().toString();
                    teamNumber = teamNumEditText.getText().toString();
                    matchNumber = matchNumEditText.getText().toString();

                    //Checks for Checkboxes

                    if (groundCheckBox.isChecked()) {
                        groundLoads = true;
                    }
                    else {
                        groundLoads = false;
                    }
                    if (hpCheckBox.isChecked()) {
                        hpLoads = true;
                    }
                    else {
                        hpLoads = false;
                    }

                    if (hpShotCheckBox.isChecked()) {
                        humanShot = true;
                    } else {
                        humanShot = false;
                    }

                    if (leftTarmacCheckBox.isChecked()) {
                        leftTarmac = true;
                    } else {
                        leftTarmac = false;
                    }

                    //edit sharedPreferences
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("NumUpperShotsAuton", upperShotsAuton);
                    editor.putString("NumLowerShotsAuton", lowerShotsAuton);
                    editor.putString("NumUpperMissedAuton", upperMissedAuton);
                    editor.putString("NumLowerMissedAuton", lowerMissedAuton);
                    editor.putString("StartTarmacNum", tarmacNumber);
                    editor.putBoolean("GroundLoads", groundLoads);
                    editor.putBoolean("HPLoads", hpLoads);
                    editor.putBoolean("HumanShotAuton", humanShot);
                    editor.putBoolean("LeftTarmacAuton", leftTarmac);
                    editor.putString("TeamNumber", teamNumber);
                    editor.putString("MatchNumber", matchNumber);
                    editor.commit();
//                Toast.makeText(MainActivity.this, "Auton Data Saved!", Toast.LENGTH_LONG).show();

//                upperTeleopEditText.setText("");
//                lowerTeleopEditText.setText("");
                }
            });

        endgameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences new_SP = getApplicationContext().getSharedPreferences("TeamData", MODE_PRIVATE);

                String climbAttempted = new_SP.getString("ClimbAttempted", "");


//                hpCheckBox.setChecked(new_SP.getBoolean("HPLoads", false));
//                groundCheckBox.setChecked(new_SP.getBoolean("GroundLoads", false));



                if(climbAttempted.equals("Traversal")) {
                    traversal.setChecked(true);
                }
                else if(climbAttempted.equals("High")) {
                    high.setChecked(true);
                }
                else if(climbAttempted.equals("Mid")) {
                    mid.setChecked(true);
                }
                else if(climbAttempted.equals("Low")) {
                    low.setChecked(true);
                }
                else if(climbAttempted.equals(("None"))) {  
                    none.setChecked(true);
                }

                autonButton.setImageResource(R.drawable.auton_0);
                teleopButton.setImageResource(R.drawable.teleop_0);
                endgameButton.setImageResource(R.drawable.endgame_1);
                qrCodeButton.setImageResource(R.drawable.qrcode_0);
                //Visible/Invisible Toggle
                climb.setVisibility(View.VISIBLE);
                fieldImage.setVisibility(View.VISIBLE);
                upperTextView.setVisibility(View.VISIBLE);
                upperTeleopEditTextIN.setVisibility(View.VISIBLE);
                upperTeleopEditTextOUT.setVisibility(View.VISIBLE);
                lowerTextView.setVisibility(View.VISIBLE);
                lowerTeleopEditTextIN.setVisibility(View.VISIBLE);
                lowerTeleopEditTextOut.setVisibility(View.VISIBLE);
                loadsFromTextView.setVisibility(View.VISIBLE);
                groundCheckBox.setVisibility(View.VISIBLE);
                hpCheckBox.setVisibility(View.VISIBLE);
                upperTeleopShotsIncrease.setVisibility(View.VISIBLE);
                upperTeleopShotsDecrease.setVisibility(View.VISIBLE);
                lowerTeleopShotsIncrease.setVisibility(View.VISIBLE);
                lowerTeleopShotsDecrease.setVisibility(View.VISIBLE);
                lowerTeleopMissedIncrease.setVisibility(View.VISIBLE);
                lowerTeleopMissedDecrease.setVisibility(View.VISIBLE);
                upperTeleopMissedIncrease.setVisibility(View.VISIBLE);
                upperTeleopMissedDecrease.setVisibility(View.VISIBLE);
                timerChronometer.setVisibility(View.VISIBLE);
                timerStart.setVisibility(View.VISIBLE);
                timerStop.setVisibility(View.INVISIBLE);
                timerReset.setVisibility(View.VISIBLE);
                climberTimeText.setVisibility(View.VISIBLE);

                tarmacNumberTextView.setVisibility(View.INVISIBLE);
                tarmacEditText.setVisibility(View.INVISIBLE);
                tarmacNumberIncrease.setVisibility(View.INVISIBLE);
                tarmacNumberDecrease.setVisibility(View.INVISIBLE);
                hpShotCheckBox.setVisibility(View.INVISIBLE);
                leftTarmacCheckBox.setVisibility(View.INVISIBLE);
                qrCodeImage.setVisibility(View.INVISIBLE);
                comments.setVisibility(View.INVISIBLE);
                commentsTxt.setVisibility(View.INVISIBLE);
                generateQr.setVisibility(View.INVISIBLE);
                nextMatch.setVisibility(View.INVISIBLE);
                upperAutonEditTextIN.setVisibility(View.INVISIBLE);
                lowerAutonEditTextIN.setVisibility(View.INVISIBLE);
                upperAutonEditTextOUT.setVisibility(View.INVISIBLE);
                lowerAutonEditTextOUT.setVisibility(View.INVISIBLE);
                lowerAutonShotsIncrease.setVisibility(View.INVISIBLE);
                lowerAutonShotsDecrease.setVisibility(View.INVISIBLE);
                upperAutonShotsIncrease.setVisibility(View.INVISIBLE);
                upperAutonShotsDecrease.setVisibility(View.INVISIBLE);
                lowerAutonMissedIncrease.setVisibility(View.INVISIBLE);
                lowerAutonMissedDecrease.setVisibility(View.INVISIBLE);
                upperAutonMissedIncrease.setVisibility(View.INVISIBLE);
                upperAutonMissedDecrease.setVisibility(View.INVISIBLE);

                upperShotsTeleOP = upperTeleopEditTextIN.getText().toString();
                lowerShotsTeleOP = lowerTeleopEditTextIN.getText().toString();
                upperMissedTeleOP = upperTeleopEditTextOUT.getText().toString();
                lowerMissedTeleOP = lowerTeleopEditTextOut.getText().toString();

                if(groundCheckBox.isChecked()) {
                    groundLoads = true;
                }
                else {
                    groundLoads = false;
                }
                if(hpCheckBox.isChecked()) {
                    hpLoads = true;
                }
                else {
                    hpLoads = false;
                }

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("NumUpperShotsTeleOP", upperShotsTeleOP);
                editor.putString("NumLowerShotsTeleOP", lowerShotsTeleOP);
                editor.putString("NumUpperMissedTeleOP", upperMissedTeleOP);
                editor.putString("NumLowerMissedTeleOP", lowerMissedTeleOP);
                editor.putBoolean("GroundLoads", groundLoads);
                editor.putBoolean("HPLoads", hpLoads);
                editor.putString("TeamNumber", teamNumber);
                editor.putString("MatchNumber", matchNumber);
                editor.commit();

//                Toast.makeText(MainActivity.this, "Tele OP Data Saved!", Toast.LENGTH_LONG).show();



            }
        });
        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autonButton.setImageResource(R.drawable.auton_0);
                teleopButton.setImageResource(R.drawable.teleop_0);
                endgameButton.setImageResource(R.drawable.endgame_0);
                qrCodeButton.setImageResource(R.drawable.qrcode_1);
//                Toast.makeText(MainActivity.this, "Endgame Data Saved!", Toast.LENGTH_LONG).show();
                climbTime = timerChronometer.getText().toString();
                if(traversal.isChecked()) {
                    climbAttempted = "Traversal";
                }
                else if(high.isChecked()) {
                    climbAttempted = "High";
                }
                else if(mid.isChecked()) {
                    climbAttempted = "Mid";
                }
                else if(low.isChecked()) {
                    climbAttempted = "Low";
                }
                else if(none.isChecked()) {
                    climbAttempted = "None";
                }

                upperShotsTeleOP = upperTeleopEditTextIN.getText().toString();
                lowerShotsTeleOP = lowerTeleopEditTextIN.getText().toString();
                upperMissedTeleOP = upperTeleopEditTextOUT.getText().toString();
                lowerMissedTeleOP = lowerTeleopEditTextOut.getText().toString();

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ClimbAttempted", climbAttempted);
                editor.putString("ClimbTime", climbTime);
                editor.putString("NumUpperShotsTeleOP", upperShotsTeleOP);
                editor.putString("NumLowerShotsTeleOP", lowerShotsTeleOP);
                editor.putString("NumUpperMissedTeleOP", upperMissedTeleOP);
                editor.putString("NumLowerMissedTeleOP", lowerMissedTeleOP);
                editor.putString("TeamNumber", teamNumber);
                editor.putString("MatchNumber", matchNumber);
                editor.commit();

                //Visible/Invisible Toggle
                qrCodeImage.setVisibility(View.INVISIBLE);
                comments.setVisibility(View.VISIBLE);
                commentsTxt.setVisibility(View.VISIBLE);
                generateQr.setVisibility(View.VISIBLE);
                nextMatch.setVisibility(View.VISIBLE);

                upperAutonEditTextIN.setVisibility(View.INVISIBLE);
                lowerAutonEditTextIN.setVisibility(View.INVISIBLE);
                upperTeleopEditTextIN.setVisibility(View.INVISIBLE);
                lowerTeleopEditTextIN.setVisibility(View.INVISIBLE);

                upperAutonEditTextOUT.setVisibility(View.INVISIBLE);
                lowerAutonEditTextOUT.setVisibility(View.INVISIBLE);
                upperTeleopEditTextOUT.setVisibility(View.INVISIBLE);
                lowerTeleopEditTextOut.setVisibility(View.INVISIBLE);

                tarmacEditText.setVisibility(View.INVISIBLE);


                timerChronometer.setVisibility(View.INVISIBLE);
                timerStart.setVisibility(View.INVISIBLE);
                timerStop.setVisibility(View.INVISIBLE);
                timerReset.setVisibility(View.INVISIBLE);
                climberTimeText.setVisibility(View.INVISIBLE);

                groundCheckBox.setVisibility(View.INVISIBLE);
                hpCheckBox.setVisibility(View.INVISIBLE);
                hpShotCheckBox.setVisibility(View.INVISIBLE);
                leftTarmacCheckBox.setVisibility(View.INVISIBLE);

                upperTextView.setVisibility(View.INVISIBLE);
                lowerTextView.setVisibility(View.INVISIBLE);
                tarmacNumberTextView.setVisibility(View.INVISIBLE);
                loadsFromTextView.setVisibility(View.INVISIBLE);

                upperAutonShotsIncrease.setVisibility(View.INVISIBLE);
                upperAutonShotsDecrease.setVisibility(View.INVISIBLE);
                lowerAutonShotsIncrease.setVisibility(View.INVISIBLE);
                lowerAutonShotsDecrease.setVisibility(View.INVISIBLE);
                lowerAutonMissedIncrease.setVisibility(View.INVISIBLE);
                lowerAutonMissedDecrease.setVisibility(View.INVISIBLE);
                upperAutonMissedIncrease.setVisibility(View.INVISIBLE);
                upperAutonMissedDecrease.setVisibility(View.INVISIBLE);

                upperTeleopShotsIncrease.setVisibility(View.INVISIBLE);
                upperTeleopShotsDecrease.setVisibility(View.INVISIBLE);
                lowerTeleopShotsIncrease.setVisibility(View.INVISIBLE);
                lowerTeleopShotsDecrease.setVisibility(View.INVISIBLE);
                lowerTeleopMissedIncrease.setVisibility(View.INVISIBLE);
                lowerTeleopMissedDecrease.setVisibility(View.INVISIBLE);
                upperTeleopMissedIncrease.setVisibility(View.INVISIBLE);
                upperTeleopMissedDecrease.setVisibility(View.INVISIBLE);

                tarmacNumberIncrease.setVisibility(View.INVISIBLE);
                tarmacNumberDecrease.setVisibility(View.INVISIBLE);

                fieldImage.setVisibility(View.INVISIBLE);

                climb.setVisibility(View.INVISIBLE);
            };
        });

        generateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrCodeImage.setVisibility(View.INVISIBLE);
                String scoutComments = comments.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Comments", scoutComments);
                editor.commit();
                SharedPreferences new_SP = getApplicationContext().getSharedPreferences("TeamData", MODE_PRIVATE);
                String scoutCommentsString, numUpperShotsMissedAuton, numLowerShotsMissedAuton, numUpperShotsMissedTeleOP, numLowerShotsMissedTeleOP, timeToClimb, numUpperShotsAuton, numLowerShotsAuton, numUpperShotsTeleOP, numLowerShotsTeleOP, startTarmacNum, climbAttemptedString, humanLoadsString, groundLoadsString, humanShootString, leaveTarmacString, teamNumberString, matchNumberString; //new Strings to get data
                boolean didHumanShoot, didLeaveTarmac, didGroundLoads, didHPLoads; //new vars for booleans

                if(new_SP.getString("NumUpperShotsAuton", "").equals("")){
                    numUpperShotsAuton = "0";
                } else {
                    numUpperShotsAuton = new_SP.getString("NumUpperShotsAuton", "");
                }


                if(new_SP.getString("NumLowerShotsAuton", "").equals("")){
                    numLowerShotsAuton = "0";
                } else {
                    numLowerShotsAuton = new_SP.getString("NumLowerShotsAuton", "");
                }

                if(new_SP.getString("NumUpperMissedAuton", "").equals("")){
                    numUpperShotsMissedAuton = "0";
                } else {
                    numUpperShotsMissedAuton = new_SP.getString("NumUpperMissedAuton", "");
                }


                if(new_SP.getString("NumLowerMissedAuton", "").equals("")){
                    numLowerShotsMissedAuton = "0";
                } else {
                    numLowerShotsMissedAuton = new_SP.getString("NumLowerMissedAuton", "");
                }


                //TELEOP

                if(new_SP.getString("NumUpperShotsTeleOP", "").equals("")){
                    numUpperShotsTeleOP = "0";
                } else {
                    numUpperShotsTeleOP = new_SP.getString("NumUpperShotsTeleOP", "");
                }


                if(new_SP.getString("NumLowerShotsTeleOP", "").equals("")){
                    numLowerShotsTeleOP = "0";
                } else {
                    numLowerShotsTeleOP = new_SP.getString("NumLowerShotsTeleOP", "");
                }

                if(new_SP.getString("NumUpperMissedTeleOP", "").equals("")){
                    numUpperShotsMissedTeleOP = "0";
                } else {
                    numUpperShotsMissedTeleOP = new_SP.getString("NumUpperMissedTeleOP", "");
                }


                if(new_SP.getString("NumLowerMissedTeleOP", "").equals("")){
                    numLowerShotsMissedTeleOP = "0";
                } else {
                    numLowerShotsMissedTeleOP = new_SP.getString("NumLowerMissedTeleOP", "");
                }

                if(new_SP.getString("StartTarmacNum", "").equals("")) {
                    startTarmacNum = "1";
                }
                else {
                    startTarmacNum = new_SP.getString("StartTarmacNum", "");
                }

                if(new_SP.getString("ClimbAttempted", "").equals("")) {
                    climbAttemptedString = "None";
                }
                else {
                    climbAttemptedString = new_SP.getString("ClimbAttempted", "");
                }

                if(new_SP.getString("Comments", "").equals("")) {
                    scoutCommentsString = "N/A";
                }
                else{
                    scoutCommentsString = new_SP.getString("Comments", "");
                }
                //instantiating strings
//                climbAttemptedString = new_SP.getString("ClimbAttempted", "");
                teamNumberString = new_SP.getString("TeamNumber", "");
                matchNumberString = new_SP.getString("MatchNumber", "");
//                numUpperShotsAuton = new_SP.getString("NumUpperShotsAuton", "");
//                numLowerShotsAuton = new_SP.getString("NumLowerShotsAuton", "");
//                numUpperShotsMissedAuton = new_SP.getString("NumUpperMissedAuton", "");
//                numLowerShotsMissedAuton = new_SP.getString("NumLowerMissedAuton", "");
//                startTarmacNum = new_SP.getString("StartTarmacNum", "");

//                scoutCommentsString = new_SP.getString("Comments", "");
                timeToClimb = new_SP.getString("ClimbTime", "");

//                numUpperShotsTeleOP = new_SP.getString("NumUpperShotsTeleOP", "");
//                numLowerShotsTeleOP = new_SP.getString("NumLowerShotsTeleOP", "");
//                numUpperShotsMissedTeleOP = new_SP.getString("NumUpperMissedTeleOP", "");
//                numLowerShotsMissedTeleOP = new_SP.getString("NumLowerMissedTeleOP", "");


                //instantiating booleans
                didHumanShoot = new_SP.getBoolean("HumanShotAuton", false);
                didLeaveTarmac = new_SP.getBoolean("LeftTarmacAuton", false);
//
                didGroundLoads = new_SP.getBoolean("GroundLoads", false);
                didHPLoads = new_SP.getBoolean("HPLoads", false);

                if(didHumanShoot) {
                    humanShootString = "Yes";
                }
                else {
                    humanShootString = "No";
                }

                if(didLeaveTarmac) {
                    leaveTarmacString = "Yes";
                }
                else {
                    leaveTarmacString = "No";
                }

                if(didGroundLoads) {
                    groundLoadsString = "Yes";
                }
                else {
                    groundLoadsString = "No";
                }

                if(didHPLoads) {
                    humanLoadsString = "Yes";
                }
                else {
                    humanLoadsString = "No";
                }

               /* String output = "Team Number: " + teamNumberString
                        +"\nMatch Number: " + matchNumberString
                        +"\nAUTON:"
                        +"\nUpper Shots Made: " + numUpperShotsAuton
                        + "\nLower Shots Made: " + numLowerShotsAuton
                        + "\nStarting Tarmac Number: " + startTarmacNum
                        + "\nLoads Originated From: " + groundLoadsString + " " + humanLoadsString
                        + "\nDid Human Player Shoot? " + humanShootString
                        + "\nDid Robot Leave Tarmac? " + leaveTarmacString
                        +"\nTELEOP:"
                        +"\nUpper Shots Made: " + numUpperShotsTeleOP
                        +"\nLower Shots Made: " + numLowerShotsTeleOP
                        +"\nENDGAME:"
                        +"\nClimb Attempted: " + climbAttemptedString
                        +"\nCOMMENTS:"
                        +"\n" + scoutCommentsString;*/

                    String csvOutput = matchNumberString
                        + ',' + teamNumberString
                        + ',' + numUpperShotsAuton
                            + ',' + numUpperShotsMissedAuton
                        + ',' + numLowerShotsAuton
                            + ',' + numLowerShotsMissedAuton
                        + ',' + startTarmacNum
                        + ',' + humanShootString
                            + ',' + leaveTarmacString
                            + ',' + groundLoadsString
                            + ',' + humanLoadsString
                        + ',' + numUpperShotsTeleOP
                            + ',' + numUpperShotsMissedTeleOP
                        + ',' + numLowerShotsTeleOP
                            + ',' + numLowerShotsMissedTeleOP
                        + ',' + climbAttemptedString
                            + ',' + timeToClimb
                            + ',' + scoutCommentsString;

                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix = writer.encode(csvOutput, BarcodeFormat.QR_CODE, 350, 350);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    qrCodeImage.setImageBitmap(bitmap);
                    qrCodeImage.setVisibility(View.VISIBLE);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });
        nextMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autonButton.setImageResource(R.drawable.auton_1);
                teleopButton.setImageResource(R.drawable.teleop_0);
                endgameButton.setImageResource(R.drawable.endgame_0);
                qrCodeButton.setImageResource(R.drawable.qrcode_0);

                qrCodeImage.setVisibility(View.INVISIBLE);
                climb.setVisibility(View.INVISIBLE);
                comments.setVisibility(View.INVISIBLE);
                commentsTxt.setVisibility(View.INVISIBLE);
                generateQr.setVisibility(View.INVISIBLE);
                nextMatch.setVisibility(View.INVISIBLE);

                tarmacNumberTextView.setVisibility(View.VISIBLE);
                tarmacEditText.setVisibility(View.VISIBLE);
                tarmacNumberIncrease.setVisibility(View.VISIBLE);
                tarmacNumberDecrease.setVisibility(View.VISIBLE);
                hpShotCheckBox.setVisibility(View.VISIBLE);
                leftTarmacCheckBox.setVisibility(View.VISIBLE);
                fieldImage.setVisibility(View.VISIBLE);
                upperTextView.setVisibility(View.VISIBLE);
                upperAutonEditTextIN.setVisibility(View.VISIBLE);
                upperAutonEditTextOUT.setVisibility(View.VISIBLE);
                upperTeleopEditTextIN.setVisibility(View.INVISIBLE);
                upperTeleopEditTextOUT.setVisibility(View.INVISIBLE);
                lowerTextView.setVisibility(View.VISIBLE);
                lowerAutonEditTextIN.setVisibility(View.VISIBLE);
                lowerAutonEditTextOUT.setVisibility(View.VISIBLE);
                lowerTeleopEditTextIN.setVisibility(View.INVISIBLE);
                lowerTeleopEditTextOut.setVisibility(View.INVISIBLE);
                loadsFromTextView.setVisibility(View.VISIBLE);
                groundCheckBox.setVisibility(View.VISIBLE);
                hpCheckBox.setVisibility(View.VISIBLE);
                lowerAutonShotsIncrease.setVisibility(View.VISIBLE);
                lowerAutonShotsDecrease.setVisibility(View.VISIBLE);
                upperAutonShotsIncrease.setVisibility(View.VISIBLE);
                upperAutonShotsDecrease.setVisibility(View.VISIBLE);
                lowerAutonMissedIncrease.setVisibility(View.VISIBLE);
                lowerAutonMissedDecrease.setVisibility(View.VISIBLE);
                upperAutonMissedIncrease.setVisibility(View.VISIBLE);
                upperAutonMissedDecrease.setVisibility(View.VISIBLE);

                lowerTeleopShotsIncrease.setVisibility(View.INVISIBLE);
                lowerTeleopShotsDecrease.setVisibility(View.INVISIBLE);
                upperTeleopShotsIncrease.setVisibility(View.INVISIBLE);
                upperTeleopShotsDecrease.setVisibility(View.INVISIBLE);
                lowerTeleopMissedIncrease.setVisibility(View.INVISIBLE);
                lowerTeleopMissedDecrease.setVisibility(View.INVISIBLE);
                upperTeleopMissedIncrease.setVisibility(View.INVISIBLE);
                upperTeleopMissedDecrease.setVisibility(View.INVISIBLE);


                upperAutonEditTextIN.setText("");
                lowerAutonEditTextIN.setText("");
                upperTeleopEditTextIN.setText("");
                lowerTeleopEditTextIN.setText("");

                upperAutonEditTextOUT.setText("");
                lowerAutonEditTextOUT.setText("");
                upperTeleopEditTextOUT.setText("");
                lowerTeleopEditTextOut.setText("");

                tarmacEditText.setText("");
                groundCheckBox.setChecked(false);
                hpCheckBox.setChecked(false);
                hpShotCheckBox.setChecked(false);
                leftTarmacCheckBox.setChecked(false);
                teamNumEditText.setText("");
                matchNumEditText.setText("");
                comments.setText("");
                traversal.setChecked(false);
                high.setChecked(false);
                mid.setChecked(false);
                low.setChecked(false);
                none.setChecked(false);


                SharedPreferences.Editor editor = sp.edit();
                editor.putString("NumUpperShotsAuton", "");
                editor.putString("NumLowerShotsAuton", "");
                editor.putString("NumUpperMissedAuton", "");
                editor.putString("NumLowerMissedAuton", "");
                editor.putString("NumUpperMissedTeleOP", "");
                editor.putString("NumLowerMissedTeleOP", "");
                editor.putString("StartTarmacNum", "");
                editor.putBoolean("GroundLoads", false);
                editor.putBoolean("HPLoads", false);
                editor.putBoolean("HumanShotAuton", false);
                editor.putBoolean("LeftTarmacAuton", false);
                editor.putString("TeamNumber", "");
                editor.putString("MatchNumber", "");
                editor.putString("NumUpperShotsTeleOP", "");
                editor.putString("NumLowerShotsTeleOP", "");
                editor.putString("ClimbAttempted", "");
                editor.putString("ClimbTime", "");
                editor.putString("Comments", "");
                editor.commit();

                timerChronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
            }
        });
    }

    public void startChronometer(View v) {
        if(!running) {
            timerChronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            timerChronometer.start();
            running = true;

        }
    }

    public void pauseChronometer(View v) {
        if(running) {
            timerChronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - timerChronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer(View v) {
        timerChronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }





}