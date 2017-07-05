package com.uma.umar.ui.place;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.uma.umar.utils.FirebaseConstants;
import com.uma.umar.utils.UMALog;

import java.util.ArrayList;

public class PlaceDetailsActivity extends BaseActivity implements OnMapReadyCallback, ValueEventListener, View.OnClickListener {

    public static final String PLACE_KEY = "placeKey";

    private TextView mPlaceNameTextView;
    private ImageView mAugmentedRealityImageView;

    private GoogleMap mMap;
    private DatabaseReference mPlaceRef;
    private String mPlaceKey;
    private Place mPlace;

    public static void startActivity(Activity activity, String placeKey) {
        Intent intent = new Intent(activity, PlaceDetailsActivity.class);
        intent.putExtra(PLACE_KEY, placeKey);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.stay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        mPlaceNameTextView = (TextView) findViewById(R.id.place_name_textview);
        mAugmentedRealityImageView = (ImageView) findViewById(R.id.umar_image);
        mAugmentedRealityImageView.setOnClickListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mPlaceKey = getIntent().getStringExtra(PLACE_KEY);
        mPlaceRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.PLACES).child(mPlaceKey).getRef();
        mPlaceRef.addValueEventListener(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mPlace = dataSnapshot.getValue(Place.class);
        UMALog.d("Place", "Place: name=" + mPlace.getName_en());

        mPlaceNameTextView.setText(mPlace.getName_en());

        // Add a marker and move the camera
        LatLng latLng = new LatLng(mPlace.getLatitude(), mPlace.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(mPlace.getName_en()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(18)            // Sets the zoom
                .build();            // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        UMALog.d("Place", "Place: databaseError=" + databaseError);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.umar_image) {
            ARPoint arPoint = new ARPoint(mPlace.getName_en(), mPlace.getImage(), mPlace.getLatitude(), mPlace.getLongitude(), mPlace.getAltitude());
            ArrayList<ARPoint> arPoints = new ArrayList<>();
            arPoints.add(arPoint);
            ARActivity.startActivity(this, arPoints);
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
}
