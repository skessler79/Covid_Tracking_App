package com.example.covid_19contacttracing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminShopListActivity extends AppCompatActivity {

    //Declare Firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    //Declare Views
    ListView shopList;

    ArrayList<String> mName = new ArrayList<String>();
    ArrayList<String> mStatus = new ArrayList<String>();
    ArrayList<String> shopId = new ArrayList<String>();

    List<Map<String, Object>> shopLists = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_shop);

        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //initializing Views
        shopList = findViewById(R.id.adminShopListView);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {// handles the 'back' button
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Getting shop info from Firebase
        fStore.collection("shops").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> tempShopLists = new HashMap<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //THIS SECTION MIGHT NOT BE NEEDED BY I WILL KEEP IT JUST IN CASE
                        //Basically make a new json structure array to keep all the datas
                        /*=================================================================
                        // gets name, phone, manager etc
                        tempShopLists=document.getData();

                        // add shopId key into the hash map
                        tempShopLists.put("shopId", document.getId());

                        //append shopLists array
                        shopLists.add(tempShopLists);
                        ==================================================================*/

                        //get attributes (modify here for ASC or DESC)
                        mName.add(document.getData().get("name").toString());
                        mStatus.add(document.getData().get("status").toString());
                        shopId.add(document.getId());

                    }
                    //set Adapter class to create rows
                    MyAdapter adapter = new MyAdapter(AdminShopListActivity.this, mName, mStatus);
                    shopList.setAdapter(adapter);
                } else {
                    //TODO: more proper error handling
                    Log.d("success", "Error getting documents: ", task.getException());
                }
            }
        });



        shopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), AdminShopProfileActivity.class);
                // this intent put our 0 index image to another activity
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                // now put title and description to another activity
                intent.putExtra("shopId", shopId.get(position));
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> rName = new ArrayList<String>();
        ArrayList<String> rStatus = new ArrayList<String>();

        MyAdapter (Context c, ArrayList<String> name,ArrayList<String> status) {
            super(c, R.layout.single_item, R.id.itemName, name);
            this.context = c;
            this.rName = name;
            this.rStatus = status;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.single_item, parent, false);

            ImageView images = row.findViewById(R.id.itemImage);
            TextView myTitle = row.findViewById(R.id.itemName);
            TextView myDescription = row.findViewById(R.id.itemDescription);

            images.setImageResource(R.drawable.admin_shop_icon);
            myTitle.setText(rName.get(position));
            myDescription.setText(rStatus.get(position));

            return row;
        }
    }
}
