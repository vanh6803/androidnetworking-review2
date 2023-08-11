package com.example.review2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcv;
    private ModelAdapter adapter;
    private List<Model> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcv = findViewById(R.id.rcv);

        list = new ArrayList<>();
        adapter = new ModelAdapter(this);
        rcv.setAdapter(adapter);

        getAllData();

    }

    private void getAllData() {
        ApiService.instance.getAll().enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                Log.d("AAA", response.body().toString());
                list = response.body();
                adapter.setList(list);
                rcv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Log.d("BBB", t.getMessage());
            }
        });
    }
}