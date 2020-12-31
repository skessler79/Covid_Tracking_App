package com.example.covid_19contacttracing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CustomerHistoryActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    RecyclerView.Adapter programAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<String> programNameList;
    ArrayList<String> programDescriptionList;
    ArrayList<Integer> programImages;
    private SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history);

        programNameList = new ArrayList<>();
        programDescriptionList = new ArrayList<>();
        programImages = new ArrayList<>();

        ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("list");
        for(int i = 0; i < list.size(); ++i)
        {
            String name = list.get(i).get("shop");
            String time = list.get(i).get("time");
            programNameList.add(name);
            Date currentTime = new Date(Long.valueOf(time) * 1000L);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z", Locale.getDefault());
            String dateString = formatter.format(currentTime);
            programDescriptionList.add(dateString);

            SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
            String hourString = hourFormat.format(currentTime);
            int hourInt = Integer.valueOf(hourString);

            if(hourInt >= 6 && hourInt < 18)
            {
                programImages.add(R.drawable.sun);
            }
            else
            {
                programImages.add(R.drawable.night);
            }
        }

        recyclerView = findViewById(R.id.history_rv_program);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        programAdapter = new HistoryProgramAdapter(this, programNameList, programDescriptionList, programImages);
        recyclerView.setAdapter(programAdapter);
    }
}