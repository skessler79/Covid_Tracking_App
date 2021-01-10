package com.example.covid_19contacttracing;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static java.lang.Long.parseLong;
import static java.lang.Math.abs;
/**
 *  This class consists of methods that handle database operations involving a admin.
 *
 * @author Selwyn Darryl Kessler
 * @author Theerapob Loo @ Loo Wei Xiong
 */
public class Admin extends User {
    private ArrayList<String> shopHistory;
    private Map<String, Object> query;

    private Random rand;

    private static FirebaseAuth fAuth;
    private static FirebaseFirestore fStore;

    public Admin()
    {
        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Initializing random class
        rand = new Random();
    }

    // generate QR code
    public Bitmap generateQR(String shopId)
    {
        //generate qr code with given text
        QRGEncoder qrgEncoder = new QRGEncoder("COVIDTRACE-"+shopId, null, QRGContents.Type.TEXT, 150);

        return qrgEncoder.getBitmap();
    }

    // flag customers with id given
    public void flag(Context context, String customerId, String shopId, long time)
    {
        // Declare Document References
        DocumentReference shopRef = fStore.collection("shops").document(shopId);
        DocumentReference customerRef = fStore.collection("users").document(customerId);

        // flag the current shop
        shopRef
                .update("status", ShopStatus.valueOf("Case"))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("success", "DocumentSnapshot successfully updated!(Shop Case)");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("failed", "Error updating document (Shop Case)", e);
                    }
                });

        // flag the current customer
        customerRef
                .update("status", CustStatus.valueOf(("Case")))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Succesfully flagged current visit", Toast.LENGTH_SHORT).show();
                        Log.d("success", "DocumentSnapshot successfully updated! (Customer Case)");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error flagging current visit", Toast.LENGTH_SHORT).show();
                        Log.w("failed", "Error updating document (Customer Case)", e);
                    }
                });

        // retrieve the visit from shop
        shopRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    shopHistory = (ArrayList<String>) documentSnapshot.get("history");
                    flagLogic(shopHistory, time , customerId);
                }
            }
        });
    }

    // flagging logic to check whether the user is close contact or not
    private void flagLogic (ArrayList<String> historyList, long time, String originalId){

        final int[] counter = {0};
        for (int i = 0 ; i < historyList.size();i++){
            DocumentReference historyRef = fStore.collection("history").document(historyList.get(i));
            historyRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
            {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot)
                {
                    String tempCustomerId = documentSnapshot.getString("customer");
                    long tempTime = parseLong(documentSnapshot.getLong("time").toString());

                    // check if its shorter than 1 hour or equals to the case customer
                    if (abs((tempTime) - time) <= 3600 && !(tempCustomerId.equals(originalId))){
                        DocumentReference customerRef = fStore.collection("users").document(tempCustomerId);
                        customerRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                        {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot)
                            {
                                if(documentSnapshot.exists() && !(documentSnapshot.get("status").equals("Case")))
                                {
                                    // flag the current customer as close case
                                    customerRef
                                            .update("status", "Close")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("success", "DocumentSnapshot successfully updated! (Close Case)");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("failed", "Error updating document (Close Case)", e);
                                                }
                                            });
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    // method for generating random visits
    public void randomVisitGenerator(Context context){
        ArrayList<String> shopIdList = new  ArrayList<String>();
        ArrayList<String> customerIdList = new  ArrayList<String>();
        fStore.collection("users").whereEqualTo("role", "Customer").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        customerIdList.add(document.getId()); // add all available customerId into
                    }
                    fStore.collection("shops").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    shopIdList.add(document.getId());
                                }
                                for ( int i = 0; i < 30; i++){
                                    int randomShopID = rand.nextInt(shopIdList.size());
                                    int randomCustomerId = rand.nextInt(customerIdList.size());
                                    int randomTimeDifference = rand.nextInt(18000);
                                    Long currentTime = (System.currentTimeMillis() / 1000L) - randomTimeDifference;
                                    Log.d("success", "randomShopID==>"+ randomShopID);
                                    Log.d("success", "randomCustomerId==>"+ randomCustomerId);
                                    Log.d("success", "currentTime==>"+ currentTime);
                                    randomVisitGeneratorLogic(shopIdList.get(randomShopID),customerIdList.get(randomCustomerId), currentTime);
                                }
                                Toast.makeText(context, "Successfully generated random visits", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w("failed", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                } else {
                    Log.w("failed", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    // some method to handle logic of checking in
    private void randomVisitGeneratorLogic(String shopId, String customerId, Long currentTime){

        final String[] historyId = new String[1];

        // Updating master history in  firestore cloud
        Map<String, Object> history = new HashMap<>();
        history.put("time", currentTime);
        history.put("shop", shopId);
        history.put("customer", fAuth.getCurrentUser().getUid());

        DocumentReference docRef = fStore.collection("users").document(customerId);
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
                                DocumentReference docRef = fStore.collection("users").document(customerId);
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
}

