package com.unknown.speedtimestables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RestartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restart);

        String stage;
        TextView tvRecord, tvBestRecord;
        tvBestRecord = findViewById(R.id.tv_best_record_restart);
        tvRecord = findViewById(R.id.tv_record_restart);

        Intent intent = getIntent();
        int recordStage = intent.getIntExtra("recordStage", 1);
        stage = "STAGE " + recordStage;
        tvRecord.setText(stage);

        SharedPreferences pref = getSharedPreferences("prefData", MODE_PRIVATE);
        int bestRecordStage = pref.getInt("bestRecordStage", 1);
        stage = "STAGE " + bestRecordStage;
        tvBestRecord.setText(stage);

        if (recordStage > bestRecordStage || bestRecordStage == 1) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("bestRecordStage", recordStage);
            editor.apply();
        }

    }

    public void restartGame(View view) {
        startActivity(new Intent(this, GameActivity.class));
        finish();
    }
}
