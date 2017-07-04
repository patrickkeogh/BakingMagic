package com.programming.kantech.bakingmagic.app.views.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Recipe;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.views.activities.Activity_Details;

import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider_BakingMagic extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.i(Constants.LOG_TAG, "updateAppWidget() called in WidgetProvider_BakingMagic");

        // Construct the RemoteViews object
        RemoteViews views = getRemoteView(context);

//        // Create an Intent to launch MainActivity when clicked
//        Intent intent = new Intent(context, Activity_Details.class);
//
//        ContentResolver resolver = context.getContentResolver();
//
//        Uri uri = Contract_BakingMagic.RecipeEntry.CONTENT_URI;
//
//        String selection = Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_ID + "=?";
//        String[] selectionArgs = {"1"};
//
//        // Get the fav recipe from the db
//        Cursor cursor = resolver.query(uri,null,selection,selectionArgs,null);
//
//        Recipe favRecipe;
//
//        if (cursor != null && cursor.getCount() >0) {
//            cursor.moveToFirst();
//
//                favRecipe = Contract_BakingMagic.RecipeEntry.getRecipeFromCursor(cursor);
//                Log.i(Constants.LOG_TAG, "Recipe in Widget:" + favRecipe.getName());
//
//                intent.putExtra(Constants.EXTRA_RECIPE, favRecipe);
//
//                views.setTextViewText(R.id.widget_recipe_name, favRecipe.getName());
//
//            cursor.close();
//
//        }else{
//
//            throw new RuntimeException("Favorite Recipe not found!");
//        }
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//        // Widgets allow click handlers to only launch pending intents
//        views.setOnClickPendingIntent(R.id.widget_app_name, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    /**
     * Creates and returns the RemoteViews to be displayed in the widget
     *
     * @param context The context
     * @return The RemoteViews for the widget
     */
    private static RemoteViews getRemoteView(Context context) {
        Log.i(Constants.LOG_TAG, "getRemoteView() called in WidgetProvider_BakingMagic");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);

        // Set the WidgetService_BakingMagic intent to act as the adapter for the ListView
        //Intent intent_service = new Intent(context, WidgetService_BakingMagic.class);
        //views.setRemoteAdapter(R.id.widget_list_view_ingredients, intent_service);

        Intent intent = new Intent(context, Activity_Details.class);

        // Set the PlantDetailActivity intent to launch when clicked
        //Intent appIntent = new Intent(context, Activity_Details.class);

        ContentResolver resolver = context.getContentResolver();

        Uri uri = Contract_BakingMagic.RecipeEntry.CONTENT_URI;

        String selection = Contract_BakingMagic.RecipeEntry.COLUMN_RECIPE_ID + "=?";
        String[] selectionArgs = {"1"};

        // Get the fav recipe from the db
        Cursor cursor = resolver.query(uri, null, selection, selectionArgs, null);

        Recipe favRecipe;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            favRecipe = Contract_BakingMagic.RecipeEntry.getRecipeFromCursor(cursor);
            Log.i(Constants.LOG_TAG, "Recipe in Widget:" + favRecipe.toString());

            Log.i(Constants.LOG_TAG, "Add Recicpe to intent");

            intent.putExtra(Constants.EXTRA_RECIPE, favRecipe);

            views.setTextViewText(R.id.widget_recipe_name, favRecipe.getName());

            cursor.close();

        } else {

            throw new RuntimeException("Favorite Recipe not found!");
        }

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            Iterator<String> it = keys.iterator();
            Log.e(Constants.LOG_TAG, "Dumping Intent start");
            while (it.hasNext()) {
                String key = it.next();
                Log.e(Constants.LOG_TAG, "[" + key + "=" + bundle.get(key) + "]");
            }
            Log.e(Constants.LOG_TAG, "Dumping Intent end");
        }else{
            Log.e(Constants.LOG_TAG, "Bundle is empty");


        }

        //PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //views.setPendingIntentTemplate(R.id.widget_list_view_ingredients, appPendingIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget_app_name, pendingIntent);


        // Handle empty favs list
        views.setEmptyView(R.id.widget_list_view_ingredients, R.id.empty_view);

        return views;
    }

    // Called anytime any widget option is changed
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {

        // Update widget

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(Constants.LOG_TAG, "getRemoteView() called in WidgetProvider_BakingMagic");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

