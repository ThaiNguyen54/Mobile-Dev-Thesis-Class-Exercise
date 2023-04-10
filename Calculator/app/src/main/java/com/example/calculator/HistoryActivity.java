package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    Gson gson = new Gson();
    ListView lv_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        Intent intent = getIntent();
        String historyCount = intent.getStringExtra("HistoryCount");
        lv_history = findViewById(R.id.lvHistory);


        // Create a thread for reading history operation
        Thread ReadHistoryOperationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                File filePath = getApplicationContext().getFilesDir();
                File readFrom = new File(filePath, "history.json");
                String str_historyOperation;
                byte[] byte_historyOperation = new byte[(int) readFrom.length()];
                try {
                    FileInputStream fileInputStream = new FileInputStream(readFrom);
                    fileInputStream.read(byte_historyOperation);
                    str_historyOperation = new String(byte_historyOperation);

                    Type listType = new TypeToken<List<HistoryOperation>>(){}.getType();
                    ArrayList<HistoryOperation> list_HistoryOperation = gson.fromJson(str_historyOperation, listType);

                    String[] arr_HistoryOperation = new String[list_HistoryOperation.size()];
                    for (int i = 0; i < list_HistoryOperation.size(); i++) {
                        arr_HistoryOperation[i] = list_HistoryOperation.get(i).operation +
                                " = " + list_HistoryOperation.get(i).result;
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(HistoryActivity.this,
                            android.R.layout.simple_list_item_1, arr_HistoryOperation);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lv_history.setAdapter(arrayAdapter);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ReadHistoryOperationThread.start();
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