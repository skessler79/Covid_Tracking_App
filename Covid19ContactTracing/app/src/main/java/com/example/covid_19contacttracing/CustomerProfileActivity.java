package com.example.covid_19contacttracing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CustomerProfileActivity extends AppCompatActivity implements View.OnClickListener
{
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    // Declaring Views
    TextView pName, pPhone, pEmail, pStatus;
    Button checkInBtn, historyBtn;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        // Initializing Views
        pName = findViewById(R.id.userName);
        pPhone = findViewById(R.id.userPhone);
        pEmail = findViewById(R.id.userEmail);
        pStatus = findViewById(R.id.userStatus);
        checkInBtn = findViewById(R.id.checkInBtn);
        historyBtn = findViewById(R.id.historyBtn);

        checkInBtn.setOnClickListener(this);

        // Initializing toolbar
        toolbar = findViewById(R.id.userProfileToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

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
                    pStatus.setText(documentSnapshot.getString("status"));
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        scanCode();
    }

    private void scanCode()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);       // TODO : Try to change to QR code types only
        integrator.setPrompt("Scanner Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
        {
            if(result.getContents() != null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Scanning Results");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        scanCode();
                    }
                }).setNegativeButton("Finish", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else
            {
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
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