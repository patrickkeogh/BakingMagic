package com.programming.kantech.bakingmagic.app.views.activities;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Recipe;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;
import com.programming.kantech.bakingmagic.app.services.Service_WidgetUpdate;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.utils.Utils_General;
import com.programming.kantech.bakingmagic.app.utils.Utils_Preferences;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_DetailsList;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_Ingredients;
import com.programming.kantech.bakingmagic.app.views.fragments.Fragment_Step;

import java.lang.ref.WeakReference;

public class Activity_Details extends AppCompatActivity implements Fragment_Step.StepNavClickListener,
        Fragment_DetailsList.StepClickListener {

    // Track whether to display a two-column or single-column UI
    // A single-column display refers to phone screens, and two-column to larger tablet screens
    private boolean mTwoCols;
    private Recipe mRecipe;
    private static Step mStep;

    private MenuItem menu_fav;

    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.LOG_TAG, "onCreate called in Activity_Details");

        setContentView(R.layout.activity_details);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {

            Log.i(Constants.LOG_TAG, "Activity_Details savedInstanceState is not null");
            if (savedInstanceState.containsKey(Constants.STATE_INFO_RECIPE)) {
                Log.i(Constants.LOG_TAG, "we found the recipe key in savedInstanceState");
                mRecipe = savedInstanceState.getParcelable(Constants.STATE_INFO_RECIPE);
            }

            if (savedInstanceState.containsKey(Constants.STATE_INFO_STEP)) {
                Log.i(Constants.LOG_TAG, "we found the step key in savedInstanceState");
                mStep = savedInstanceState.getParcelable(Constants.STATE_INFO_STEP);
            }

            if (savedInstanceState.containsKey(Constants.STATE_INFO_DETAILS_FRAGMENT)) {
                Log.i(Constants.LOG_TAG, "we found the fragemnt key in savedInstanceState");
                String tag = savedInstanceState.getString(Constants.STATE_INFO_DETAILS_FRAGMENT);

                Log.i(Constants.LOG_TAG, "tag:" + tag);

                mFragment = new Fragment();
                mFragment = mFragmentManager.findFragmentByTag(tag);
            }

        } else {
            Log.i(Constants.LOG_TAG, "Activity_Details savedInstanceState is null, get data from intent");
            mRecipe = getIntent().getParcelableExtra(Constants.EXTRA_RECIPE);
        }

        // Set the action bar back button to look like an up button
        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(mRecipe.getName());
        }

        if (savedInstanceState == null) {
            // Add master list fragment to the screen
            addMasterListFragment();
        }


        // Determine if you're creating a two-pane or single-pane display
        if (findViewById(R.id.layout_for_two_cols) != null) {
            Log.i(Constants.LOG_TAG, "We have 2 columns to show");

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            // This LinearLayout will only initially exist in the two-col tablet case
            mTwoCols = true;

            if (savedInstanceState == null) {
                // Start by showing the ingredients
                addIngredientsFragment();
            } else {
                replaceMasterListFragment();

                if (mFragment instanceof Fragment_Ingredients) {
                    Log.i(Constants.LOG_TAG, "Fragement instanceof Ingredients");

                    replaceDetailsFragmentWithIngredientsFrag();
                } else if (mFragment instanceof Fragment_Step) {
                    Log.i(Constants.LOG_TAG, "Fragement instanceof Step");
                    replaceDetailsFragmentWithStepFrag();
                }
            }

        } else {
            Log.i(Constants.LOG_TAG, "We have 1 column to show");
            mTwoCols = false;

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);

            }

            if (mFragment instanceof Fragment_Ingredients) {
                replaceDetailsFragmentWithIngredientsFrag();
            } else if (mFragment instanceof Fragment_Step) {
                replaceDetailsFragmentWithStepFrag();
            } else {
                replaceMasterListFragment();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(Constants.LOG_TAG, "onBackPressed()");

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);

        // Change the image depnding on if this recipe
        // is already the users favorite or not

        int fav_id = Utils_Preferences.getFavId(this);

        menu_fav = menu.getItem(0);

        if(mRecipe.getId() == fav_id){
            menu_fav.setIcon(R.drawable.ic_favorite_accent_24dp);
        }else {
            menu_fav.setIcon(R.drawable.ic_favorite_border_accent_24dp);
        }

        return true; //true means been handled ...

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {

            int fav_id = Utils_Preferences.getFavId(this);

            if(fav_id == mRecipe.getId()){
                Utils_Preferences.saveFavId(this, 0);
                menu_fav.setIcon(R.drawable.ic_favorite_border_accent_24dp);
                Utils_General.showToast(this, getString(R.string.toast_fav_old));

                // Update the home screen widget to show the ingredients for the new favorite recipe
                Service_WidgetUpdate.startActionUpdateBakingWidget(this);
            }else{
                Utils_Preferences.saveFavId(this, mRecipe.getId());
                menu_fav.setIcon(R.drawable.ic_favorite_accent_24dp);
                Utils_General.showToast(this, getString(R.string.toast_fav_new));

                // Update the home screen widget to show the ingredients for the new favorite recipe
                Service_WidgetUpdate.startActionUpdateBakingWidget(this);
            }
        }

        return super.onOptionsItemSelected(item); // let app handle it
    }

    private void addIngredientsFragment() {
        Log.i(Constants.LOG_TAG, "addIngredientsFragment called()");

        Fragment_Ingredients frag = Fragment_Ingredients.newInstance(mRecipe.getId());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.container_details, frag, Constants.TAG_FRAGMENT_INGREDIENTS);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    public void addMasterListFragment() {

        Log.i(Constants.LOG_TAG, "AddMasterListFragment called()");

        Fragment_DetailsList frag = Fragment_DetailsList.newInstance(mRecipe.getId());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.container_master, frag, Constants.TAG_FRAGMENT_MASTER);
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    public void replaceMasterListFragment() {

        Log.i(Constants.LOG_TAG, "ReplaceMasterListFragment called()");

        Fragment_DetailsList frag = Fragment_DetailsList.newInstance(mRecipe.getId());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.container_master, frag, Constants.TAG_FRAGMENT_MASTER);
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    public void replaceDetailsFragmentWithStepFrag() {

        Log.i(Constants.LOG_TAG, "replaceDetailsFragmentWithStepFrag called()");

        Fragment_Step frag = Fragment_Step.newInstance(mStep);

        // Put this information in a Bundle and attach it to an Intent that will launch an Ingredients Activity
        //Bundle bundle = new Bundle();
        //bundle.putParcelable(Constants.EXTRA_STEP, mStep);
        //bundle.putString(Constants.EXTRA_RECIPE_NAME, mRecipe.getName());

        //Fragment_Step frag = new Fragment_Step();
        //frag.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (mTwoCols) {
            transaction.replace(R.id.container_details, frag, Constants.TAG_FRAGMENT_STEP);
        } else {
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.container_master, frag, Constants.TAG_FRAGMENT_STEP);
            //transaction.addToBackStack(null);
        }

        // Commit the transaction
        transaction.commit();

    }

    public void replaceDetailsFragmentWithIngredientsFrag() {

        Log.i(Constants.LOG_TAG, "replaceDetailsFragmentWithIngredientsFrag called()");

        Fragment_Ingredients frag = Fragment_Ingredients.newInstance(mRecipe.getId());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Log.i(Constants.LOG_TAG, "Activity details replaceDetailsFragmentWithIngredientsFrag TWOCols:" + mTwoCols);
        if (mTwoCols) {

            transaction.replace(R.id.container_details, frag, Constants.TAG_FRAGMENT_INGREDIENTS);
        } else {
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.container_master, frag, Constants.TAG_FRAGMENT_INGREDIENTS);
            //transaction.addToBackStack(null);
        }

        // Commit the transaction
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(Constants.LOG_TAG, "onSaveInstanceState() called in Activity_Details");
        outState.putParcelable(Constants.STATE_INFO_RECIPE, mRecipe);
        outState.putParcelable(Constants.STATE_INFO_STEP, mStep);

        // Store the currently visible fragment depending on the orientation
        if (mTwoCols) {

            String fragmentTag = mFragmentManager.findFragmentById(R.id.container_details).getTag();
            //Log.i(Constants.LOG_TAG, "fragemnt tag:" + fragmentTag);
            outState.putString(Constants.STATE_INFO_DETAILS_FRAGMENT, fragmentTag);
        } else {
            String fragmentTag = mFragmentManager.findFragmentById(R.id.container_master).getTag();
            //Log.i(Constants.LOG_TAG, "fragemnt tag:" + fragmentTag);
            outState.putString(Constants.STATE_INFO_DETAILS_FRAGMENT, fragmentTag);
        }

        //Save the fragment's instance
        //getSupportFragmentManager().putFragment(outState, "myFragmentName", mContent);
    }

    @Override
    public void onStepSelected(Step step) {
        mStep = step;
        replaceDetailsFragmentWithStepFrag();
    }

    @Override
    public void onNextStepSelected() {

        /* URI for all rows of data in our step table */
        Uri uri = Contract_BakingMagic.StepsEntry.CONTENT_URI;

        // Assuming the validation in the fragment worked, we should not get requests
        // that we dont have steps for
        int nextId = mStep.getId() + 1;

        String selection = Contract_BakingMagic.StepsEntry.COLUMN_STEP_ID + "=?";
        String[] selectionArgs = {"" + nextId};

        GetStepAsyncQueryHandler handler = new GetStepAsyncQueryHandler(this);

        handler.startQuery(0, null, uri, null, selection, selectionArgs, null);

    }

    @Override
    public void onPreviousStepSelected() {

        /* URI for all rows of data in our step table */
        Uri uri = Contract_BakingMagic.StepsEntry.CONTENT_URI;

        // Assuming the validation in the fragment worked, we should not get requests
        // that we dont have steps for
        int nextId = mStep.getId() - 1;

        String selection = Contract_BakingMagic.StepsEntry.COLUMN_STEP_ID + "=?";
        String[] selectionArgs = {"" + nextId};

        GetStepAsyncQueryHandler handler = new GetStepAsyncQueryHandler(this);

        handler.startQuery(0, null, uri, null, selection, selectionArgs, null);

    }

    /**
     * My implementation of the AsyncQueryHandler.
     */
    private static class GetStepAsyncQueryHandler extends AsyncQueryHandler {

        private final WeakReference<Activity_Details> mActivity;

        private GetStepAsyncQueryHandler(Activity activity) {
            super(activity.getContentResolver());
            mActivity = new WeakReference<>((Activity_Details) activity);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

            // ToDo: Do I still need to call this?????
            super.onQueryComplete(token, cookie, cursor);

            // Check if an error was returned
            if (null == cursor || cursor.getCount() == 0) {
                Log.i(Constants.LOG_TAG, "There was an error getting the steps for a count");
            } else {
                cursor.moveToFirst();

                // Replace the fragment with the new step
                mStep = Contract_BakingMagic.StepsEntry.getStepFromCursor(cursor);


                mActivity.get().replaceDetailsFragmentWithStepFrag();

            }
        }
    }
}