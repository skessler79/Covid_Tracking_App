package com.example.covid_19contacttracing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminCustomerProfileActivity extends AppCompatActivity {

    //Declare View
//    ImageView customerImage;
    TextView customerName, customerStatus;

    //Declare firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    //Creating Objects
    Customer customer;
    Admin admin = new Admin();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_profile);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) { // handles the 'back' button
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

//        customerImage = findViewById(R.id.customerImage);
        customerName = findViewById(R.id.customerName);
        customerStatus = findViewById(R.id.customerStatus);

        Intent intent = getIntent();

        Bundle bundle = this.getIntent().getExtras();
        String userId = intent.getStringExtra("userId");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Getting user info from Firebase
        DocumentReference docRef = fStore.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    // get attributes
                    String name = documentSnapshot.getString("fullName");
//                    String phone = documentSnapshot.getString("phone");
                    String email = documentSnapshot.getString("email");
                    String status = documentSnapshot.getString("status");

                    customer = new Customer(name, "0123456789", email, CustStatus.valueOf(status));

                    customerName.setText(customer.getName());
                    customerStatus.setText(customer.getStatus().name());
                }
            }
        });



        actionBar.setTitle("Customer Profile");

    }
}
