package com.example.covid_19contacttracing;


import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Admin extends User {
    private CustStatus status;
    private ArrayList<HashMap<String, String>> history;
    private Map<String, Object> query;

    static FirebaseAuth fAuth;
    static FirebaseFirestore fStore;

    public Admin()
    {
        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    // flag customers with id given
    public void flag(String customerId, String shopId)
    {
        Log.d("success", "customerId in dialog: " + customerId);
        Log.d("success", "shopId in dialog: " + shopId);
    }

    // generate QR code
    public Bitmap generateQR(String shopId)
    {
        QRGEncoder qrgEncoder = new QRGEncoder(shopId, null, QRGContents.Type.TEXT, 150);

        return qrgEncoder.getBitmap();
    }

    public Map<String,Object>getQueryById(String type, String id){
        query = new HashMap<>();
        // Getting user info from Firebase
        DocumentReference docRef = fStore.collection(type).document(id);
        Log.d("success", "i was here");
        Log.d("success", "admin type==>"+type);
        Log.d("success", "admin id==>"+id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                Log.d("success", "I was here");
                query =  documentSnapshot.getData();
                Log.d("success", "admin data===>" + query.toString());
            }
        });
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return query;
    }

    // Opens shop list page
    public void showShopList()
    { }

    // Opens master history page
    public void showMasterHistory()
    { }
}
