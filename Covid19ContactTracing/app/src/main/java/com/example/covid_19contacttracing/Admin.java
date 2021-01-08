package com.example.covid_19contacttracing;


import android.graphics.Bitmap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

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
