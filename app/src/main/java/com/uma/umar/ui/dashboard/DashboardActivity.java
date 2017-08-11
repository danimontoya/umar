package com.uma.umar.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uma.umar.BaseActivity;
import com.uma.umar.R;
import com.uma.umar.UmARApplication;
import com.uma.umar.model.ARPoint;
import com.uma.umar.model.Place;
import com.uma.umar.ui.about.AboutActivity;
import com.uma.umar.ui.ar.ARActivity;
import com.uma.umar.ui.category.CategoriesActivity;
import com.uma.umar.ui.place.PlaceDetailsActivity;
import com.uma.umar.ui.profile.ProfileActivity;
import com.uma.umar.ui.qr.BarcodeCaptureActivity;
import com.uma.umar.ui.schools.SchoolsActivity;
import com.uma.umar.ui.webview.WebViewActivity;
import com.uma.umar.utils.FirebaseConstants;
import com.uma.umar.utils.UmARSharedPreferences;

import java.util.ArrayList;

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ValueEventListener, LanguageFragment.LanguageListener {

    private View mARButton;
    private View mPlacesButton;
    private View mQRButton;
    private View mInfoButton;

    private DatabaseReference mPlacesRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.PLACES).getRef();

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.stay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.title_activity_dashboard);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mARButton = findViewById(R.id.ar_button);
        mARButton.setOnClickListener(this);
        mPlacesButton = findViewById(R.id.places_button);
        mPlacesButton.setOnClickListener(this);
        mQRButton = findViewById(R.id.qr_button);
        mQRButton.setOnClickListener(this);
        mInfoButton = findViewById(R.id.info_button);
        mInfoButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_school) {
            SchoolsActivity.startActivity(this);

        } else if (id == R.id.nav_profile) {
            ProfileActivity.startActivity(this, UmARSharedPreferences.getSchoolId());

        } else if (id == R.id.nav_language) {
            LanguageFragment languageFragment = LanguageFragment.newInstance(UmARSharedPreferences.getLanguage());
            languageFragment.setListener(this);
            languageFragment.show(getSupportFragmentManager(), LanguageFragment.TAG);

        } else if (id == R.id.nav_about) {
            WebViewActivity.startActivity(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ar_button) {
            mPlacesRef.addValueEventListener(this);

        } else if (id == R.id.places_button) {
            CategoriesActivity.startActivity(this);

        } else if (id == R.id.qr_button) {
            BarcodeCaptureActivity.startActivity(this);

        } else if (id == R.id.info_button) {
            AboutActivity.startActivity(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BarcodeCaptureActivity.RC_BARCODE_CAPTURE && resultCode == CommonStatusCodes.SUCCESS) {
            PlaceDetailsActivity.startActivity(this, data.getStringExtra(BarcodeCaptureActivity.BarcodeObject));
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<ARPoint> arPoints = new ArrayList<>();
        for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
            Place place = placeSnapshot.getValue(Place.class);
            ARPoint arPoint = new ARPoint(place.getName_en(), place.getImage(), place.getLatitude(), place.getLongitude(), place.getAltitude());
            // TODO: 7/4/17 Picasso get bitmap here?
            arPoints.add(arPoint);
        }
        ARActivity.startActivity(this, arPoints);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onLanguageSelected(String langCode) {
        UmARSharedPreferences.setLanguage(langCode);
        UmARApplication.getInstance().setLocale();
        DashboardActivity.startActivity(this);
    }
}
