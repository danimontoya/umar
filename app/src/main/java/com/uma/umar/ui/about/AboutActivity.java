package com.uma.umar.ui.about;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

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
import com.uma.umar.model.School;
import com.uma.umar.utils.FirebaseConstants;
import com.uma.umar.utils.UMALog;
import com.uma.umar.utils.UmARNetworkUtil;
import com.uma.umar.utils.UmARSharedPreferences;

public class AboutActivity extends BaseActivity implements OnMapReadyCallback, ValueEventListener, View.OnClickListener {

    public static void startActivity(Activity activity) {
        if (!UmARNetworkUtil.isNetworkAvailable()) {
            ((BaseActivity) activity).showDialogNoInternet();
            return;
        }
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }

    private School mSchool;
    private AboutSchoolViewHolder mViewHolder;
    private GoogleMap mMap;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mViewHolder = new AboutSchoolViewHolder(findViewById(R.id.root));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.SCHOOLS).child(UmARSharedPreferences.getSchoolId()).getRef();
        mRef.addValueEventListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRef.removeEventListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mSchool = dataSnapshot.getValue(School.class);
        if (mSchool == null) {
            showDialogInformationException();
            return;
        }
        mViewHolder.nameTextView.setText(mSchool.getName());
        mViewHolder.descriptionTextView.setText(mSchool.getDescription());

        // Add a marker and move the camera
        LatLng latLng = new LatLng(mSchool.getLatitude(), mSchool.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(mSchool.getName()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(17)            // Sets the zoom
                .build();            // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mViewHolder.addressTextView.setText(mSchool.getAddress());
        mViewHolder.directorTextView.setText(mSchool.getDirector());
        mViewHolder.emailTextView.setText(mSchool.getEmail());
        mViewHolder.phoneConciergeTextView.setText(mSchool.getPhone_concierge());
        mViewHolder.phoneSecretaryTextView.setText(mSchool.getPhone_secretary());

        mViewHolder.phoneConciergeTextView.setOnClickListener(this);
        mViewHolder.phoneSecretaryTextView.setOnClickListener(this);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        UMALog.d("School", "School: databaseError=" + databaseError);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mViewHolder.phoneConciergeTextView.getId()) {
            dialPhone(mSchool.getPhone_concierge());
        } else if (id == mViewHolder.phoneSecretaryTextView.getId()) {
            dialPhone(mSchool.getPhone_secretary());
        }
    }

    private void dialPhone(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }
}
