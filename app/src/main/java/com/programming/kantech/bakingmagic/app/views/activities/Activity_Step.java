package com.programming.kantech.bakingmagic.app.views.activities;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Recipe;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.utils.Utils_General;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_Ingredients;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_Step;

public class Activity_Step extends AppCompatActivity implements Fragment_Step.StepNavClickListener {

    private Step mStep;
    private String mRecipe_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        // Set the action bar back button to look like an up button
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {

            // Create a new head BodyPartFragment
            Fragment_Step frag = new Fragment_Step();

            // Get the recipe_id from the intent
            mStep = getIntent().getParcelableExtra(Constants.EXTRA_STEP);
            mRecipe_Name = getIntent().getStringExtra(Constants.EXTRA_RECIPE_NAME);

            if (actionBar != null) {
                actionBar.setTitle("Ingredients For " + mRecipe_Name);
            }

            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.EXTRA_STEP, mStep);
            frag.setArguments(bundle);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.container_details, frag)
                    .commit();

        }


    }

    // Called from the step fragment
    @Override
    public void onNextStepSelected() {

        // Assuming the validation in the fragment worked, we should not get requests
        // that we dont have steps for
        int nextId = mStep.getId() + 1;

        String selection = Contract_BakingMagic.StepsEntry.COLUMN_STEP_ID + "=?";
        String[] selectionArgs = {"" + nextId};

        getStepFromDB(selection, selectionArgs);

    }

    @Override
    public void onPreviousStepSelected() {

        // Assuming the validation in the fragment worked, we should not get requests
        // that we dont have steps for
        int nextId = mStep.getId() - 1;

        String selection = Contract_BakingMagic.StepsEntry.COLUMN_STEP_ID + "=?";
        String[] selectionArgs = {"" + nextId};

        getStepFromDB(selection, selectionArgs);

    }

    private void getStepFromDB(String selection, String[] args) {

        ContentResolver resolver = getContentResolver();

        /* URI for all rows of data in our step table */
        Uri uri = Contract_BakingMagic.StepsEntry.CONTENT_URI;

        new AsyncQueryHandler(resolver) {
            @Override
            protected void onQueryComplete(int token, Object cookie,
                                           Cursor cursor) {

                // Check if an error was returned
                if (null == cursor || cursor.getCount() == 0) {
                    Log.i(Constants.LOG_TAG, "There was an error getting the steps for a count");
                } else {
                    cursor.moveToFirst();
                    // Replace the fragment with the new step

                    mStep = Contract_BakingMagic.StepsEntry.getStepFromCursor(cursor);

                    if (mStep.getVideoURL().length() != 0) {

                        // Load step fragment
                        replaceDetailsFragmentWithStepFrag();


                    } else if (mStep.getThumbnailURL().length() != 0) {
                        // TODO: Handle steps with images, and no videos
                    }


                }
            }
        }.startQuery(0, null, uri, null, selection, args, null);
    }

    public void replaceDetailsFragmentWithStepFrag() {

        // Put this information in a Bundle and attach it to an Intent that will launch an Ingredients Activity
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_STEP, mStep);
        bundle.putString(Constants.EXTRA_RECIPE_NAME, mRecipe_Name);

        Fragment_Step frag = new Fragment_Step();
        frag.setArguments(bundle);

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container_details, frag).commit();
    }
}

