package com.example.covid_19contacttracing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class IntroductoryActivity extends AppCompatActivity
{
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    // Declaring Views
    TextView nameSplash;
    ImageView logoSplash, imgSplash;
    LottieAnimationView lottieAnimationView;

    int userStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Initializing animated splash screen views
        logoSplash = findViewById(R.id.logoSplash);
        nameSplash = findViewById(R.id.nameSplash);
        imgSplash = findViewById(R.id.imgSplash);
        lottieAnimationView = findViewById(R.id.lottieSplash);

        // Getting screen dimensions for animation
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        imgSplash.animate().translationY(height * -1.5F).setDuration(1000).setStartDelay(3600);
        logoSplash.animate().translationY(height).setDuration(1000).setStartDelay(3600);
        nameSplash.animate().translationY(height).setDuration(1000).setStartDelay(3600);
        lottieAnimationView.animate().translationY(height).setDuration(1000).setStartDelay(3600);

        checkUserProfile();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                switch(userStatus)
                {
                    case 0:
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        finish();
                        break;

                    case 1:
                        startActivity(new Intent(getApplicationContext(), UserAddDetailsActivity.class));
                        finish();
                        break;

                    case 2:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        break;
                }

            }
        }, 4400);
    }


    private void checkUserProfile()
    {
        if(fAuth.getCurrentUser() != null)
        {
            DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
            {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot)
                {
                    // If user has already registered previously
                    if(documentSnapshot.exists())
                    {
                        userStatus = 2;
                    }
                    // If user is a new user
                    else
                    {
                        userStatus = 1;
                    }
                }
            });
        }
        else
        {
            userStatus = 0;
        }
    }
}