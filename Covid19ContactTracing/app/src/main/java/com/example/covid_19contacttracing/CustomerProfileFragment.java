package com.example.covid_19contacttracing;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CustomerProfileFragment extends Fragment implements View.OnClickListener
{
    private static final String EXTRA_TEXT = "text";

    public static CustomerProfileFragment createFor(String text) {
        CustomerProfileFragment fragment = new CustomerProfileFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    // Declaring Views
    TextView pName, pPhone, pEmail, pStatus;
    Button checkInBtn, historyBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_customer_profile, container, false);
        // Initializing Views
        pName = root.findViewById(R.id.userName);
        pPhone = root.findViewById(R.id.userPhone);
        pEmail = root.findViewById(R.id.userEmail);
        pStatus = root.findViewById(R.id.userStatus);
        checkInBtn = root.findViewById(R.id.checkInBtn);
        historyBtn = root.findViewById(R.id.historyBtn);

        checkInBtn.setOnClickListener(this);

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
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);       // TODO : Try to change to QR code types only
        integrator.setPrompt("Scanner Code");
        integrator.initiateScan();
    }
}
