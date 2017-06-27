package com.uma.umar.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uma.umar.BaseActivity;
import com.uma.umar.R;
import com.uma.umar.model.Profile;
import com.uma.umar.ui.dashboard.DashboardActivity;
import com.uma.umar.ui.profile.adapter.ProfileAdapter;
import com.uma.umar.ui.profile.adapter.ProfileViewHolder;
import com.uma.umar.ui.schools.adapter.RecyclerDividerDecorator;
import com.uma.umar.ui.schools.listener.SchoolsListener;
import com.uma.umar.utils.FirebaseConstants;

public class ProfileActivity extends BaseActivity implements SchoolsListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private String mSchoolId;
    private DatabaseReference mProfilesRef;
    private ProfileAdapter mAdapter;

    public static void startActivity(Activity activity, String schoolId){
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtra(FirebaseConstants.SCHOOL_ID, schoolId);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.stay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_profile);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_profile);

        mSchoolId = getIntent().getStringExtra(FirebaseConstants.SCHOOL_ID);
        mProfilesRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.SCHOOLS).child(mSchoolId).child(FirebaseConstants.PROFILES).getRef();
        mAdapter = new ProfileAdapter(this, Profile.class, R.layout.profile_item_layout, ProfileViewHolder.class, mProfilesRef);

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
        Profile profile = mAdapter.getItem(position);
        String profileKey = mAdapter.getRef(position).getKey();
        Toast.makeText(this, "Profile: " + profile.getName_en() + ", Key: " + profileKey, Toast.LENGTH_SHORT).show();
        Log.d("Profile", "Profile: " + profile.getName_en() + ", Key: " + profileKey);
        DashboardActivity.startActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_to_right);
    }
}
