package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        Intent intent = getIntent();
        String historyCount = intent.getStringExtra("HistoryCount");

        TextView history = (TextView) findViewById(R.id.txt_history);
        history.setText("You've clicked '=' " + historyCount + " times");
    }
}