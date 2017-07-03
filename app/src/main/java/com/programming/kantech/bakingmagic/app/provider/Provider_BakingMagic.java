package com.programming.kantech.bakingmagic.app.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.programming.kantech.bakingmagic.app.utils.Constants;

/**
 * Created by patrick keogh on 2017-06-24.
 *
 */

public class Provider_BakingMagic extends ContentProvider{

    // Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Use DBHelper to manage database creation and version
     * management.
     */
    private DBHelper mOpenHelper;

    // Define final integer constants for the directory of tasks and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int RECIPES_ALL = 100;
    public static final int RECIPES_BY_ID = 101;
    public static final int INGREDIENTS_ALL = 200;
    public static final int INGREDIENTS_BY_ID = 201;
    public static final int STEPS_ALL = 300;
    public static final int STEPS_BY_ID = 301;

    private static final String sRecipeByIdSelection =
            Contract_BakingMagic.RecipeEntry.TABLE_NAME + "." +
                    Contract_BakingMagic.RecipeEntry._ID + " = ? ";


    /**
     * Define a static buildUriMatcher method that associates URI's with their int match
     *
     * @return UriMatcher
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_BakingMagic.PATH_RECIPES, RECIPES_ALL);
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_BakingMagic.PATH_INGREDIENTS, INGREDIENTS_ALL);
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_BakingMagic.PATH_STEPS, STEPS_ALL);

        // replaces # with int id
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_BakingMagic.PATH_RECIPES + "/#", RECIPES_BY_ID);
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_BakingMagic.PATH_INGREDIENTS + "/#", INGREDIENTS_BY_ID);
        uriMatcher.addURI(Constants.CONTENT_AUTHORITY, Contract_BakingMagic.PATH_STEPS + "/#", STEPS_BY_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        // Initialize the database helper
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        String id;

        // Query for the tasks directory and write a default case
        switch (match) {
            case RECIPES_ALL:
                //Log.i(Constants.LOG_TAG, "QUERY URI MATCH:MOVIES_ALL:" + uri);
                retCursor = db.query(Contract_BakingMagic.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENTS_ALL:
                //Log.i(Constants.LOG_TAG, "QUERY URI MATCH:INGREDIENTS_ALL:" + uri);
                retCursor = db.query(Contract_BakingMagic.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case STEPS_ALL:
                //Log.i(Constants.LOG_TAG, "QUERY URI MATCH:STEPS_ALL:" + uri);
                retCursor = db.query(Contract_BakingMagic.StepsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case INGREDIENTS_BY_ID:
                //Log.i(Constants.LOG_TAG, "QUERY URI MATCH:INGREDIENTS_BY_ID:" + uri);

                id = uri.getLastPathSegment();

                retCursor = db.query(Contract_BakingMagic.RecipeEntry.TABLE_NAME,
                        projection,
                        "recipe_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);

                break;
            case RECIPES_BY_ID:
                //Log.i(Constants.LOG_TAG, "QUERY URI MATCH:MOVIE_BY_ID:" + uri);

                id = uri.getLastPathSegment();

                retCursor = db.query(Contract_BakingMagic.RecipeEntry.TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        // Get access to the database (to write new data to)
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        //Log.i(Constants.LOG_TAG, "Match:" + match);

        // URI to be returned
        Uri returnUri;

        long id;

        switch (match) {
            case INGREDIENTS_ALL:
                //Log.i(Constants.LOG_TAG, "INSERT INGREDIENTS_ALL called");
                id = db.insert(Contract_BakingMagic.IngredientEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(Contract_BakingMagic.IngredientEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            case STEPS_ALL:
                //Log.i(Constants.LOG_TAG, "INSERT STEPS_ALL called");
                id = db.insert(Contract_BakingMagic.StepsEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(Contract_BakingMagic.StepsEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case RECIPES_ALL:
                //Log.i(Constants.LOG_TAG, "INSERT RECIPES_ALL called");
                id = db.insert(Contract_BakingMagic.RecipeEntry.TABLE_NAME, null, values);

                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(Contract_BakingMagic.RecipeEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] args) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        // Keep track of the number of deleted tasks
        int rowsDeleted = 0; // starts as 0
        String id;

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            case RECIPES_ALL:
                // Use selections/selectionArgs to filter
                rowsDeleted = db.delete(Contract_BakingMagic.RecipeEntry.TABLE_NAME, selection, args);
                break;
            case INGREDIENTS_ALL:
                // Use selections/selectionArgs to filter
                rowsDeleted = db.delete(Contract_BakingMagic.IngredientEntry.TABLE_NAME, selection, args);
                break;
            case STEPS_ALL:
                // Use selections/selectionArgs to filter
                rowsDeleted = db.delete(Contract_BakingMagic.StepsEntry.TABLE_NAME, selection, args);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Delete uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (rowsDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int rowsInserted = 0;

        switch (sUriMatcher.match(uri)) {

            case RECIPES_ALL:
                db.beginTransaction();

                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(Contract_BakingMagic.RecipeEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) getContext().getContentResolver().notifyChange(uri, null);

                return rowsInserted;

            case INGREDIENTS_ALL:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(Contract_BakingMagic.IngredientEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) getContext().getContentResolver().notifyChange(uri, null);

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
