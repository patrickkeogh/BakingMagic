package com.programming.kantech.bakingmagic.app.views.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Recipe;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.utils.Utils_General;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_DetailsList;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_Ingredients;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_Step;

public class Activity_Details extends AppCompatActivity implements Fragment_DetailsList.StepClickListener {

    // Track whether to display a two-column or single-column UI
    // A single-column display refers to phone screens, and two-column to larger tablet screens
    private boolean mTwoCols;
    private Recipe mRecipe;

    // We will initially load the ingredients list fragment
    private boolean mShowIngredients = true;

    private FrameLayout frame_details;


    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            Log.i(Constants.LOG_TAG, "savedInstanceState is not null");
            if (savedInstanceState.containsKey(Constants.STATE_INFO_RECIPE)) {
                Log.i(Constants.LOG_TAG, "we found the key in savedInstanceState");
                mRecipe = savedInstanceState.getParcelable(Constants.STATE_INFO_RECIPE);
            }
        } else {
            Log.i(Constants.LOG_TAG, "savedInstanceState is null, get data from intent");
            mRecipe = getIntent().getParcelableExtra(Constants.EXTRA_RECIPE);
        }

        // Set the action bar back button to look like an up button
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }



        Log.i(Constants.LOG_TAG, "Recipe id in onCreate(): " + mRecipe.getId());

        // Add master list fragment to the screen
        addMasterListFragment();

        // Determine if you're creating a two-pane or single-pane display
        if (findViewById(R.id.layout_for_two_cols) != null) {
            Log.i(Constants.LOG_TAG, "We have 2 columns to show");

            // This LinearLayout will only initially exist in the two-col tablet case
            mTwoCols = true;

            // Get a ref to the details container
            frame_details = (FrameLayout) findViewById(R.id.container_details);

            if (savedInstanceState == null) {

                // Start by showing the ingredients
                addIngredientsFragment();
            }

        } else {
            Log.i(Constants.LOG_TAG, "We have 1 column to show");
            mTwoCols = false;
        }


    }

    private void addIngredientsFragment() {

        Fragment_Ingredients frag_ingredients = new Fragment_Ingredients();

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_RECIPE_ID, mRecipe.getId());
        frag_ingredients.setArguments(bundle);

        mFragmentManager.beginTransaction()
                .add(R.id.container_details, frag_ingredients).commit();

    }

    public void addMasterListFragment() {

        Fragment_DetailsList frag_details_list = new Fragment_DetailsList();

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_RECIPE_ID, mRecipe.getId());
        frag_details_list.setArguments(bundle);

        mFragmentManager.beginTransaction()
                .add(R.id.master_list_fragment, frag_details_list).commit();


    }

    public void replaceDetailsFragmentWithStepFrag(Step step) {

        // Put this information in a Bundle and attach it to an Intent that will launch an Ingredients Activity
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_STEP, step);
        bundle.putString(Constants.EXTRA_RECIPE_NAME, mRecipe.getName());

        if(mTwoCols){
            Fragment_Step frag = new Fragment_Step();
            frag.setArguments(bundle);

            mFragmentManager.beginTransaction()
                    .replace(R.id.container_details, frag).commit();
        }else{
            // Attach the Bundle to an intent
            final Intent intent = new Intent(this, Activity_Step.class);

            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    public void replaceDetailsFragmentWithIngredientsFrag() {

        // Put this information in a Bundle and attach it to an Intent that will launch an Ingredients Activity
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_RECIPE_ID, mRecipe.getId());
        bundle.putString(Constants.EXTRA_RECIPE_NAME, mRecipe.getName());

        if(mTwoCols){
            Fragment_Ingredients frag_ingredients = new Fragment_Ingredients();
            frag_ingredients.setArguments(bundle);

            mFragmentManager.beginTransaction()
                    .replace(R.id.container_details, frag_ingredients).commit();
        }else{
            // Attach the Bundle to an intent
            final Intent intent = new Intent(this, Activity_Ingredients.class);

            intent.putExtras(bundle);
            startActivity(intent);
        }





    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(Constants.LOG_TAG, "onSaveInstanceState() called in Activity_Details");
        outState.putParcelable(Constants.STATE_INFO_RECIPE, mRecipe);
    }

    @Override
    public void onStepSelected(Step step) {

        Log.i(Constants.LOG_TAG, "Step:" + step.toString());

        if (step.getVideoURL().length() != 0) {
            Utils_General.showToast(this, "We have a Video:" + step.getVideoURL());

            // Load step fragment
            replaceDetailsFragmentWithStepFrag(step);


        } else if (step.getThumbnailURL().length() != 0) {
            Utils_General.showToast(this, "We have an Image:" + step.getThumbnailURL());
        }
    }


}
