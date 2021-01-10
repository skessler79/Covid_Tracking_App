package com.example.covid_19contacttracing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminShopProfileActivity extends AppCompatActivity {

    //Declare View
    ImageView qrCodeImage;
    TextView shopName, phoneNumber, managerName, shopStatus;

    //Declare firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    //Creating Objects
    Admin admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_shop_profile);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {// handles the 'back' button
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Initialize admin
        admin = new Admin();

        // Initialize firestore
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        qrCodeImage = findViewById(R.id.qrCodeImage);
        shopName = findViewById(R.id.shop_name);
        phoneNumber = findViewById(R.id.phone_number);
        managerName = findViewById(R.id.manager_name);
        shopStatus = findViewById(R.id.status);

        //get data that got passed into this activity
        Intent intent = getIntent();
        String shopId = intent.getStringExtra("shopId");

        // Getting user info from Firebase
        DocumentReference docRef = fStore.collection("shops").document(shopId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    // get attributes
                    String name = documentSnapshot.getString("name");
                    String phone = documentSnapshot.getString("phone");
                    String manager = documentSnapshot.getString("manager");
                    String status = documentSnapshot.getString("status");

                    // set the text accordingly
                    shopName.setText(name);
                    phoneNumber.setText(phone);
                    managerName.setText(manager);
                    shopStatus.setText(status);

                    try{
                        qrCodeImage.setImageBitmap(admin.generateQR(shopId)); // generates ar code
                        qrCodeImage.setVisibility(View.VISIBLE); // set the qrcode as image
                    }catch(Exception e){
                        Log.e("Error!", "error: ",  e);
                        e.printStackTrace();
                    }
                }
            }
        });

        actionBar.setTitle("Shop Details");

    }
}
