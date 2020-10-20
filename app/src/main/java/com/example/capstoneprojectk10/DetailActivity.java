package com.example.capstoneprojectk10;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailActivity extends AppCompatActivity {
    private String[] android = {"M Agung Satria A", "Nadya Nurfadillah", "Umi Pertiwi", "Wahyu Nugroho B"};

    // implementasi
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // deklarasi untuk recyclerview
        recyclerView = findViewById(R.id.recyclerview);

        adapter = new RecyclerViewAdapter(this, android); // konstruktor untuk adapter
        linearLayoutManager = new LinearLayoutManager(this); // menampilkan item berupa list

        recyclerView.setLayoutManager(linearLayoutManager); // menset layoutmanager yg telah kita buat
        recyclerView.setAdapter(adapter); // menset adapter-nya
    }


    public void launchSecondActivity(View view) {
        Log.d(LOG_TAG, "Button clicked!");
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

}