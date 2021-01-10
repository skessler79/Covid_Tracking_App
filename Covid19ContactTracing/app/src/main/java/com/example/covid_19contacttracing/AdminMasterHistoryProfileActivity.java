package com.example.covid_19contacttracing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminMasterHistoryProfileActivity extends AppCompatActivity {

    //Declare View
    TextView historyShop, historyDescription;
    Button button;

    //Declare firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    //Creating Objects
    Customer customer;
    Admin admin = new Admin();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_master_history_profile);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) { // handles the 'back' button
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        historyShop = findViewById(R.id.masterHistoryShop);
        historyDescription = findViewById(R.id.masterHistoryDescription);

        Intent intent = getIntent();

        Bundle bundle = this.getIntent().getExtras();
        String historyId = intent.getStringExtra("historyId");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Getting user info from Firebase
        DocumentReference docRef = fStore.collection("history").document(historyId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    // get attributes
                    String shopName = documentSnapshot.getString("shopName");
//                    String phone = documentSnapshot.getString("phone");
                    String customerName = documentSnapshot.getString("customerName");
                    String getTime = documentSnapshot.get("time").toString();
                    Date currentTime = new Date(Long.valueOf(getTime) * 1000L);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    String dateString = formatter.format(currentTime);


                    historyShop.setText(shopName);
                    historyDescription.setText("visited by " + customerName + " at " + dateString);
                    button = (Button) findViewById(R.id.adminFlagBtn);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openDialog(historyId);
                        }
                    });
                }
            }
        });

        actionBar.setTitle("Visit History");

    }

    public void openDialog (String historyId) {
        FlagDialog flagDialog = new FlagDialog(historyId);
        flagDialog.show(getSupportFragmentManager(), "flag dialog");
    }
}
