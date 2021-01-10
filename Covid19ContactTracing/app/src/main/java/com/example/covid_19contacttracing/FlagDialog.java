package com.example.covid_19contacttracing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FlagDialog extends AppCompatDialogFragment {

    //Declare Object
    private Admin admin;

    //Declare firebase
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String historyId;
    private String customerId;
    private String shopId;
    private long time;

    public FlagDialog (){
        super();

        //initialize firebase
        this.fAuth = FirebaseAuth.getInstance();
        this.fStore = FirebaseFirestore.getInstance();

        //initialize admin
        admin = new Admin();
    }

    public FlagDialog(String dHistoryId){
        super();

        //initialize firebase
        this.fAuth = FirebaseAuth.getInstance();
        this.fStore = FirebaseFirestore.getInstance();

        //initialize admin
        admin = new Admin();

        this.historyId = dHistoryId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        // gets history data by id from firestore
        DocumentReference docRef = fStore.collection("history").document(historyId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    customerId = documentSnapshot.getString("customer");
                    shopId = documentSnapshot.getString("shop");
                    time = documentSnapshot.getLong("time");
                }
            }
        });
        builder.setView(view).setTitle("Confirmation")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                admin.flag(getContext(),customerId, shopId, time);
            }
        });
        return builder.create();
    }
}
