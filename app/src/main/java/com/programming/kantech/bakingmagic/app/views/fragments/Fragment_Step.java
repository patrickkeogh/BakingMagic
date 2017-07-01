package com.programming.kantech.bakingmagic.app.views.fragments;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.views.activities.Activity_Details;
import com.programming.kantech.bakingmagic.app.views.activities.Activity_Step;

/**
 * Created by patrick keogh on 2017-06-24.
 */

public class Fragment_Step extends Fragment {

    private Step mStep;
    private int mStepCount;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    private TextView mPreviousStep;
    private TextView mNextStep;

    // Define a new interface StepNavClickListener that triggers a callback in the host activity
    StepNavClickListener mCallback;

    // StepNavClickListener interface, calls a method in the host activity depending on the nav button clicked
    public interface StepNavClickListener {
        void onNextStepSelected();
        void onPreviousStepSelected();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public Fragment_Step() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Load the saved state if there is one,
        // if not get Step from the arguments bundle
        if(savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(Constants.STATE_INFO_STEP);
            mStepCount = savedInstanceState.getInt(Constants.STATE_INFO_STEP_COUNT);
        }else{
            Log.i(Constants.LOG_TAG, "savedInstanceState is null, get data from intent");

            Bundle args = getArguments();
            mStep = args.getParcelable(Constants.EXTRA_STEP);
            mStepCount = args.getInt(Constants.EXTRA_STEP_COUNT);
        }

        // Inflate the step details fragment layout
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        // Initialize the player view.
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);

        TextView tv_title = (TextView) rootView.findViewById(R.id.tv_step_title);
        TextView tv_description = (TextView) rootView.findViewById(R.id.tv_step_description);

        if (mStep != null) {
            tv_title.setText(mStep.getShortDescription());
            tv_description.setText(mStep.getDescription());
            getStepCount();
        }

        // Determine if we have step navigation (only in portrait view)
        if (rootView.findViewById(R.id.layout_for_step_navigation) != null) {

            mPreviousStep = (TextView) rootView.findViewById(R.id.tv_previous_step);
            mPreviousStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity_Step) getActivity()).onPreviousStepSelected();
                }
            });

            mNextStep = (TextView) rootView.findViewById(R.id.tv_next_step);
            mNextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity_Step) getActivity()).onNextStepSelected();

                }
            });
        }


        Log.i(Constants.LOG_TAG, "Step in Fragment Media:" + mStep.toString());

        // Initialize the player.
        initializePlayer(Uri.parse(mStep.getVideoURL()));

        return rootView;
    }

    private void getStepCount() {

        ContentResolver resolver = getActivity().getContentResolver();

        final Cursor mCursor;

        String selection = Contract_BakingMagic.StepsEntry.COLUMN_RECIPE_ID + "=?";
        String[] selectionArgs = {"" + mStep.getRecipe_id()};

                /* URI for all rows of data in our gatherings table */
        Uri uri = Contract_BakingMagic.StepsEntry.CONTENT_URI;

//        mCursor = resolver.query(
//                uri,                        // The content URI of the words table
//                null,                       // The columns to return for each row (projection)
//                selection,                   // Either null, or the word the user entered
//                selectionArgs,                    // Either empty, or the string the user entered
//                null);                       // The sort order for the returned rows

        new AsyncQueryHandler(resolver) {
            @Override
            protected void onQueryComplete(int token, Object cookie,
                                           Cursor cursor) {

                // Check if an error was returned
                if (null == cursor) {

                    Log.i(Constants.LOG_TAG, "There was an error getting the steps for a count");

                    // If the Cursor is empty, the provider found no matches
                } else if (cursor.getCount() < 1) {
                    Log.i(Constants.LOG_TAG, "There was an error, the count returned 0 steps");
                } else {
                    // Get the count
                    mStepCount = cursor.getCount();
                    Log.i(Constants.LOG_TAG, "Count returned is: " + mStepCount);

                    if((mStep.getId()+ 1) < mStepCount){
                        mNextStep.setVisibility(View.VISIBLE);
                    }else{
                        mNextStep.setVisibility(View.INVISIBLE);
                    }

                    if(mStep.getId() == 0){
                        mPreviousStep.setVisibility(View.INVISIBLE);
                    }else{
                        mPreviousStep.setVisibility(View.VISIBLE);
                    }
                }
            }
        }.startQuery(0, null, uri, null, selection, selectionArgs, null);


    }

    /**
     * Release the player when the fragment is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {

        Log.i(Constants.LOG_TAG, "initialize player called()");

        if (mExoPlayer == null) {
            Log.i(Constants.LOG_TAG, "mExoPlayer == null");
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingMagicApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        Log.i(Constants.LOG_TAG, "releasePlayer called()");
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }



}
