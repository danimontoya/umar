package com.uma.umar.ui.schools;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uma.umar.BaseActivity;
import com.uma.umar.R;
import com.uma.umar.model.School;
import com.uma.umar.model.SchoolViewHolder;
import com.uma.umar.ui.schools.adapter.SchoolsAdapter;
import com.uma.umar.ui.schools.listener.SchoolsListener;

public class SchoolsActivity extends BaseActivity implements SchoolsListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private SchoolsAdapter mAdapter;

    //Getting reference to Firebase Database
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schools);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_schools);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_schools);

        DatabaseReference mSchoolsRef = mDatabaseReference.child("schools").getRef();
        mAdapter = new SchoolsAdapter(this, School.class, R.layout.school_item_layout, SchoolViewHolder.class, mSchoolsRef);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
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
}
