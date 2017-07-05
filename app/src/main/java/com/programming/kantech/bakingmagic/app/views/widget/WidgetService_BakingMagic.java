package com.programming.kantech.bakingmagic.app.views.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Ingredient;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.utils.Utils_General;
import com.programming.kantech.bakingmagic.app.utils.Utils_Preferences;

/**
 * Created by patrick keogh on 2017-07-04.
 *
 */

public class WidgetService_BakingMagic extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext());
    }
}

class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;
    //private int mRecipe_id;

    public WidgetRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.i(Constants.LOG_TAG, "onDataSetChanged() called in WidgetRemoteViewsFactory");

        // Get all the ingredients for the fav recipe

        Uri uri = Contract_BakingMagic.IngredientEntry.CONTENT_URI;

        String selection = Contract_BakingMagic.IngredientEntry.COLUMN_RECIPE_ID + "=?";
        String[] selectionArgs = {"" + Utils_Preferences.getFavId(mContext)};

        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(
                uri,
                null,
                selection,
                selectionArgs,
                null
        );
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the ListView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        //Log.i(Constants.LOG_TAG, "getViewAt() called in WidgetRemoteViewsFactory");

        if (mCursor == null || mCursor.getCount() == 0) return null;

        mCursor.moveToPosition(position);

        Ingredient ingredient = Contract_BakingMagic.IngredientEntry.getIngredientFromCursor(mCursor);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_list_item);

        views.setTextViewText(R.id.widget_tv_measurement, Utils_General.getFormattedMeasurement(mContext, ingredient.getQuantity(), ingredient.getMeasure()));
        views.setTextViewText(R.id.widget_tv_ingredient, ingredient.getIngredient());

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        // Test code for list item clicks - not need for this project
//        Bundle extras = new Bundle();
//        extras.putString(Constants.EXTRA_RECIPE_NAME, ingredient.getIngredient());
//        Intent fillInIntent = new Intent();
//        fillInIntent.putExtras(extras);
//        views.setOnClickFillInIntent(R.id.widget_list_item_layout, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the ListView the same
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
