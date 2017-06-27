package com.programming.kantech.bakingmagic.app.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by patrick keogh on 2017-06-24.
 */

public class DBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 4;

    // Name of the sql database on the device
    private static final String DATABASE_NAME = "baking_magic_project.db";

    /**
     * Constructor for DatabaseHelper
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + Contract_BakingMagic.IngredientEntry.TABLE_NAME
                + " (" + Contract_BakingMagic.IngredientEntry._ID + " INTEGER PRIMARY KEY, "
                + Contract_BakingMagic.IngredientEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, "
                + Contract_BakingMagic.IngredientEntry.COLUMN_INGREDIENT_QTY + " REAL NOT NULL, "
                + Contract_BakingMagic.IngredientEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, "
                + Contract_BakingMagic.IngredientEntry.COLUMN_INGREDIENT_UOM + " TEXT NOT NULL " + " );";

        final String SQL_CREATE_STEPS_TABLE = "CREATE TABLE " + Contract_BakingMagic.StepsEntry.TABLE_NAME
                + " (" + Contract_BakingMagic.StepsEntry._ID + " INTEGER PRIMARY KEY, "
                + Contract_BakingMagic.StepsEntry.COLUMN_STEP_ID + " INTEGER NOT NULL, "
                + Contract_BakingMagic.StepsEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, "
                + Contract_BakingMagic.StepsEntry.COLUMN_STEP_DESC + " TEXT NOT NULL, "
                + Contract_BakingMagic.StepsEntry.COLUMN_STEP_SHORT_DESC + " TEXT NOT NULL, "
                + Contract_BakingMagic.StepsEntry.COLUMN_STEP_IMAGE_URL + " TEXT NOT NULL, "
                + Contract_BakingMagic.StepsEntry.COLUMN_STEP_VIDEO_URL + " TEXT NOT NULL " + " );";



        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + Contract_BakingMagic.RecipeEntry.TABLE_NAME
                + " (" + Contract_BakingMagic.RecipeEntry._ID + " INTEGER PRIMARY KEY, "
                + Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, "
                + Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_SERVINGS + " INTEGER NOT NULL, "
                + Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, "
                + Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_IMAGE + " TEXT NOT NULL, "

                // Set up the username column as a foreign key to appointment table.
                + " FOREIGN KEY (" + Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_ID + ") REFERENCES " +
                Contract_BakingMagic.IngredientEntry.TABLE_NAME + " (" +
                Contract_BakingMagic.IngredientEntry.COLUMN_RECIPE_ID + ") "
                +" );";

        db.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
        db.execSQL(SQL_CREATE_STEPS_TABLE);
        db.execSQL(SQL_CREATE_RECIPE_TABLE);


    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract_BakingMagic.IngredientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract_BakingMagic.StepsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract_BakingMagic.RecipeEntry.TABLE_NAME);

        onCreate(db);

    }
}
