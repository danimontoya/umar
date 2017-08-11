package com.uma.umar.ui.category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uma.umar.BaseActivity;
import com.uma.umar.R;
import com.uma.umar.model.Place;
import com.uma.umar.ui.category.adapter.PlaceAdapter;
import com.uma.umar.ui.category.adapter.PlaceViewHolder;
import com.uma.umar.ui.place.PlaceDetailsActivity;
import com.uma.umar.ui.schools.adapter.RecyclerDividerDecorator;
import com.uma.umar.ui.schools.listener.SchoolsListener;
import com.uma.umar.utils.FirebaseConstants;
import com.uma.umar.utils.UMALog;
import com.uma.umar.utils.UmARNetworkUtil;

public class CategoriesActivity extends BaseActivity implements SchoolsListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private DatabaseReference mPlacesRef;
    private PlaceAdapter mAdapter;

    public static void startActivity(Activity activity) {
        if (!UmARNetworkUtil.isNetworkAvailable()) {
            ((BaseActivity) activity).showDialogNoInternet();
            return;
        }
        Intent intent = new Intent(activity, CategoriesActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.stay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_places);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_places);

        mPlacesRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.PLACES).getRef();
        mAdapter = new PlaceAdapter(this, Place.class, R.layout.place_item_layout, PlaceViewHolder.class, mPlacesRef);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerDividerDecorator decorator = new RecyclerDividerDecorator(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(decorator);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDataChanged() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, int position) {
        Place place = mAdapter.getItem(position);
        String placeKey = mAdapter.getRef(position).getKey();
        Toast.makeText(this, "Place: " + place.getName() + ", Key: " + placeKey, Toast.LENGTH_SHORT).show();
        UMALog.d("Place", "Place: " + place.getName() + ", Key: " + placeKey);
        PlaceDetailsActivity.startActivity(this, placeKey);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_to_right);
    }
}
