package com.programming.kantech.bakingmagic.app.provider;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.programming.kantech.bakingmagic.app.data.model.pojo.Ingredient;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Recipe;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.utils.Constants;

/**
 * Created by patrick on 2017-06-24.
 *
 */

public class Contract_BakingMagic {

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + Constants.CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_RECIPES = RecipeEntry.TABLE_NAME;
    public static final String PATH_INGREDIENTS = IngredientEntry.TABLE_NAME;
    public static final String PATH_STEPS = StepsEntry.TABLE_NAME;

    public static final class StepsEntry implements BaseColumns {

        // IngredientEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        public static final String TABLE_NAME = "recipe_steps";

        //public static final String _ID = "_id";
        public static final String COLUMN_STEP_ID = "step_id";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_STEP_DESC = "step_desc";
        public static final String COLUMN_STEP_SHORT_DESC = "step_short_desc";
        public static final String COLUMN_STEP_VIDEO_URL = "step_video_url";
        public static final String COLUMN_STEP_IMAGE_URL = "step_image_url";

        /**
         * Return a Uri that points to the row containing a given id.
         *
         * @param id id of the step
         * @return Uri
         */
        public static Uri buildStepUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Create an Ingredient object with the data from a cursor.
         *
         * @param cursor cursor containing the ingredient object
         * @return Step
         */
        public static Step getStepFromCursor(Cursor cursor) {

            Step step = new Step();

            step.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STEP_ID)));

            step.setRecipe_id(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID)));

            step.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STEP_DESC)));
            step.setShortDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STEP_SHORT_DESC)));
            step.setThumbnailURL(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STEP_IMAGE_URL)));
            step.setVideoURL(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STEP_VIDEO_URL)));


            return step;
        }
    }


    public static final class IngredientEntry implements BaseColumns {

        // IngredientEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        public static final String TABLE_NAME = "recipe_ingredients";

        //public static final String _ID = "_id";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
        public static final String COLUMN_INGREDIENT_QTY = "ingredient_qty";
        public static final String COLUMN_INGREDIENT_UOM = "ingedient_uom";

        /**
         * Return a Uri that points to the row containing a given id.
         *
         * @param id id of the ingredient
         * @return Uri
         */
        public static Uri buildIngredientUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Create an Ingredient object with the data from a cursor.
         *
         * @param cursor cursor containing the ingredient object
         * @return Ingredient
         */
        public static Ingredient getIngredientFromCursor(Cursor cursor) {

            Ingredient ingredient = new Ingredient();

            ingredient.setRecipe_id(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID)));

            ingredient.setIngredient(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)));
            ingredient.setMeasure(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_UOM)));
            ingredient.setQuantity(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_QTY)));


            return ingredient;
        }
    }

    public static final class RecipeEntry implements BaseColumns {

        // RecipeEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_NAME = "recipes";

        //public static final String _ID = "_id";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_RECIPE_SERVINGS = "recipe_servings";
        public static final String COLUMN_RECIPE_IMAGE = "recipe_image";

        /**
         * Return a Uri that points to the row containing a given id.
         *
         * @param id recipe id
         * @return Uri
         */
        public static Uri buildRecipeUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Create a Movie object with the data from a cursor.
         *
         * @param cursor A cursor containing the movie object
         * @return Movie
         */
        public static Recipe getRecipeFromCursor(Cursor cursor) {

            Recipe recipe = new Recipe();

            recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID)));
            recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME)));
            recipe.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_IMAGE)));
            recipe.setServings(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_SERVINGS)));

            return recipe;
        }

    }





}
