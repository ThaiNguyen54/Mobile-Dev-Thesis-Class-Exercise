package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        Intent intent = getIntent();
        String historyCount = intent.getStringExtra("HistoryCount");

//        String calculateHistory = readFromFile("CalculateHistory.txt");

        TextView history = (TextView) findViewById(R.id.txt_history);
//        history.setText(calculateHistory);
//        history.setText("You've clicked '=' " + historyCount + " times");

        String json = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    new File(getApplicationContext().getFilesDir(), "history.json")));

            String line = bufferedReader.readLine();
            while (line != null) {
                json += line;
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        HistoryOperation[] historyOperation = gson.fromJson(json, HistoryOperation[].class);
        int count = historyOperation.length;
        Log.d("COUNT", (String)  Integer.toString(count));
//        history.setText(historyOperation.getOperation() + " = " + historyOperation.getResult());

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