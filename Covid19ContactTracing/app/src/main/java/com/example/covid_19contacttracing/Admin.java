package com.example.covid_19contacttracing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Admin extends User {
    private CustStatus status;
    private ArrayList<HashMap<String, String>> history;

    static FirebaseAuth fAuth;
    static FirebaseFirestore fStore;

    public Admin()
    {
        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    // flag customers with id given
    public void flagCustomer()
    { }

    // generate QR code
    public Bitmap generateQR(String shopId)
    {
        QRGEncoder qrgEncoder = new QRGEncoder(shopId, null, QRGContents.Type.TEXT, 150);

        return qrgEncoder.getBitmap();
    }

    // Opens shop list page
    public void showShopList()
    { }

    // Opens master history page
    public void showMasterHistory()
    { }
}
