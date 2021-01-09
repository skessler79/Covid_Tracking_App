package com.example.covid_19contacttracing;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StatisticsFragment extends Fragment
{
    private static final String EXTRA_TEXT = "text";

    public static StatisticsFragment createFor(String text)
    {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    // Declaring Views
    private TextView stat_card_cases, stat_card_deaths, stat_card_recovered, stat_card_location, stat_card_updated;
    private Button malaysia_button, global_button;
    private ImageView outbreak_map;
    private ProgressBar progress_bar2, progress_bar3, progress_bar4, progress_bar5, progress_bar6;

    boolean selected = false;
    Stats malaysiaStats;
    Stats globalStats;

    private final String urlGlobal = "https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/total";
    private final String urlMalaysia = "https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/total?country=Malaysia";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_statistics, container, false);

        // Initializing views
        stat_card_cases = root.findViewById(R.id.stat_card_cases);
        stat_card_deaths = root.findViewById(R.id.stat_card_deaths);
        stat_card_recovered = root.findViewById(R.id.stat_card_recovered);
        stat_card_location = root.findViewById(R.id.stat_card_location);
        stat_card_updated = root.findViewById(R.id.stat_card_updated);
        outbreak_map = root.findViewById(R.id.outbreak_map);
        progress_bar2 = root.findViewById(R.id.progressBar2);
        progress_bar3 = root.findViewById(R.id.progressBar3);
        progress_bar4 = root.findViewById(R.id.progressBar4);
        progress_bar5 = root.findViewById(R.id.progressBar5);
        progress_bar6 = root.findViewById(R.id.progressBar6);

        malaysia_button = root.findViewById(R.id.malaysia_button);
        global_button = root.findViewById(R.id.global_button);
        malaysia_button.setVisibility(View.INVISIBLE);
        global_button.setVisibility(View.INVISIBLE);

        malaysiaStats = new Stats();
        globalStats = new Stats();

        jsonParse();

        malaysia_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(selected)
                {
                    malaysia_button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_statistic_selected));
                    global_button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_statistic_unselected));
                    malaysia_button.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_blue));
                    global_button.setTextColor(Color.parseColor("#000000"));

                    stat_card_cases.setText(String.format("%,d", malaysiaStats.confirmed));
                    stat_card_deaths.setText(String.format("%,d", malaysiaStats.deaths));
                    stat_card_recovered.setText(String.format("%,d", malaysiaStats.recovered));
                    stat_card_location.setText(malaysiaStats.location);
                    stat_card_updated.setText(malaysiaStats.updated.substring(0, 10));
                    outbreak_map.setImageResource(R.drawable.outbreak_map_malaysia);

                    selected = false;
                }
            }
        });

        global_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!selected)
                {
                    global_button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_statistic_selected));
                    malaysia_button.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_statistic_unselected));
                    malaysia_button.setTextColor(Color.parseColor("#000000"));
                    global_button.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_blue));

                    stat_card_cases.setText(String.format("%,d", globalStats.confirmed));
                    stat_card_deaths.setText(String.format("%,d", globalStats.deaths));
                    stat_card_recovered.setText(String.format("%,d", globalStats.recovered));
                    stat_card_location.setText(globalStats.location);
                    stat_card_updated.setText(globalStats.updated.substring(0, 10));
                    outbreak_map.setImageResource(R.drawable.outbreak_map_world);

                    selected = true;
                }
            }
        });

        return root;
    }

    public void jsonParse() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest requestMalaysia = new JsonObjectRequest(Request.Method.GET, urlGlobal, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONObject data = response.getJSONObject("data");
                    globalStats.confirmed = data.getLong("confirmed");
                    globalStats.deaths = data.getLong("deaths");
                    globalStats.recovered = data.getLong("recovered");
                    globalStats.location = data.getString("location");
                    globalStats.updated = data.getString("lastReported");

                } catch (Exception e)
                {
                    Toast.makeText(getContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-rapidapi-key", "4c6e430bbbmshaeb5eea1acc8352p118f90jsn06ee47dbd32e");
                params.put("x-rapidapi-host", "covid-19-coronavirus-statistics.p.rapidapi.com");

                return params;
            }
        };
        queue.add(requestMalaysia);

        JsonObjectRequest requestGlobal = new JsonObjectRequest(Request.Method.GET, urlMalaysia, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONObject data = response.getJSONObject("data");
                    malaysiaStats.confirmed = data.getLong("confirmed");
                    malaysiaStats.deaths = data.getLong("deaths");
                    malaysiaStats.recovered = data.getLong("recovered");
                    malaysiaStats.location = data.getString("location");
                    malaysiaStats.updated = data.getString("lastReported");

                    // Show stats for Malaysia upon completion of fetching data from API
                    stat_card_cases.setText(String.format("%,d", malaysiaStats.confirmed));
                    stat_card_deaths.setText(String.format("%,d", malaysiaStats.deaths));
                    stat_card_recovered.setText(String.format("%,d", malaysiaStats.recovered));
                    stat_card_location.setText(malaysiaStats.location);
                    stat_card_updated.setText(malaysiaStats.updated.substring(0, 10));

                    // Enable the buttons and remove the progress bars
                    malaysia_button.setVisibility(View.VISIBLE);
                    global_button.setVisibility(View.VISIBLE);
                    progress_bar2.setVisibility(View.GONE);
                    progress_bar3.setVisibility(View.GONE);
                    progress_bar4.setVisibility(View.GONE);
                    progress_bar5.setVisibility(View.GONE);
                    progress_bar6.setVisibility(View.GONE);

                } catch (Exception e)
                {
                    Toast.makeText(getContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-rapidapi-key", "4c6e430bbbmshaeb5eea1acc8352p118f90jsn06ee47dbd32e");
                params.put("x-rapidapi-host", "covid-19-coronavirus-statistics.p.rapidapi.com");

                return params;
            }
        };
        queue.add(requestGlobal);

    }
}

class Stats
{
    public Long confirmed;
    public Long deaths;
    public Long recovered;
    public String location;
    public String updated;
}