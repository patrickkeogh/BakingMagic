package com.programming.kantech.bakingmagic.app.views.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Ingredient;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.utils.Utils_General;

/**
 * Created by patri on 2017-06-26.
 */

public class Adapter_Details_Steps extends RecyclerView.Adapter<Adapter_Details_Steps.ViewHolder_Details> {

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    final private OnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface OnClickHandler {
        void onClick(Step step);
    }

    private Cursor mCursor;

    /**
     * Creates an Adapter_Details_Steps.
     *
     * @param context      Used to talk to the UI and app resources
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public Adapter_Details_Steps(@NonNull Context context, OnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public ViewHolder_Details onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_details_master_list, viewGroup, false);

        view.setFocusable(true);

        return new ViewHolder_Details(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder_Details holder, int position) {

        mCursor.moveToPosition(position);

        /* Read date from the cursor */
        Step step = Contract_BakingMagic.StepsEntry.getStepFromCursor(mCursor);

        holder.tv_details.setText("Step: " + step.getShortDescription());


    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    /**
     * Swaps the cursor used by the Adapter for its data. This method is called by the
     * Activity after a load has finished, as well as when the Loader responsible for loading
     * the data is reset. When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newCursor the new cursor to use as this adapter's data source
     */
    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        //Log.i(Constants.LOG_TAG, "swap Cusor called with:" + getItemCount());
        notifyDataSetChanged();
    }

    class ViewHolder_Details extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tv_details;

        public ViewHolder_Details(View view) {
            super(view);
            view.setOnClickListener(this);

            tv_details = (TextView) view.findViewById(R.id.tv_details);
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());
            mClickHandler.onClick(Contract_BakingMagic.StepsEntry.getStepFromCursor(mCursor));

        }
    }

}
