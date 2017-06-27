package com.programming.kantech.bakingmagic.app.utils;

import android.content.ContentValues;

import com.programming.kantech.bakingmagic.app.data.model.pojo.Ingredient;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Recipe;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;

/**
 * Created by patrick keogh on 2017-06-23.
 */

public class Utils_ContentValues {

    /**
     * Extract the values from a recipe to be used with a database.
     *
     * @param recipe The recipe object to be extracted
     * @return result ContentValues instance with the value of the recipe
     */
    public static ContentValues extractRecipeValues(Recipe recipe) {
        ContentValues result = new ContentValues();
        //result.put(GatheringEntry._ID, gathering.getId());
        result.put(Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_ID, recipe.getId());
        result.put(Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_NAME, recipe.getName());
        result.put(Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_IMAGE, recipe.getImage());
        result.put(Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_SERVINGS, recipe.getServings());

        return result;

    }

    /**
     * Extract the values from an ingredient to be used with a database.
     *
     * @param recipe_id The id of the recipe
     * @param ingredient The ingredient object to be extracted
     * @return result ContentValues instance with the value of the ingredient
     */
    public static ContentValues extractIngredientValues(int recipe_id, Ingredient ingredient) {
        ContentValues result = new ContentValues();

        result.put(Contract_BakingMagic.IngredientEntry.COLUMN_RECIPE_ID, recipe_id);
        result.put(Contract_BakingMagic.IngredientEntry.COLUMN_INGREDIENT_NAME, ingredient.getIngredient());
        result.put(Contract_BakingMagic.IngredientEntry.COLUMN_INGREDIENT_QTY, ingredient.getQuantity());
        result.put(Contract_BakingMagic.IngredientEntry.COLUMN_INGREDIENT_UOM, ingredient.getMeasure());

        return result;

    }

    /**
     * Extract the values from an ingredient to be used with a database.
     *
     * @param recipe_id The id of the recipe
     * @param step The ingredient object to be extracted
     * @return result ContentValues instance with the value of the ingredient
     */
    public static ContentValues extractStepValues(int recipe_id, Step step) {
        ContentValues result = new ContentValues();

        result.put(Contract_BakingMagic.StepsEntry.COLUMN_RECIPE_ID, recipe_id);
        result.put(Contract_BakingMagic.StepsEntry.COLUMN_STEP_ID, step.getId());
        result.put(Contract_BakingMagic.StepsEntry.COLUMN_STEP_DESC, step.getDescription());
        result.put(Contract_BakingMagic.StepsEntry.COLUMN_STEP_SHORT_DESC, step.getShortDescription());
        result.put(Contract_BakingMagic.StepsEntry.COLUMN_STEP_IMAGE_URL, step.getThumbnailURL());
        result.put(Contract_BakingMagic.StepsEntry.COLUMN_STEP_VIDEO_URL, step.getVideoURL());

        return result;

    }

    /**
     * Ensure this class is only used as a utility.
     */
    private Utils_ContentValues() {
        throw new AssertionError();
    }



}
