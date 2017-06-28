package com.programming.kantech.bakingmagic.app.utils;


import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;

/**
 * Created by patrick keogh on 2017-06-23.
 * Class that contains all the Constants required in our Baking Magic App
 */

public class Constants {

    /**
     * Debugging tag used by the Android logger.
     */
    public final static String LOG_TAG = "KanTech Baking App:";

    public static final String BASE_PATH = "http://go.udacity.com/";

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.programming.kantech.bakingmagic.app";



    /**
     * The Constants used for data added as extras to intents
     */
    public static final String EXTRA_RECIPE = "extra_recipe";
    public static final String EXTRA_STEP = "extra_step";
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";
    public static final String EXTRA_RECIPE_NAME = "extra_recipe_name";
    public static final String EXTRA_INGREDIENTS_LIST = "extra_ingredients_list";

    /**
     * The Constants used for data added to savedInstanceState
     */

    public static final String STATE_INFO_RECIPE_ID = "state_info_recipe_id";
    public static final String STATE_INFO_RECIPE = "state_info_recipe";
    public static final String STATE_INFO_RECIPE_NAME = "state_info_recipe_name";
    public static final String STATE_INFO_INGEDIENTS_LIST = "state_info_ingredients_list";

    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    public static final int RECIPE_DETAIL_LOADER = 1;
    public static final int INGREDIENT_DETAIL_LOADER = 2;
    public static final int STEPS_DETAIL_LOADER = 3;

    /*
     * The columns of data that we are interested in displaying within our activity list of
     * recipe names
     */
    public static final String[] LOADER_RECIPE_DETAIL_COLUMNS = {
            Contract_BakingMagic.RecipeEntry._ID,
            Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_ID,
            Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_NAME,
            Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_IMAGE,
            Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_SERVINGS
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int COL_ID = 0;
    public static final int COL_RECIPE_ID = 1;
    public static final int COL_RECIPE_NAME = 2;
    public static final int COL_RECIPE_IMAGE = 3;
    public static final int COL_RECIPE_SERVINGS = 4;

    /*
     * The columns of data that we are interested in displaying within our activity list of
     * ingredients
     */
    public static final String[] LOADER_INGREDIENT_DETAIL_COLUMNS = {
            Contract_BakingMagic.IngredientEntry._ID,
            Contract_BakingMagic.IngredientEntry.COLUMN_RECIPE_ID,
            Contract_BakingMagic.IngredientEntry.COLUMN_INGREDIENT_NAME,
            Contract_BakingMagic.IngredientEntry.COLUMN_INGREDIENT_UOM,
            Contract_BakingMagic.IngredientEntry.COLUMN_INGREDIENT_QTY
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int COL_INGREDIENT_ID = 0;
    public static final int COL_INGREDIENT_RECIPE_ID = 1;
    public static final int COL_INGREDIENT_NAME = 2;
    public static final int COL_INGREDIENT_UOM = 3;
    public static final int COL_INGREDIENT_QTY = 4;

    /*
     * The columns of data that we are interested in displaying within our activity list of
     * ingredients
     */
    public static final String[] LOADER_STEP_DETAIL_COLUMNS = {
            Contract_BakingMagic.StepsEntry._ID,
            Contract_BakingMagic.StepsEntry.COLUMN_RECIPE_ID,
            Contract_BakingMagic.StepsEntry.COLUMN_STEP_ID,
            Contract_BakingMagic.StepsEntry.COLUMN_STEP_DESC,
            Contract_BakingMagic.StepsEntry.COLUMN_STEP_SHORT_DESC,
            Contract_BakingMagic.StepsEntry.COLUMN_STEP_IMAGE_URL,
            Contract_BakingMagic.StepsEntry.COLUMN_STEP_VIDEO_URL
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int COL_STEP_ID = 0;
    public static final int COL_STEP_RECIPE_ID = 1;
    public static final int COL_STEP_STEP_ID = 2;
    public static final int COL_STEP_DESC = 3;
    public static final int COL_STEP_SHORT_DESC = 4;
    public static final int COL_STEP_IMAGE_URL = 5;
    public static final int COL_STEP_VIDEO_URL = 6;



}

