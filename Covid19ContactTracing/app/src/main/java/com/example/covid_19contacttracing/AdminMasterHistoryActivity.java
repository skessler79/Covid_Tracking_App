package com.example.covid_19contacttracing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.Query.Direction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdminMasterHistoryActivity extends AppCompatActivity {

    //Declare Firebase
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    //Declare Views
    private ListView customerList;
    private ImageView listImage;

    //Create object
    private Admin admin = new Admin();

    private ArrayList<String> mShopName = new ArrayList<>();
    private ArrayList<String> mCustomerName = new ArrayList<>();
    private ArrayList<String> historyId = new ArrayList<>();
    private ArrayList<Integer> imageChoice = new ArrayList<>();
    private String historyTime[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_master_history);

        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //initializing Views
        customerList = findViewById(R.id.adminMasterHistoryList);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {// handles the 'back' button
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Getting history info from Firebase
        fStore.collection("history").orderBy("time",Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    historyTime = new String[task.getResult().size()];
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String tempHistoryId = document.getId();

                        //get attributes (modify here for ASC or DESC)

                        //get history id
                        historyId.add(tempHistoryId);

                        // get customer name
                        String getCustomerName = document.getData().get("customerName").toString();
                        mCustomerName.add(getCustomerName);


                        //get shop name
                        String getShopName = document.getData().get("shopName").toString();
                        mShopName.add(getShopName);

                        //get shop time
                        String getTime = document.getData().get("time").toString();

                        Date currentTime = new Date(Long.valueOf(getTime) * 1000L);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String dateString = formatter.format(currentTime);
                        historyTime[i]  = dateString;

                        //get determinant for the day and night photo
                        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
                        String hourString = hourFormat.format(currentTime);
                        int hourInt = Integer.parseInt(hourString);

                        if(hourInt >= 6 && hourInt < 18)
                        {
                            imageChoice.add(R.drawable.sun);
                        }
                        else
                        {
                            imageChoice.add(R.drawable.night);
                        }
                        i++;
                    }

                    //set Adapter class to create rows
                    MyAdapter adapter = new MyAdapter(AdminMasterHistoryActivity.this, mShopName, mCustomerName, imageChoice, historyTime);
                    customerList.setAdapter(adapter);
                } else {
                    Log.w("failed", "Error getting documents: ", task.getException());
                }
            }
        });



        customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), AdminMasterHistoryProfileActivity.class);
                // this intent put our 0 index image to another activity
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                // now put title and description to another activity
                intent.putExtra("historyId", historyId.get(position));
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        private Context context;
        private ArrayList<String> rShopName;
        private ArrayList<String> rCustomerName;
        private String[] rHistoryTime;
        private ArrayList<Integer> rImages;
        private String title;
        private String description;

        //Declare Firebase
        FirebaseAuth fAuth;
        FirebaseFirestore fStore;

        MyAdapter (Context context, ArrayList<String> shopName,ArrayList<String> customerName, ArrayList<Integer> images, String[] historyTime ) {
            super(context, R.layout.single_item, R.id.itemName, shopName);
            // Initializing Firebase
            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            this.context = context;
            this.rShopName = shopName;
            this.rCustomerName = customerName;
            this.rImages = images;
            this.rHistoryTime = historyTime;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.single_item, parent, false);

            listImage = row.findViewById(R.id.itemImage);
            TextView myTitle = row.findViewById(R.id.itemName);
            TextView myDescription = row.findViewById(R.id.itemDescription);

            myTitle.setText(rShopName.get(position));
            myDescription.setText(rCustomerName.get(position)+" visited at " + historyTime[position]);
            listImage.setImageResource(rImages.get(position));
            return row;
        }
    }
}
