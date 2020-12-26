package com.example.covid_19contacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    // Connecting to Firebase Authentication and Firestore Database
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;

    // Declaring Views variables
    EditText fullName, email;
    Spinner statesSpinner;
    Button saveBtn;

    String userID;
    String selectedState;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        // Initializing Views
        fullName = findViewById(R.id.fullName);
        statesSpinner = findViewById(R.id.stateSpinner);
        email = findViewById(R.id.emailAddress);
        saveBtn = findViewById(R.id.saveBtn);

        // Setting up states spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.statesArray, R.layout.states_spinner_layout);
        adapter.setDropDownViewResource(R.layout.states_spinner_dropdown_layout);
        statesSpinner.setAdapter(adapter);
        statesSpinner.setOnItemSelectedListener(this);

        // Initializing firebase variables
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        // Connecting to Firestore Database for users
        DocumentReference docRef = fStore.collection("users").document(userID);

        // Save button event listener
        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!fullName.getText().toString().isEmpty() && !email.getText().toString().isEmpty())
                {
                    String userEmail = email.getText().toString();

                    // Check if email is correct format
                    if(userEmail.trim().matches(emailPattern))
                    {
                        String name = fullName.getText().toString();
                        Map<String, Object> user = new HashMap<>();
                        user.put("fullName", name);
                        user.put("currentState", selectedState);
                        user.put("email", userEmail);

                        // On complete event listener for Firestore
                        docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(AddDetails.this, "Data is not inserted.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(AddDetails.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
    {
        selectedState = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }
}