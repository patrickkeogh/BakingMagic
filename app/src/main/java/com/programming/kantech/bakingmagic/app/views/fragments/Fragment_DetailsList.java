package com.programming.kantech.bakingmagic.app.views.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.utils.Utils_General;
import com.programming.kantech.bakingmagic.app.views.ui.Adapter_Details_Steps;

/**
 * Created by patrick keogh on 2017-06-24.
 *
 */

public class Fragment_DetailsList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        Adapter_Details_Steps.OnClickHandler {

    private int mPosition = RecyclerView.NO_POSITION;

    private Adapter_Details_Steps mAdapter;

    private RecyclerView rv_details_list;

    private ProgressBar mLoadingIndicator;

    private int mRecipe_Id;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    StepClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface StepClickListener {
        void onStepSelected(Step step);

        void replaceDetailsFragmentWithIngredientsFrag();
    }


    // Mandatory empty constructor
    public Fragment_DetailsList() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        Bundle args = getArguments();
        mRecipe_Id = args.getInt(Constants.EXTRA_RECIPE_ID);

        //Log.i(Constants.LOG_TAG, "Recipe id in onCreateView(): " + mRecipe_Id);


        // Get a reference to the textview4
        TextView tv_ingredients = (TextView) rootView.findViewById(R.id.tv_ingredients);

        tv_ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils_General.showToast(getActivity(), "We clicked the ingredients text");

                mCallback.replaceDetailsFragmentWithIngredientsFrag();

            }
        });

        // Get a reference to the RecyclerView in the fragment_master_list xml layout file
        rv_details_list = (RecyclerView) rootView.findViewById(R.id.rv_details_list);

        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        rv_details_list.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        rv_details_list.setHasFixedSize(false);

        mAdapter = new Adapter_Details_Steps(getActivity(), this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        rv_details_list.setAdapter(mAdapter);

        getActivity().getSupportLoaderManager().initLoader(Constants.STEPS_DETAIL_LOADER, null, this);

        // Return the rootview
        return rootView;
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (StepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement StepClickListener");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        showLoading();

        switch (loaderId) {
            case Constants.STEPS_DETAIL_LOADER:

                String selection = Contract_BakingMagic.StepsEntry.COLUMN_RECIPE_ID + "=?";
                String[] selectionArgs = {"" + mRecipe_Id};


                /* URI for all rows of data in our gatherings table */
                Uri uri = Contract_BakingMagic.StepsEntry.CONTENT_URI;

                String orberBy = Contract_BakingMagic.StepsEntry.COLUMN_STEP_ID + " ASC";

                return new android.support.v4.content.CursorLoader(getActivity(),
                        uri,
                        Constants.LOADER_STEP_DETAIL_COLUMNS,
                        selection,
                        selectionArgs,
                        orberBy);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        rv_details_list.smoothScrollToPosition(mPosition);

        showDataView();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void showDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the recycler view is showing */
        rv_details_list.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the recycler view */
        rv_details_list.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Step step) {
        mCallback.onStepSelected(step);
    }

}
