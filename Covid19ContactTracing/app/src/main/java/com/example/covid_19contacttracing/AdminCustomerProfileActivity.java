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
    private TextView customerName, customerPhoneNumber, customerEmail, customerState, customerStatus;

    //Declare firebase
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_profile);

        // handles the 'back' button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        customerName = findViewById(R.id.display_name);
        customerPhoneNumber = findViewById(R.id.phone_number);
        customerEmail = findViewById(R.id.email_address);
        customerState = findViewById(R.id.state);
        customerStatus = findViewById(R.id.status);

        //get intent
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
                    String phone = documentSnapshot.getString("phone");
                    String email = documentSnapshot.getString("email");
                    String state = documentSnapshot.getString("currentState");
                    String status = documentSnapshot.getString("status");

                    customerName.setText(name);
                    customerPhoneNumber.setText(phone);
                    customerEmail.setText(email);
                    customerState.setText(state);
                    customerStatus.setText(status);
                }
            }
        });



        actionBar.setTitle("Customer Profile");

    }
}
