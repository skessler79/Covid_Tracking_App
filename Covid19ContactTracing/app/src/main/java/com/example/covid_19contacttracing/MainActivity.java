package com.example.covid_19contacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity
{
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    // Declaring Views
    TextView pName, pEmail, pPhone, pState;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Views
        pName = findViewById(R.id.profileFullName);
        pEmail = findViewById(R.id.profileEmail);
        pPhone = findViewById(R.id.profilePhone);
        pState = findViewById(R.id.profileState);
        toolbar = findViewById(R.id.toolbar);

        // Initializing toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Getting user info from Firebase
        DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    String fullName = documentSnapshot.getString("fullName");
                    pName.setText(fullName);
                    pEmail.setText(documentSnapshot.getString("email"));
                    pPhone.setText(fAuth.getCurrentUser().getPhoneNumber());
                    pState.setText(documentSnapshot.getString("currentState"));
                }
            }
        });
    }

    // Overriding toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Check menu item selected in toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        // Logout
        if(item.getItemId() == R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}