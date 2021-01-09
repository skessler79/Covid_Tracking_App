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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static java.lang.Math.abs;

public class Admin extends User {
    private ArrayList<String> shopHistory;
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
    public void flag(Context context, String customerId, String shopId, long time)
    {


    }

    // customer flagging logic
    private void flagCustomer (ArrayList<String> historyList, long time, String originalId){

    }

    // generate QR code
    public Bitmap generateQR(String shopId)
    {
        QRGEncoder qrgEncoder = new QRGEncoder(shopId, null, QRGContents.Type.TEXT, 150);

        return qrgEncoder.getBitmap();
    }

    public void getQueryById(String type, String id){}

    // Opens shop list page
    public void showShopList()
    { }

    // Opens master history page
    public void showMasterHistory()
    { }
}
