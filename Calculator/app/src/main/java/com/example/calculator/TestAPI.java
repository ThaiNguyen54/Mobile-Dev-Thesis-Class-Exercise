package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestAPI extends AppCompatActivity {
    private Button btnGetData;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);
        Intent intent = getIntent();
        btnGetData = findViewById(R.id.btnGetData);
        listView = findViewById(R.id.listviewData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods methods = RetrofitClient.getRetrofitInstance().create(Methods.class);
                Call <Model> call = methods.getAllData();
                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        ArrayList <Model.data> data = response.body().getData();
                        String[] names = new String[data.size()];
                        for (int i = 0; i < data.size(); i++) {
                            names[i] = "Id: " + data.get(i).getId() + "\nName: " +
                                    data.get(i).getFirst_name() + " " +
                                    data.get(i).getLast_name() + "\nEmail"  +
                                    data.get(i).getEmail();
                        }
                        listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, names));
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}