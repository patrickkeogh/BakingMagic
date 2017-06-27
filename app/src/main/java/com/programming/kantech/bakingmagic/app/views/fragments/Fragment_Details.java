package com.programming.kantech.bakingmagic.app.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.utils.Constants;

/**
 * Created by patrick keogh on 2017-06-24.
 */

public class Fragment_Details extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public Fragment_Details() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Load the saved state if there is one
        if(savedInstanceState != null) {
            //mIngredients = savedInstanceState.getParcelableArrayList(Constants.STATE_INFO_INGEDIENTS_LIST);
            //mRecipeId = savedInstanceState.getInt(Constants.STATE_INFO_RECIPE_ID);
        }

        // Inflate the Ingredients List Fragment
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        return rootView;
    }
}
