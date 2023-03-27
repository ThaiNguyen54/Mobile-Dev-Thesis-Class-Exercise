package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        Intent intent = getIntent();
        String historyCount = intent.getStringExtra("HistoryCount");

        String calculateHistory = readFromFile("CalculateHistory.txt");

        TextView history = (TextView) findViewById(R.id.txt_history);
        history.setText(calculateHistory);
//        history.setText("You've clicked '=' " + historyCount + " times");
    }

    public String readFromFile(String fileName) {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] history = new byte[(int) readFrom.length()];
        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(history);
            return new String(history);
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public void next_exercise(View view) {
        Intent intent = new Intent(this, TestAPI.class);
        startActivity(intent);
    }
}