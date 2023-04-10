package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

//import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result, solution;
    MaterialButton buttonC, buttonOpenBr, buttonCloseBr, buttonDivide;
    MaterialButton button7, button8, button9, buttonAsterisk;
    MaterialButton button4, button5, button6, buttonPlus;
    MaterialButton button1, button2, button3, buttonMinus;
    MaterialButton buttonAC, button0, buttonDot, buttonEqual;
    Gson gson = new Gson();

    int historyCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
        solution = findViewById(R.id.solution);

        assignID(buttonC, R.id.bnt_c);
        assignID(buttonOpenBr, R.id.bnt_open_bracket);
        assignID(buttonCloseBr, R.id.bnt_close_bracket);
        assignID(buttonDivide, R.id.btn_divide);
        assignID(button7, R.id.bnt_7);
        assignID(button8, R.id.bnt_8);
        assignID(button9, R.id.bnt_9);
        assignID(buttonAsterisk, R.id.bnt_asterisk);
        assignID(button4, R.id.bnt_4);
        assignID(button5, R.id.bnt_5);
        assignID(button6, R.id.bnt_6);
        assignID(buttonPlus, R.id.btn_plus);
        assignID(button1, R.id.bnt_1);
        assignID(button2, R.id.bnt_2);
        assignID(button3, R.id.bnt_3);
        assignID(buttonMinus, R.id.btn_minus);
        assignID(buttonAC, R.id.bnt_AC);
        assignID(button0, R.id.bnt_0);
        assignID(buttonDot, R.id.bnt_dot);
        assignID(buttonEqual, R.id.btn_equal);
    }

    void assignID(MaterialButton bnt, int id) {
        bnt = findViewById(id);
        bnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();
        String calculateData = solution.getText().toString();

        if(buttonText.equals("AC")){
            solution.setText("");
            result.setText("0");
            return;
        }
        if(buttonText.equals("=")){
//            solution.setText(result.getText());
            String finalResult = getResult(calculateData);

            if(!finalResult.equals("Error")){
                result.setText(finalResult);
            }
            historyCount += 1;

            // Starting to save history operation
            Thread SaveHistoryOperation = new Thread(new Runnable() {
                @Override
                public void run() {
                    File path = getApplicationContext().getFilesDir();

                    // Check if history.json exist
                    File historyFile = new File(path + "/history.json");
                    boolean isHistoryFileExist = historyFile.exists();
                    if (isHistoryFileExist == false) {
                        try{
                            FileOutputStream writer = new FileOutputStream(new File(path, "history.json"));
                            HistoryOperation historyOperation = new HistoryOperation(solution.getText().toString(), result.getText().toString());
                            List<HistoryOperation> list_HistoryOperation = new ArrayList<>();
                            list_HistoryOperation.add(historyOperation);
                            String str_HistoryOperationJson = gson.toJson(list_HistoryOperation);
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(new File(path, "history.json"));
                                fileOutputStream.write(str_HistoryOperationJson.getBytes());
                                fileOutputStream.close();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "wrote to file: history.json" , Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Read history.json file
                        File readFrom = new File(path, "history.json");
                        String JSON_str_historyOperationList;
                        byte[] byte_historyOperationList = new byte[(int) readFrom.length()];
                        Type listType = new TypeToken<List<HistoryOperation>>(){}.getType();

                        try {
                            FileInputStream inputStream = new FileInputStream(readFrom);
                            inputStream.read(byte_historyOperationList);
                            JSON_str_historyOperationList = new String(byte_historyOperationList);
                            ArrayList<HistoryOperation> historyOperationArrayList = gson.fromJson(JSON_str_historyOperationList, listType);
                            int a = historyOperationArrayList.size();
                            if (historyOperationArrayList.size() >= 10) {
                                historyOperationArrayList.remove(0);
                            }

                            // After reading the history.json,
                            // then we append a new operation the history operation list for saving
                            HistoryOperation historyOperation = new HistoryOperation(solution.getText().toString(), result.getText().toString());
                            historyOperationArrayList.add(historyOperation);
                            String historyOperationString = gson.toJson(historyOperationArrayList);
                            FileOutputStream writer = new FileOutputStream(new File(path, "history.json"));
                            writer.write(historyOperationString.getBytes());
                            writer.close();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "wrote to file: history.json" , Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            SaveHistoryOperation.start();
            return;
        }
        if(buttonText.equals("C")){
            calculateData = calculateData.substring(0, calculateData.length()-1);
        } else {
            calculateData = calculateData + buttonText;
        }
        solution.setText(calculateData);
    }

    public void writeToFile(String fileName, String content) {
        File path = getApplicationContext().getFilesDir();
        try{
            FileOutputStream writer = new FileOutputStream(new File(path, fileName));
            writer.write(content.getBytes());
            writer.close();
            Toast.makeText(getApplicationContext(), "wrote to file: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getResult(String data){
        try{
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();
            if(finalResult.endsWith(".0")){
                finalResult = finalResult.replace(".0", "");
            }
            return finalResult;
        } catch (Exception e) {
            return "Error";
        }
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putExtra("HistoryCount", String.valueOf(historyCount));
        startActivity(intent);
    }

    public void next_exercise(View view) {
        Intent intent = new Intent(this, TestAPI.class);
        startActivity(intent);
    }
}