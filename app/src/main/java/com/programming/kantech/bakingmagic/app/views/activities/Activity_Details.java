package com.programming.kantech.bakingmagic.app.views.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Recipe;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.utils.Utils_General;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_Details;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_DetailsList;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_Ingredients;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_Media_Video;

public class Activity_Details extends AppCompatActivity implements Fragment_DetailsList.StepClickListener {

    // Track whether to display a two-column or single-column UI
    // A single-column display refers to phone screens, and two-column to larger tablet screens
    private boolean mTwoCols;
    private Recipe mRecipe;

    // We will initially load the ingredients list fragment
    private boolean mShowIngredients = true;

    private FrameLayout frame_ingredients;
    private FrameLayout frame_media_video;
    private FrameLayout frame_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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
        replaceMasterListFragment();


        // Determine if you're creating a two-pane or single-pane display
        if (findViewById(R.id.layout_for_two_cols) != null) {
            Log.i(Constants.LOG_TAG, "We have 2 columns to show");

            frame_ingredients = (FrameLayout) findViewById(R.id.container_ingredients);
            frame_media_video = (FrameLayout) findViewById(R.id.container_media_video);

            // This LinearLayout will only initially exist in the two-col tablet case
            mTwoCols = true;


            if (savedInstanceState == null) {

                replaceFragments(false);


            }

        } else {
            Log.i(Constants.LOG_TAG, "We have 1 column to show");
            mTwoCols = false;
        }


    }

    public void replaceMasterListFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment_DetailsList frag_details_list = new Fragment_DetailsList();

        Bundle bundle = new Bundle();
        bundle.putInt("edttext", mRecipe.getId());
        frag_details_list.setArguments(bundle);


        fragmentManager.beginTransaction()
                .add(R.id.master_list_fragment, frag_details_list).commit();


    }

    public void replaceVideoMediaFragment(Step step) {

        frame_media_video.setVisibility(View.VISIBLE);
        frame_ingredients.setVisibility(View.GONE);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment_Media_Video frag_media = new Fragment_Media_Video();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_STEP, step);
        frag_media.setArguments(bundle);


        fragmentManager.beginTransaction()
                .add(R.id.container_media_video, frag_media).commit();


    }

    public void replaceFragments(boolean showIngredients) {

        mShowIngredients = showIngredients;

        // In two-pane mode, add initial BodyPartFragments to the screen
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (mTwoCols) {

            if (mShowIngredients) {

                Log.i(Constants.LOG_TAG, "Show Ingredients");


                frame_media_video.setVisibility(View.GONE);
                frame_ingredients.setVisibility(View.VISIBLE);

                // Creating a ingredients fragment
                Fragment_Ingredients frag_ingredients = new Fragment_Ingredients();
                frag_ingredients.setRecipeId(44);

                // Add the fragment to its container using a transaction
                fragmentManager.beginTransaction()
                        .add(R.id.container_ingredients, frag_ingredients)
                        .commit();
            }

//            } else {
//
//                Log.i(Constants.LOG_TAG, "Show Video");
//
//                frame_media_video.setVisibility(View.VISIBLE);
//                frame_ingredients.setVisibility(View.GONE);
//
//                // Creating a video fragment
//                Fragment_Media_Video frag_video = new Fragment_Media_Video();
//
//                // Add the fragment to its container using a transaction
//                fragmentManager.beginTransaction()
//                        .add(R.id.container_media_video, frag_video)
//                        .commit();
//
//            }
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

            // Load media Fragment for videos
            replaceVideoMediaFragment(step);


        } else if (step.getThumbnailURL().length() != 0) {
            Utils_General.showToast(this, "We have an Image:" + step.getThumbnailURL());
        }
    }

    private void loadFragmentForVideo() {

    }
}
