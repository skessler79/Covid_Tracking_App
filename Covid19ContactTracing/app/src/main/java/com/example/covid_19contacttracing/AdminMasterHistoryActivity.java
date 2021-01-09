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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminMasterHistoryActivity extends AppCompatActivity {

    //Declare Firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    //Declare Views
    ListView customerList;
    ImageView listImage;

    //Create object
    Admin admin = new Admin();

    ArrayList<String> mShopName = new ArrayList<String>();
    ArrayList<String> mCustomerName = new ArrayList<String>();
    ArrayList<String> historyId = new ArrayList<String>();
    ArrayList<String> historyTime = new ArrayList<String>();
    ArrayList<Integer> imageChoice = new ArrayList<>();
    List<Map<String, Object>> shopLists = new ArrayList<Map<String, Object>>();

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
        fStore.collection("history").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> tempShopLists = new HashMap<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String tempHistoryId = document.getId();

                        //get attributes (modify here for ASC or DESC)

                        //get history id
                        historyId.add(tempHistoryId);

                        // get customer name
                        String getCustomerId = document.getData().get("customer").toString();
//                        String getCustomerName = admin.getQueryById("users",getCustomerId).toString();
//                        Log.d("success", "customerData==>"+ getCustomerName);
                        mCustomerName.add(getCustomerId);


                        //get shop name
                        String getShopId = document.getData().get("shop").toString();
//                        String getShopName = admin.getQueryById("shops",getShopId).get("name").toString();
                        mShopName.add(getShopId);

                        //get shop time
                        String getTime = document.getData().get("time").toString();

                        Date currentTime = new Date(Long.valueOf(getTime) * 1000L);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z", Locale.getDefault());
                        String dateString = formatter.format(currentTime);
                        historyTime.add(dateString);

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
                    }
                    for(int i = 0; i < mCustomerName.size(); i++){
                        DocumentReference docRef1 = fStore.collection("users").document(mCustomerName.get(i));
                        int finalI = i;
                        docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                        {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot)
                            {
                                if(documentSnapshot.exists()){
                                    mCustomerName.set(finalI,documentSnapshot.getString("fullName"));
                                }
                            }
                        });
                        DocumentReference docRef2 = fStore.collection("shops").document(mShopName.get(i));

                        docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                        {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot)
                            {
                                if(documentSnapshot.exists()){
                                    mShopName.set(finalI,documentSnapshot.getString("name"));
                                }
                            }
                        });
                    }

                    Log.d("success", "customerName==>"+ mCustomerName);

                    //set Adapter class to create rows
                    MyAdapter adapter = new MyAdapter(AdminMasterHistoryActivity.this, mShopName, mCustomerName, imageChoice);
                    customerList.setAdapter(adapter);
                    Log.d("success", "historyId==>"+ historyId);
                } else {
                    //TODO: more proper error handling
                    Log.d("success", "Error getting documents: ", task.getException());
                }
            }
        });



        customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), AdminShopProfileActivity.class);
                // this intent put our 0 index image to another activity
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                // now put title and description to another activity
                intent.putExtra("shopId", historyId.get(position));
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> rName = new ArrayList<String>();
        ArrayList<String> rStatus = new ArrayList<String>();
        ArrayList<Integer> rImages;
        private String title;
        private String description;

        //Declare Firebase
        FirebaseAuth fAuth;
        FirebaseFirestore fStore;

        MyAdapter (Context context, ArrayList<String> name,ArrayList<String> customerName, ArrayList<Integer> images) {
            super(context, R.layout.single_item, R.id.ItemName, name);
            // Initializing Firebase
            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            Log.d("success", "shopName==>"+ name);
            Log.d("success", "customerName==>"+ customerName);
            this.context = context;
            this.rName = name;
            this.rStatus = customerName;
            this.rImages = images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.single_item, parent, false);

            listImage = row.findViewById(R.id.itemImage);
            TextView myTitle = row.findViewById(R.id.ItemName);
            TextView myDescription = row.findViewById(R.id.itemDescription);

            myTitle.setText(rName.get(position));
            myDescription.setText(rStatus.get(position));
            listImage.setImageResource(rImages.get(position));
            return row;
        }
    }
}
