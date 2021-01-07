package com.example.covid_19contacttracing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AdminProfileFragment extends Fragment implements View.OnClickListener
{
    private static final String EXTRA_TEXT = "text";

    public static AdminProfileFragment createFor(String text) {
        AdminProfileFragment fragment = new AdminProfileFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    // Declaring Views
    TextView adminName;
    Button shopBtn, customerBtn;

    // Creating Customer Object
    Customer customer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_admin_profile, container, false);
        // Initializing Views
        shopBtn = root.findViewById(R.id.adminShopBtn);
        customerBtn = root.findViewById(R.id.adminCustomerBtn);
        adminName = root.findViewById(R.id.adminName);

        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdminShopActivity.class));
            }
        });

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
                    adminName.setText("Welcome, " +fullName);
                }
            }
        });

        customerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Getting user history from Firebase
                DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        customer.showHistory(getContext(), documentSnapshot);
                    }
                });
            }
        });
        return root;
    }

    @Override
    public void onClick(View v)
    {
        scanCode();
    }

    // Check-in with QR code
    private void scanCode()
    {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(AdminProfileFragment.this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);       // TODO : Try to change to QR code types only
        integrator.setPrompt("Place a QR code inside the viewfinder to scan it.");
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
        {
            if(result.getContents() != null)
            {
                customer.checkIn(result);

                // Alert dialog for checking-in
                DocumentReference shopRef = fStore.collection("shops").document(result.getContents());
                shopRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        if(documentSnapshot.exists())
                        {
                            String shopName = documentSnapshot.getString("name");
                            showQRSuccessMessage(shopName);
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(getContext(), "No Results", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showQRSuccessMessage(String shopName)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Successfully checked-in to " + shopName);
        builder.setTitle("Thank you!");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
