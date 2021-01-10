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

/**
 *  This class consists of methods that handle database operations involving a customer.
 *
 * @author Selwyn Darryl Kessler
 * @author Theerapob Loo @ Loo Wei Xiong
 */
public class Customer extends User
{
    /**
     * Holds the customer status as a value of an enum.
     */
    private CustStatus status;

    /**
     * Stores the Firebase Authentication instance.
     */
    static FirebaseAuth fAuth;

    /**
     * Stores the Cloud Firestore instance.
     */
    static FirebaseFirestore fStore;

    /**
     * Default constructor for Customer class.
     */
    public Customer()
    {
        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    /**
     * Parameterized constructor for Customer class.
     *
     * @param name Name of the customer.
     * @param phone Phone number of the customer.
     * @param email Email address of the customer.
     * @param status Status of the customer.
     */
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

    /**
     * Returns the status of the customer.
     *
     * @return The status of the customer.
     */
    public CustStatus getStatus()
    {
        return this.status;
    }

    /**
     * Sets the status of the customer.
     *
     * @param status The status of the customer to be set.
     */
    public void setStatus(CustStatus status)
    {
        this.status = status;
    }

    /**
     * Checks-in to a shop with a QR code.
     *
     * @param result The result of the QR code scan.
     */
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

        DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                history.put("customerName", documentSnapshot.getString("fullName"));

                DocumentReference shopRef = fStore.collection("shops").document(shopId);
                shopRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        history.put("shopName", documentSnapshot.getString("name"));

                        fStore.collection("history").add(history).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
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
                });
            }
        });
    }

    /**
     * Opens an activity to show the current customer visit history.
     *
     * @param context The context of the current activity.
     * @param documentSnapshot The current customer's document snapshot from Cloud Firestore.
     */
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
                DocumentReference docRef = fStore.collection("history").document(list.get(i));
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        HashMap<String, String> mapA = new HashMap<>();
                        mapA.put("time", documentSnapshot.get("time").toString());
                        mapA.put("shop", documentSnapshot.getString("shopName"));
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
        }
    }

    /**
     * Returns a short string containing the basic info (name, phone, email, status) of the current customer.
     *
     * @return A short string containing the basic info of the current customer.
     */
    @Override
    public String toString()
    {
        return this.name + " , " + this.phone + " , " + this.email + " , " + this.status;
    }
}
