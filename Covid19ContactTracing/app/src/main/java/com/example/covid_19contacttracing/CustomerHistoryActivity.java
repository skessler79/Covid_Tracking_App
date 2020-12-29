package com.example.covid_19contacttracing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class CustomerHistoryActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    RecyclerView.Adapter programAdapter;
    RecyclerView.LayoutManager layoutManager;
    String[] programNameList = {"C", "C++", "Java"};
    String[] programDescriptionList = {"C Description", "C++ Description", "Java Description"};
    int[] programImages = {R.drawable.sun, R.drawable.night, R.drawable.sun};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history);

        recyclerView = findViewById(R.id.history_rv_program);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        programAdapter = new HistoryProgramAdapter(this, programNameList, programDescriptionList, programImages);
        recyclerView.setAdapter(programAdapter);
    }
}