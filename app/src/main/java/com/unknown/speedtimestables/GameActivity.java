package com.unknown.speedtimestables;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static int CORRECT = 1;
    private static int WRONG = 0;

    private TextView tvStage, tvTimes;
    private Button bt1, bt2, bt3, bt4;
    private int stageNum = 0, num1, num2, answer, progress = 10000, maxProgress = 10000;
    private String times;
    private ImageView imageView;
    private ArrayList<Integer> arrNum = new ArrayList<>();
    private ProgressBar progressBar;
    private TimerThread timerThread;
    private boolean isRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvStage = findViewById(R.id.tv_stage);
        tvTimes = findViewById(R.id.tv_times);
        imageView = findViewById(R.id.iv);
        progressBar = findViewById(R.id.pb);
        bt1 = findViewById(R.id.bt_1);
        bt2 = findViewById(R.id.bt_2);
        bt3 = findViewById(R.id.bt_3);
        bt4 = findViewById(R.id.bt_4);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);

        nextStage();

    }

    private void nextStage() {
        bt1.setEnabled(true);
        bt2.setEnabled(true);
        bt3.setEnabled(true);
        bt4.setEnabled(true);
        imageView.setVisibility(View.GONE);
        stageNum++;
        String stage = "STAGE " + stageNum;
        tvStage.setText(stage);

        if (stageNum < 5) {
            maxProgress -= (maxProgress / 7);
            progressBar.setMax(maxProgress);
            progress = maxProgress;
        } else if (stageNum < 10) {
            maxProgress -= (maxProgress / 10);
            progressBar.setMax(maxProgress);
            progress = maxProgress;
        } else if (stageNum < 15) {
            maxProgress -= (maxProgress / 12);
            progressBar.setMax(maxProgress);
            progress = maxProgress;
        } else if (stageNum < 25) {
            maxProgress -= (maxProgress / 15);
            progressBar.setMax(maxProgress);
            progress = maxProgress;
        } else {
            maxProgress = 1000;
            progressBar.setMax(maxProgress);
            progress = maxProgress;
        }


        Log.e("GameA", stageNum + " : " + maxProgress + "");

        num1 = getRandomNum();
        num2 = getRandomNum();
        times = num1 + " X " + num2;
        tvTimes.setText(times);
        answer = num1 * num2;

        arrNum.clear();
        arrNum.add(answer);
        arrNum.add(answer + num1);
        arrNum.add(answer - num2);
        arrNum.add(answer + num1 + 3);
        Collections.shuffle(arrNum);

        bt1.setText(String.valueOf(arrNum.get(0)));
        bt2.setText(String.valueOf(arrNum.get(1)));
        bt3.setText(String.valueOf(arrNum.get(2)));
        bt4.setText(String.valueOf(arrNum.get(3)));

        isRun = true;
        timerThread = new TimerThread();
        timerThread.start();

    }

    private class TimerThread extends Thread {
        @Override
        public void run() {
            super.run();

            while (isRun) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progress);

                        if (progress < 0) {
                            checkAnswer("0");
                        }

                        progress -= 50;

                    }//run
                });//runnable

                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }//while
        }//run
    }

    private void checkAnswer(String selectedNum) {
        isRun = false;
        bt1.setEnabled(false);
        bt2.setEnabled(false);
        bt3.setEnabled(false);
        bt4.setEnabled(false);

        if (Integer.parseInt(selectedNum) == answer) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_correct_green);
            handler.sendEmptyMessageDelayed(CORRECT, 2000);

        } else {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_wrong_red);
            handler.sendEmptyMessageDelayed(WRONG, 2000);
        }
    }

    private int getRandomNum() {
        Random random = new Random();
        return random.nextInt(8) + 2;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_1) {
            checkAnswer(bt1.getText().toString());
        } else if (id == R.id.bt_2) {
            checkAnswer(bt2.getText().toString());
        } else if (id == R.id.bt_3) {
            checkAnswer(bt3.getText().toString());
        } else if (id == R.id.bt_4) {
            checkAnswer(bt4.getText().toString());
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == CORRECT) {
                nextStage();
            } else if (msg.what == WRONG) {
                Intent intent = new Intent(getApplicationContext(), RestartActivity.class);
                intent.putExtra("recordStage", stageNum);
                startActivity(intent);
                finish();
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
        timerThread = null;
    }
}
