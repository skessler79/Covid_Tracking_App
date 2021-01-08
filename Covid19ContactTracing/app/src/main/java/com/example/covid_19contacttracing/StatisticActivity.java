package com.example.covid_19contacttracing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private TextView mTextViewResult;
    private RequestQueue mQueue;

    private final String url = "https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/total";
    private final String urlMalaysia = "https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/total?country=Malaysia";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        mTextViewResult = findViewById(R.id.text_view_result);


        mQueue = Volley.newRequestQueue(this);
        jsonParse(url);
        try
        {
            Thread.sleep(500);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        jsonParse(urlMalaysia);
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
                    mTextViewResult.append(location + " Covid-19 Statistics : \n\n");
                    mTextViewResult.append("Total cases : " + String.valueOf(confirmed) + "\n" +
                                           "Total recoveries : " + String.valueOf(recovered) + "\n" +
                                           "Total deaths : " + String.valueOf(deaths) + "\n\n");

                } catch (Exception e)
                {
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

//    private void jsonParse()
//    {
//        String url = "https://api.myjson.com/bins/kp9wz";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
//        {
//            @Override
//            public void onResponse(JSONObject response)
//            {
//                try
//                {
//                    JSONArray jsonArray = response.getJSONArray("employees");
//                    for(int i = 0; i < jsonArray.length(); ++i)
//                    {
//                        JSONObject employee = jsonArray.getJSONObject(i);
//
//                        String firstName = employee.getString("firstname");
//                        int age = employee.getInt("age");
//                        String mail = employee.getString("mail");
//
//                        mTextViewResult.append(firstName + " , " + String.valueOf(age) + " , " + mail + "\n\n");
//                    }
//                } catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener()
//        {
//            @Override
//            public void onErrorResponse(VolleyError error)
//            {
//                error.printStackTrace();
//            }
//        })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//            HashMap<String, String> headers = new HashMap<String, String>();
//            //headers.put("Content-Type", "application/json");
//            headers.put("key", "Value");
//            return headers;
//        };
//        mQueue.add(request);
//    }

}

