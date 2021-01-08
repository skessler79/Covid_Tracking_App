package com.example.covid_19contacttracing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StatisticActivity extends AppCompatActivity
{
    // Declaring Views
    private TextView stat_card_cases, stat_card_deaths, stat_card_recovered, stat_card_location, stat_card_updated;
    private Button malaysia_button, global_button;
    private ImageView outbreak_map;

    boolean selected = false;

    private final String urlGlobal = "https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/total";
    private final String urlMalaysia = "https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/total?country=Malaysia";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initializing views
        stat_card_cases = findViewById(R.id.stat_card_cases);
        stat_card_deaths = findViewById(R.id.stat_card_deaths);
        stat_card_recovered = findViewById(R.id.stat_card_recovered);
        stat_card_location = findViewById(R.id.stat_card_location);
        stat_card_updated = findViewById(R.id.stat_card_updated);
        outbreak_map = findViewById(R.id.outbreak_map);

        malaysia_button = findViewById(R.id.malaysia_button);
        global_button = findViewById(R.id.global_button);

        jsonParse(urlMalaysia);

        malaysia_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(selected)
                {
                    malaysia_button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_scan_background));
                    global_button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_user_history_background));
                    malaysia_button.setTextColor(Color.parseColor("#000000"));
                    global_button.setTextColor(Color.parseColor("#FFFFFF"));


                    jsonParse(urlMalaysia);
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
                    global_button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_scan_background));
                    malaysia_button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_user_history_background));
                    malaysia_button.setTextColor(Color.parseColor("#FFFFFF"));
                    global_button.setTextColor(Color.parseColor("#000000"));

                    jsonParse(urlGlobal);
                    selected = true;
                }
            }
        });
    }

    public void jsonParse(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONObject data = response.getJSONObject("data");
                    Long confirmed = data.getLong("confirmed");
                    Long deaths = data.getLong("deaths");
                    Long recovered = data.getLong("recovered");
                    String location = data.getString("location");
                    String updated = data.getString("lastReported");

                    stat_card_cases.setText(String.format("%,d", confirmed));
                    stat_card_deaths.setText(String.format("%,d", deaths));
                    stat_card_recovered.setText(String.format("%,d", recovered));
                    stat_card_location.setText(location);
                    stat_card_updated.setText(updated.substring(0, 10));

                    if(location.equals("Malaysia"))
                    {
                        outbreak_map.setImageResource(R.drawable.outbreak_map_malaysia);
                    }
                    else
                    {
                        outbreak_map.setImageResource(R.drawable.outbreak_map_world);
                    }


                } catch (Exception e)
                {
                    Toast.makeText(StatisticActivity.this, "TEST", Toast.LENGTH_SHORT).show();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-rapidapi-key", "4c6e430bbbmshaeb5eea1acc8352p118f90jsn06ee47dbd32e");
                params.put("x-rapidapi-host", "covid-19-coronavirus-statistics.p.rapidapi.com");

                return params;
            }
        };
        queue.add(request);

    }

}

