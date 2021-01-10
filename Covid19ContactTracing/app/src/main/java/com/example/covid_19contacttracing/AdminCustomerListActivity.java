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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCustomerListActivity extends AppCompatActivity {

    //Declare Firebase
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    //Declare Views
    private ListView customerList;
    private ImageView listImage;

    private ArrayList<String> customerName = new ArrayList<>();
    private ArrayList<String> customerStatus = new ArrayList<>();
    private ArrayList<String> userId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_list);

        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //initializing Views
        customerList = findViewById(R.id.adminCustomerListView);

        // handles the 'back' button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Getting shop info from Firebase
        fStore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> tempShopLists = new HashMap<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        //get attributes
                        Boolean isCustomer = document.getData().get("role").toString().equals("Customer"); // check if the user is admin or customer
                        if (isCustomer){
                            customerName.add(document.getData().get("fullName").toString());
                            customerStatus.add(document.getData().get("status").toString());
                            userId.add(document.getId());
                        }
                        else{
                            continue;
                        }
                    }
                    //set Adapter class to create rows
                    MyAdapter adapter = new MyAdapter(AdminCustomerListActivity.this, customerName, customerStatus);
                    customerList.setAdapter(adapter);
                } else {
                    Log.w("failed", "Error getting documents: ", task.getException());
                }
            }
        });

        customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // pass intent to the the AdminCustomerProfileActivity
                Intent intent = new Intent(getApplicationContext(), AdminCustomerProfileActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                intent.putExtra("userId", userId.get(position));
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> rName = new ArrayList<String>();
        ArrayList<String> rStatus = new ArrayList<String>();

        MyAdapter (Context context, ArrayList<String> name,ArrayList<String> status) {
            super(context, R.layout.single_item, R.id.itemName, name);
            this.context = context;
            this.rName = name;
            this.rStatus = status;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) { // handles the single item
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.single_item, parent, false);

            listImage = row.findViewById(R.id.itemImage);
            TextView myTitle = row.findViewById(R.id.itemName);
            TextView myDescription = row.findViewById(R.id.itemDescription);

            myTitle.setText(rName.get(position));
            myDescription.setText(rStatus.get(position));
            listImage.setImageResource(R.drawable.admin_user_account_icon);
            return row;
        }
    }
}
