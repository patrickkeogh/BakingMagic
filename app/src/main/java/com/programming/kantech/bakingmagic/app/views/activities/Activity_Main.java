package com.programming.kantech.bakingmagic.app.views.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Recipe;
import com.programming.kantech.bakingmagic.app.data.retrofit.ApiClient;
import com.programming.kantech.bakingmagic.app.data.retrofit.ApiInterface;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;
import com.programming.kantech.bakingmagic.app.tasks.Task_GetRecipes;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.views.ui.Adapter_Recipe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;

/**
 * Created by patrick keogh on 2017-06-24.
 *
 */
public class Activity_Main extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, Adapter_Recipe.OnClickHandler {

    private Adapter_Recipe mAdapter;

    @InjectView(R.id.rv_recipes)
    RecyclerView mList;

    @InjectView(R.id.coordinator_layout)
    CoordinatorLayout mCLayout;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCToolbarLayout;

    @InjectView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mContext = this;

        // Set the support action bar
        setSupportActionBar(mToolbar);

        // Set a title for collapsing toolbar layout
        mCToolbarLayout.setTitle(getResources().getString(R.string.app_name));

        // Define the collapsing toolbar title text color
        mCToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        mCToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorWhite));

        //Set a listener to know the current visible state of CollapseLayout
        mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    mToolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                }else{
                    mToolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorTransparent));
                }
            }
        });

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Recipe>> call = apiService.getRecipes();

        WeakReference<Context> mContext = new WeakReference<Context>(this);

        new Task_GetRecipes(mContext).execute(call);

        GridLayoutManager lLayout = new GridLayoutManager(Activity_Main.this, getResources().getInteger(R.integer.numOfCols));

        mList = (RecyclerView)findViewById(R.id.rv_recipes);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(lLayout);



        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        //mGrid.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(Constants.RECIPE_DETAIL_LOADER, null, this);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param loaderId The ID whose loader is to be created.
     * @param args     Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case Constants.RECIPE_DETAIL_LOADER:
                /* URI for all rows of data in our gatherings table */
                Uri uri = Contract_BakingMagic.RecipeEntry.CONTENT_URI;
                /* Sort order: Ascending by name */
                String sortOrder = Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_NAME + " ASC";

                return new android.support.v4.content.CursorLoader(this,
                        uri,
                        Constants.LOADER_RECIPE_DETAIL_COLUMNS,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /**
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Recipe recipe;
        List<Recipe> mRecipes = new ArrayList<>();

        if(data != null) {

            data.moveToFirst();
            while (!data.isAfterLast())
            {
                recipe = Contract_BakingMagic.RecipeEntry.getRecipeFromCursor(data);
                //Log.i(Constants.LOG_TAG, "Recipe in LoadFinished:" + recipe);

                mRecipes.add(recipe);

                data.moveToNext();
            }
        }

        mAdapter = new Adapter_Recipe(Activity_Main.this, mRecipes, this);
        mList.setAdapter(mAdapter);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.clearRecipes();

    }

    /**
     * This method is for responding to clicks from our list.
     *
     * @param recipe The recipe that was clicked in the list
     */
    @Override
    public void onClick(Recipe recipe) {

        //Log.i(Constants.LOG_TAG, "Recipe clicked:" + recipe.getName());

        Intent intent = new Intent(Activity_Main.this, Activity_Details.class);
        intent.putExtra(Constants.EXTRA_RECIPE, recipe);
        startActivity(intent);
    }
}
