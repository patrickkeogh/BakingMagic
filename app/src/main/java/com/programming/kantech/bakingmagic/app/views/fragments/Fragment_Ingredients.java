package com.programming.kantech.bakingmagic.app.views.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Ingredient;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.views.ui.Adapter_Details_Ingredients;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick keogh on 2017-06-24.
 */

public class Fragment_Ingredients extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>,
        Adapter_Details_Ingredients.OnClickHandler {

    // Variables to store the id of the recipe that this fragment displays the ingredients for
    private int mRecipeId;
    private String mRecipeName;

    private int mPosition = RecyclerView.NO_POSITION;

    private Adapter_Details_Ingredients mAdapter;

    private RecyclerView rv_ingredients;

    private ProgressBar mLoadingIndicator;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public Fragment_Ingredients() {
    }

    /**
     * Inflates the fragment layout file
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Load the saved state if there is one
        if(savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(Constants.STATE_INFO_RECIPE_ID);
        }else{
            Log.i(Constants.LOG_TAG, "savedInstanceState is null, get data from intent");
            Bundle args = getArguments();
            mRecipeId = args.getInt(Constants.EXTRA_RECIPE_ID);
        }

        // Inflate the Ingredients List Fragment
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        // Get a reference to the RecyclerView in the fragment_ingredients xml layout file
        rv_ingredients = (RecyclerView) rootView.findViewById(R.id.rv_ingredients);

        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        rv_ingredients.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        rv_ingredients.setHasFixedSize(false);

        mAdapter = new Adapter_Details_Ingredients(getActivity(), this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        rv_ingredients.setAdapter(mAdapter);

        getActivity().getSupportLoaderManager().initLoader(Constants.INGREDIENT_DETAIL_LOADER, null, this);

        // Return the rootView
        return rootView;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putInt(Constants.STATE_INFO_RECIPE_ID, mRecipeId);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        showLoading();

        switch (loaderId) {
            case Constants.INGREDIENT_DETAIL_LOADER:

                String selection = Contract_BakingMagic.IngredientEntry.COLUMN_RECIPE_ID + "=?";
                String[] selectionArgs = {"" + mRecipeId};


                /* URI for all rows of data in our gatherings table */
                Uri uri = Contract_BakingMagic.IngredientEntry.CONTENT_URI;

                return new android.support.v4.content.CursorLoader(getActivity(),
                        uri,
                        Constants.LOADER_INGREDIENT_DETAIL_COLUMNS,
                        selection,
                        selectionArgs,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        rv_ingredients.smoothScrollToPosition(mPosition);

        showDataView();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(long id) {

    }

    private void showDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the recycler view is showing */
        rv_ingredients.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the recycler view */
        rv_ingredients.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}
