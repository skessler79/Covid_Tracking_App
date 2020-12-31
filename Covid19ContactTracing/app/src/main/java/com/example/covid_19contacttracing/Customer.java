package com.example.covid_19contacttracing;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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
import java.util.List;
import java.util.Map;

public class Customer extends User implements Serializable
{
    private CustStatus status;
    private ArrayList<HashMap<String, String>> history;

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

        Map<String, String> customerHistory = new HashMap<>();
        customerHistory.put(String.valueOf(currentTime), result.getContents());
        CustomerHistoryTest shopHistory = new CustomerHistoryTest(currentTime, fAuth.getCurrentUser().getUid());

        // Updating user history in Cloud Firestore
        DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        docRef.update("history", FieldValue.arrayUnion(customerHistory));

        // Updating shops history in Cloud Firestore
        DocumentReference shopRef = fStore.collection("shops").document(result.getContents());
        shopRef.update("history", FieldValue.arrayUnion(shopHistory));
    }

    // Opens customer history page
    public void showHistory(Context context, DocumentSnapshot documentSnapshot)
    {
        ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) documentSnapshot.get("history");
        ArrayList<HashMap<String, String>> newList = new ArrayList<>();
        final List<Boolean> done = new ArrayList<>();

        if(list.isEmpty())
        {
            Toast.makeText(context, "You have no check-in history.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(int i = list.size() - 1; i >= 0; --i)
            {
                for(Map.Entry<String, String> entry : list.get(i).entrySet())
                {
                    String timeStr = entry.getKey();
                    HashMap<String, String> mapA = new HashMap<>();
                    mapA.put("time", timeStr);

                    DocumentReference shopRef = fStore.collection("shops").document(entry.getValue());
                    int finalI = i;
                    shopRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                    {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot)
                        {
                            Log.d("testhistory", "onSuccess: Called");
                            String shopName = documentSnapshot.getString("name");
                            mapA.put("shop", shopName);
                            newList.add(mapA);

                            if(finalI == 0)
                            {
                                Intent intent = new Intent(context, CustomerHistoryActivity.class);
                                intent.putExtra("list", newList);
                                context.startActivity(intent);
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder("");
        str.append(this.name + " , ");
        str.append(this.phone + " , ");
        str.append(this.status);
        return str.toString();
    }
}
