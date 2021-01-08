package com.example.covid_19contacttracing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    TextView shopName, shopStatus;

    //Declare firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    //Creating Objects
    Shop shop;
    Admin admin = new Admin();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_shop_profile);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {// handles the 'back' button
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        qrCodeImage = findViewById(R.id.qrCodeImage);
        shopName = findViewById(R.id.shopName);
        shopStatus = findViewById(R.id.shopStatus);

        Intent intent = getIntent();

        Bundle bundle = this.getIntent().getExtras();
        String shopId = intent.getStringExtra("shopId");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

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

                    shop = new Shop(name, phone, ShopStatus.valueOf(status), manager);

                    shopName.setText(shop.getName());
                    shopStatus.setText(shop.getStatus().name());

                    try{
                        qrCodeImage.setImageBitmap(admin.generateQR("COVIDTRACE-"+shopId)); // add identifier to shop id and pass to generator
                    }catch(Exception e){
                        Log.e("Error!", "onSuccess: ",  e);
                        e.printStackTrace();
                    }

                }
            }
        });

        actionBar.setTitle("Shop Details");

    }
}
