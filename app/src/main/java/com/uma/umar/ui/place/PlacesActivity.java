package com.uma.umar.ui.place;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uma.umar.BaseActivity;
import com.uma.umar.R;
import com.uma.umar.model.Place;
import com.uma.umar.ui.place.adapter.PlaceRecyclerAdapter;
import com.uma.umar.ui.place.listener.PlacesListener;
import com.uma.umar.ui.schools.adapter.RecyclerDividerDecorator;
import com.uma.umar.utils.FirebaseConstants;
import com.uma.umar.utils.UMALog;
import com.uma.umar.utils.UmARNetworkUtil;
import com.uma.umar.utils.UmARSharedPreferences;

import java.util.LinkedHashMap;

public class PlacesActivity extends BaseActivity implements ValueEventListener, PlacesListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private DatabaseReference mPlacesRef;
    private PlaceRecyclerAdapter mAdapter;
    private LinkedHashMap<String, Place> mPlacesMap;

    public static void startActivity(Activity activity) {
        if (!UmARNetworkUtil.isNetworkAvailable()) {
            ((BaseActivity) activity).showDialogNoInternet();
            return;
        }
        Intent intent = new Intent(activity, PlacesActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.stay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_places);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_places);

        mPlacesRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.PLACES).child(UmARSharedPreferences.getSchoolId()).getRef();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlacesRef.addValueEventListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlacesRef.removeEventListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_to_right);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        String profileSelectedId = UmARSharedPreferences.getProfileId();

        mPlacesMap = new LinkedHashMap<>();
        for (DataSnapshot placeDataSnapshot : dataSnapshot.getChildren()) {
            Place place = placeDataSnapshot.getValue(Place.class);
            if (place.getProfiles().containsValue(profileSelectedId)) {
                String placeId = placeDataSnapshot.getKey();
                mPlacesMap.put(placeId, place);
                UMALog.d("Places", "Place name=" + place.getName());
            }
        }

        System.out.println("lololo");

        mAdapter = new PlaceRecyclerAdapter(this, PlacesActivity.this, mPlacesMap);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerDividerDecorator decorator = new RecyclerDividerDecorator(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(decorator);

        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onItemClick(String placeKey, Place place) {
        Toast.makeText(this, "Place: " + place.getName() + ", Key: " + placeKey, Toast.LENGTH_SHORT).show();
        UMALog.d("Place", "Place: " + place.getName() + ", Key: " + placeKey);
        PlaceDetailsActivity.startActivity(this, placeKey);
    }
}
