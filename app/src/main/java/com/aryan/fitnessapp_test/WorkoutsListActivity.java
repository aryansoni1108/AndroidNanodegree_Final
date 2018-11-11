package com.aryan.fitnessapp_test;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toolbar;

import com.aryan.fitnessapp_test.Adapters.CustomItemClickListener;
import com.aryan.fitnessapp_test.Adapters.WorkoutsRecyclerView;
import com.aryan.fitnessapp_test.Networking.WorkoutInfoFetch;
import com.aryan.fitnessapp_test.Networking.onFetchTaskCompleted;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkoutsListActivity extends AppCompatActivity {

    private static final String KEY_RECYCLER_STATE = "recycler_state_workout_list";
    @BindView(R.id.workouts_recyclerview)
    RecyclerView mWorkoutRecyclerView;
    @BindView(R.id.loading_indicator)
    ProgressBar spinner;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Workouts> workouts1 = new ArrayList<>();
    Parcelable listState;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workouts_list);
        ButterKnife.bind(this);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int catid = getIntent().getExtras().getInt(getString(R.string.intent_extra_browse_fragment));
        spinner.setVisibility(View.VISIBLE);

        Log.e("ID transfer", "" + catid);
        String url = getString(R.string.cat_url_p1) + catid + getString(R.string.cat_url_p2);
        layoutManager = new LinearLayoutManager(WorkoutsListActivity.this);
        mWorkoutRecyclerView.setLayoutManager(layoutManager);
        WorkoutInfoFetch.fetchWorkoutInfo(url, new onFetchTaskCompleted() {
            @Override
            public void onFetchWorkoutInfoFetch(final List<Workouts> workouts) {
                spinner.setVisibility(View.GONE);

                populateui(workouts);

            }

        }, WorkoutsListActivity.this);


    }

    private void populateui(final List<Workouts> workouts) {
        workouts1.addAll(workouts);



        WorkoutsRecyclerView workoutsRecyclerView = new WorkoutsRecyclerView(workouts, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(WorkoutsListActivity.this, WorkoutDetailActivity.class);
                Workouts workouts1 = workouts.get(position);
                intent.putExtra(getString(R.string.workouts_clicked_parcel), workouts1);
                startActivity(intent);

            }
        });

        mWorkoutRecyclerView.setAdapter(workoutsRecyclerView);
    }

@Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // Save list state and
    //for saving scroll position
    if(state != null)
    {
        Parcelable savedRecyclerLayoutState = state.getParcelable(KEY_RECYCLER_STATE);
        mWorkoutRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }

    }



    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        // Retrieve list state and list/item positions
        if(state != null)
            listState = state.getParcelable(KEY_RECYCLER_STATE);
    }


    }



