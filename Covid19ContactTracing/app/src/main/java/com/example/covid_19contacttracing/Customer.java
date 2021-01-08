package com.example.covid_19contacttracing;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Customer extends User implements Serializable
{
    private CustStatus status;

    static FirebaseAuth fAuth;
    static FirebaseFirestore fStore;

    public Customer()
    {
        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    public Customer(String name, String phone, String email, CustStatus status)
    {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.status = status;

        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    public CustStatus getStatus()
    {
        return this.status;
    }

    public void setStatus(CustStatus status)
    {
        this.status = status;
    }

    // Check in with QR code
    public void checkIn(IntentResult result)
    {
        Long currentTime = System.currentTimeMillis() / 1000L;
        
        String shopId = result.getContents().replace("COVIDTRACE-", "");
        final String[] historyId = new String[1];

        // Updating master history in Cloud Firestore
        Map<String, Object> history = new HashMap<>();
        history.put("time", currentTime);
        history.put("shop", shopId);
        history.put("customer", fAuth.getCurrentUser().getUid());
        fStore.collection("history")
                .add(history)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                {
                    @Override
                    public void onSuccess(DocumentReference documentReference)
                    {
                        historyId[0] = documentReference.getId();

                        // Updating user history in Cloud Firestore
                        DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
                        docRef.update("history", FieldValue.arrayUnion(historyId[0]));

                        // Updating shops history in Cloud Firestore
                        DocumentReference shopRef = fStore.collection("shops").document(shopId);
                        shopRef.update("history", FieldValue.arrayUnion(historyId[0]));
                    }
                });


    }

    // Opens customer history page
    public void showHistory(Context context, DocumentSnapshot documentSnapshot)
    {
        ArrayList<String> list = (ArrayList<String>) documentSnapshot.get("history");
        ArrayList<HashMap<String, String>> newList = new ArrayList<>();

        if(list.isEmpty())
        {
            Toast.makeText(context, "You have no check-in history.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(int i = list.size() - 1; i >= 0; --i)
            {
                int count = 0;
                int finalI = i;
                DocumentReference docRef = fStore.collection("history").document(list.get(i));
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        HashMap<String, String> mapA = new HashMap<>();
                        mapA.put("time", documentSnapshot.get("time").toString());
                        DocumentReference shopRef = fStore.collection("shops").document(documentSnapshot.get("shop").toString());
                        shopRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                        {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot)
                            {
                                mapA.put("shop", documentSnapshot.getString("name"));
                                newList.add(mapA);

                                if(newList.size() >= list.size())
                                {
                                    Intent intent = new Intent(context, CustomerHistoryActivity.class);
                                    intent.putExtra("list", newList);
                                    context.startActivity(intent);
                                }

                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public String toString()
    {
        return this.name + " , " + this.phone + " , " + this.status;
    }
}
