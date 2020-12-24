package com.example.covid_19contacttracing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddDetails extends AppCompatActivity
{
    // Connecting to Firebase Authentication and Firestore Database
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;

    // Declaring Views variables
    EditText firstName, lastName, email;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        // Initializing Views
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.emailAddress);
        saveBtn = findViewById(R.id.saveBtn);

        // Initializing firebase variables
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Save button event listener
        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty() && !email.getText().toString().isEmpty())
                {
                    
                }
                else
                {
                    Toast.makeText(AddDetails.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}