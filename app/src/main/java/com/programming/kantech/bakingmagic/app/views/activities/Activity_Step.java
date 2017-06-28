package com.programming.kantech.bakingmagic.app.views.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_Ingredients;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_Step;

public class Activity_Step extends AppCompatActivity {

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
        if(savedInstanceState == null) {

            // Retrieve list index values that were sent through an intent; use them to display the desired Android-Me body part image
            // Use setListindex(int index) to set the list index for all BodyPartFragments

            // Create a new head BodyPartFragment
            Fragment_Step frag = new Fragment_Step();

            // Get the recipe_id from the intent
            // Set the default value to 0
            Step step = getIntent().getParcelableExtra(Constants.EXTRA_STEP);
            String recipe_name = getIntent().getStringExtra(Constants.EXTRA_RECIPE_NAME);

            if (actionBar != null) {
                actionBar.setTitle("Ingredients For " + recipe_name);
            }

            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.EXTRA_STEP, step);
            frag.setArguments(bundle);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.container_details, frag)
                    .commit();

        }


    }
}

