package com.example.covid_19contacttracing;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class TestDrawerActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener
{
    // Menu options
    private static final int POS_CLOSE = 0;
    private static final int POS_DASHBOARD = 1;
    private static final int POS_MY_PROFILE = 2;
    private static final int POS_STATISTICS = 3;
    private static final int POS_SETTINGS = 4;
    private static final int POS_ABOUT_US = 5;
    private static final int POS_LOGOUT = 7;

    // Icons and titles of menu options
    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    // Declaring firebase
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private static final String TAG = "TestDrawerActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drawer);

        // Initializing toolbar
        Toolbar toolbar = findViewById(R.id.drawer_toolbar);
        setSupportActionBar(toolbar);

        // Initializing Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Initializing slidingRootNav
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withRootViewElevation(10)
                .withRootViewYTranslation(4)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_CLOSE),
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_MY_PROFILE),
                createItemFor(POS_STATISTICS),
                createItemFor(POS_SETTINGS),
                createItemFor(POS_ABOUT_US),
                new DrawerSpaceItem(260),
                createItemFor(POS_LOGOUT)
        ));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASHBOARD);
    }

    // Creates individual drawer item
    private DrawerSimpleItem createItemFor(int position)
    {
        return new DrawerSimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.primary_blue))
                .withTextTint(color(R.color.black))
                .withSelectedIconTint(color(R.color.primary_blue))
                .withSelectedTextTint(color(R.color.primary_blue));
    }

    @ColorInt
    private int color(@ColorRes int res)
    {
        return ContextCompat.getColor(this, res);
    }

    private String[] loadScreenTitles()
    {
        return getResources().getStringArray(R.array.id_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons()
    {
        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for(int i = 0; i < ta.length(); ++i)
        {
            int id = ta.getResourceId(i, 0);
            if(id != 0)
            {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    // Quits app when back button is pressed
    @Override
    public void onBackPressed()
    {
        finish();
    }

    @Override
    public void onItemSelected(int position)
    {
        Fragment selectedScreen = CustomerProfileFragment.createFor(screenTitles[position]);

        if (position == POS_LOGOUT) {
            fAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), SendOTPActivity.class);
            intent.putExtra("logout", "logout");
            startActivity(intent);
            finish();
        }
        slidingRootNav.closeMenu();

        // Getting user info from Firebase
        DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    String role = documentSnapshot.getString("role");

                    Fragment selectedScreen;
                    // select fragment to show based on user role
                    if (role.equals("Customer")){
                        selectedScreen = CustomerProfileFragment.createFor(screenTitles[position]);
                        if(position == POS_MY_PROFILE)
                        {
                            selectedScreen = CustomerProfileFragment.createFor(screenTitles[position]);
                        }
                        else if(position == POS_STATISTICS)
                        {
                            selectedScreen = StatisticsFragment.createFor(screenTitles[position]);
                        }
                    }
                    else {
                        selectedScreen = AdminProfileFragment.createFor(screenTitles[position]);

                        if(position == POS_STATISTICS)
                        {
                            selectedScreen = StatisticsFragment.createFor(screenTitles[position]);
                        }
                    }
                    showFragment(selectedScreen);
                }
            }
        });
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}