package com.uma.umar.ui.schools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.uma.umar.model.School;
import com.uma.umar.ui.dashboard.DashboardActivity;
import com.uma.umar.ui.profile.ProfileActivity;
import com.uma.umar.ui.schools.adapter.RecyclerDividerDecorator;
import com.uma.umar.ui.schools.adapter.SchoolViewHolder;
import com.uma.umar.ui.schools.adapter.SchoolsAdapter;
import com.uma.umar.ui.schools.listener.SchoolsListener;
import com.uma.umar.utils.FirebaseConstants;
import com.uma.umar.utils.UMALog;
import com.uma.umar.utils.UmARSharedPreferences;

public class SchoolsActivity extends BaseActivity implements SchoolsListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private SchoolsAdapter mAdapter;

    //Getting reference to Firebase Database
    private DatabaseReference mSchoolsRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.SCHOOLS).getRef();

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, SchoolsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schools);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_schools);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_schools);

        mAdapter = new SchoolsAdapter(this, School.class, R.layout.school_item_layout, SchoolViewHolder.class, mSchoolsRef);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerDividerDecorator decorator = new RecyclerDividerDecorator(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(decorator);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    @Override
    public void onDataChanged() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, int position) {
        School school = mAdapter.getItem(position);
        String schoolKey = mAdapter.getRef(position).getKey();

        // Storing the school id, so its not needed to ask for it everytime
        UmARSharedPreferences.setSchoolId(schoolKey);

        UMALog.d("Schools", "School: " + school.getName() + ", Key: " + schoolKey);
        ProfileActivity.startActivity(this, schoolKey);
    }
}
