package com.example.covid_19contacttracing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminProfileFragment extends Fragment
{
    private static final String EXTRA_TEXT = "text";

    public static AdminProfileFragment createFor(String text) {
        AdminProfileFragment fragment = new AdminProfileFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    // Declaring firestore variables
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    // Declaring Views
    private TextView adminName;
    private Button shopBtn, customerBtn, masterVisitBtn, randomVisitGeneratorBtn;

    // Creating Admin Object
    private Admin admin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // Initializing Views
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_admin_profile, container, false);
        shopBtn = root.findViewById(R.id.adminShopBtn);
        customerBtn = root.findViewById(R.id.adminCustomerBtn);
        adminName = root.findViewById(R.id.adminName);
        masterVisitBtn = root.findViewById(R.id.adminMasterHistoryBtn);
        randomVisitGeneratorBtn = root.findViewById(R.id.adminRandomVisitGeneratorBtn);

        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Initializing admin
        admin = new Admin();

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
                    adminName.setText(fullName);
                }
            }
        });

        //navigation to shop list
        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdminShopListActivity.class));
            }
        });

        //navigation to customer list
        customerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdminCustomerListActivity.class));
            }
        });

        // navigation to master visit list
        masterVisitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdminMasterHistoryActivity.class));
            }
        });

        // functionality to generate random visits
        randomVisitGeneratorBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openDialog(getContext());
            }
        });
        return root;
    }

    public void openDialog (Context context) {
        RandomGeneratorDialog randomGeneratorDialog = new RandomGeneratorDialog(context);
        randomGeneratorDialog.show(getFragmentManager() , "random generator dialog");
    }
}

