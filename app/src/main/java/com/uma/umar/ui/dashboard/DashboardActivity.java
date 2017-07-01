package com.uma.umar.ui.dashboard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uma.umar.BaseActivity;
import com.uma.umar.R;
import com.uma.umar.model.ARPoint;
import com.uma.umar.model.Place;
import com.uma.umar.ui.ar.ARActivity;
import com.uma.umar.ui.category.CategoriesActivity;
import com.uma.umar.utils.FirebaseConstants;

import java.util.ArrayList;

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ValueEventListener {

    private View mARButton;
    private View mPlacesButton;
    private View mQRButton;
    private View mInfoButton;

    private DatabaseReference mPlacesRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.PLACES).getRef();

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, DashboardActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.stay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ar_button) {
            //dialogNotDoneYet();
            mPlacesRef.addValueEventListener(this);
        } else if (id == R.id.places_button) {
            CategoriesActivity.startActivity(this);
        } else if (id == R.id.qr_button) {
            dialogNotDoneYet();
        } else if (id == R.id.info_button) {
            dialogNotDoneYet();
        }
    }

    private void dialogNotDoneYet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("UmAR");
        builder.setMessage("Eeee no soy tan rapido.. esto esta en proceso aun.. Waait for it! ;)")
                .setPositiveButton("Vale hijoooo..! I'll waaait!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        ArrayList<ARPoint> arPoints = new ArrayList<>();
        for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
            Place place = placeSnapshot.getValue(Place.class);
            ARPoint arPoint = new ARPoint(place.getName_en(), place.getImage(), place.getLatitude(), place.getLongitude(), place.getAltitude());
            arPoints.add(arPoint);
        }
        ARActivity.startActivity(this, arPoints);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
