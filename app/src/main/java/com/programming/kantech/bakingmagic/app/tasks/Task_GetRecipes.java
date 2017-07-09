package com.programming.kantech.bakingmagic.app.tasks;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;

import com.programming.kantech.bakingmagic.app.data.model.pojo.Ingredient;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Recipe;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;
import com.programming.kantech.bakingmagic.app.utils.Utils_ContentValues;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by patrick keogh on 2017-06-23.
 *
 */

public class Task_GetRecipes extends AsyncTask<Call, Void, List<Recipe>> {

    private WeakReference<Context> mContext;

    public Task_GetRecipes(WeakReference<Context> mContext) {
        this.mContext = mContext;
    }

    @Override
    protected List<Recipe> doInBackground(Call... params) {
        try {
            Call<List<Recipe>> call = params[0];
            Response<List<Recipe>> response = call.execute();

            return response.body();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {

        List<Ingredient> ingredients;
        List<Step> steps;

        ContentResolver resolver = mContext.get().getContentResolver();

        // Delete all ingredients, steps, and recipes, and then restore them
        resolver.delete(Contract_BakingMagic.IngredientEntry.CONTENT_URI, null, null);
        resolver.delete(Contract_BakingMagic.StepsEntry.CONTENT_URI, null, null);
        resolver.delete(Contract_BakingMagic.RecipeEntry.CONTENT_URI, null, null);

        if (recipes != null) {



            //loop over the recipes and add them to the db
            for (int i = 0; i < recipes.size(); i++) {
                //Log.i(Constants.LOG_TAG, "Recipe:" + recipes.get(i).toString());

                int recipe_id = recipes.get(i).getId();

                // Insert the recipe into the db
                resolver.insert(Contract_BakingMagic.RecipeEntry.CONTENT_URI,
                        Utils_ContentValues.extractRecipeValues(recipes.get(i)));

                // Get a list of the ingredients
                ingredients = recipes.get(i).getIngredients();

                for (int x = 0; x < ingredients.size(); x++) {

                    // Insert the ingredient into the db
                    resolver.insert(Contract_BakingMagic.IngredientEntry.CONTENT_URI,
                            Utils_ContentValues.extractIngredientValues(recipe_id, ingredients.get(x)));
                }

                // Get a list of the steps
                steps = recipes.get(i).getSteps();

                for (int x = 0; x < steps.size(); x++) {

                    // Insert the step into the db
                    resolver.insert(Contract_BakingMagic.StepsEntry.CONTENT_URI,
                            Utils_ContentValues.extractStepValues(recipe_id, steps.get(x)));
                }
            }

        }
    }
}
