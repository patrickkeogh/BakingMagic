package com.programming.kantech.bakingmagic.app.views.fragments;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.media.ExoPlayerVideoHandler;
import com.programming.kantech.bakingmagic.app.provider.Contract_BakingMagic;
import com.programming.kantech.bakingmagic.app.utils.Constants;
import com.programming.kantech.bakingmagic.app.utils.Utils_General;
import com.programming.kantech.bakingmagic.app.views.activities.Activity_FullScreenVideo;

import java.lang.ref.WeakReference;

/**
 * Created by patrick keogh on 2017-06-24.
 *
 */

public class Fragment_Step extends Fragment {

    private Step mStep;

    //private SimpleExoPlayer mExoPlayer;
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

        // Load the saved state if there is one
        if (savedInstanceState != null) {
            Log.i(Constants.LOG_TAG, "Fragment_Step savedInstanceState is not null");
            if (savedInstanceState.containsKey(Constants.STATE_INFO_STEP)) {
                Log.i(Constants.LOG_TAG, "we found the step key in savedInstanceState");
                mStep = savedInstanceState.getParcelable(Constants.STATE_INFO_STEP);
            }

        } else {
            Log.i(Constants.LOG_TAG, "Fragment_Step savedInstanceState is null, get data from intent");
            Bundle args = getArguments();
            mStep = args.getParcelable(Constants.EXTRA_STEP);
        }

        if(!Utils_General.isNetworkAvailable(getContext())){
            Utils_General.showToast(getContext(), getString(R.string.error_no_internet));

        }

        // Inflate the step details fragment layout
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        // Initialize the player view and imageor the image view.
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);

        if (rootView.findViewById(R.id.exo_fullscreen_button) != null) {

            rootView.findViewById(R.id.exo_fullscreen_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), Activity_FullScreenVideo.class);
                            intent.putExtra(Constants.EXTRA_STEP, mStep);
                            getContext().startActivity(intent);
                        }
                    });

        }


        TextView tv_title = (TextView) rootView.findViewById(R.id.tv_step_title);
        TextView tv_description = (TextView) rootView.findViewById(R.id.tv_step_description);

        if (mStep != null) {
            tv_title.setText(mStep.getShortDescription());
            tv_description.setText(mStep.getDescription());

            // set the nextand previous nav buttons if we have any
            if (rootView.findViewById(R.id.layout_for_step_navigation) != null) {

                String selection = Contract_BakingMagic.StepsEntry.COLUMN_RECIPE_ID + "=?";
                String[] selectionArgs = {"" + mStep.getRecipe_id()};

                /* URI for all rows of data in our gatherings table */
                Uri uri = Contract_BakingMagic.StepsEntry.CONTENT_URI;

                StepCountAsyncQueryHandler handler = new StepCountAsyncQueryHandler(getActivity(), this, mStep.getId());
                handler.startQuery(0, null, uri, null, selection, selectionArgs, null);
            }

        }

        // Determine if we have step navigation (only in portrait view)
        if (rootView.findViewById(R.id.layout_for_step_navigation) != null) {

            mPreviousStep = (TextView) rootView.findViewById(R.id.tv_previous_step);
            mPreviousStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onPreviousStepSelected();
                }
            });

            mNextStep = (TextView) rootView.findViewById(R.id.tv_next_step);
            mNextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onNextStepSelected();

                }
            });
        }


        //Log.i(Constants.LOG_TAG, "Step in Fragment Media:" + mStep.toString());

        return rootView;
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (Fragment_Step.StepNavClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement StepClickListener");
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if (Util.SDK_INT > 23) {
//            if (mStep.getVideoURL().length() != 0) {
//                //Utils_General.showToast(getActivity(), "No Image");
//                // Load the exo player and hide the image view
//                mImageView.setVisibility(View.GONE);
//                // Initialize the player.
//                initializePlayer(Uri.parse(mStep.getVideoURL()));
//
//            } else if (mStep.getThumbnailURL().length() != 0) {
//                //Utils_General.showToast(getActivity(), "No video");
//
//                mPlayerView.setVisibility(View.GONE);
//
//                Picasso.with(getActivity())
//                        .load(mStep.getThumbnailURL())
//                        .placeholder(R.drawable.image)
//                        .error(R.drawable.image)
//                        .into(mImageView);
//            } else {
//                // Hide both Image and ExoPlayer
//                mPlayerView.setVisibility(View.GONE);
//                mImageView.setVisibility(View.GONE);
//            }
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        if (Utils_General.isNetworkAvailable(getContext())) {
            if (mStep.getVideoURL().length() != 0) {

                ExoPlayerVideoHandler.getInstance()
                        .prepareExoPlayerForUri(getContext(),
                                Uri.parse(mStep.getVideoURL()), mPlayerView);

                ExoPlayerVideoHandler.getInstance().goToForeground();


            } else {
                // Hide both Image and ExoPlayer
                mPlayerView.setVisibility(View.GONE);
            }
        } else {
            Utils_General.showToast(getContext(), getString(R.string.error_no_internet));
        }
    }


//    @Override
//    public void onPause() {
//        super.onPause();
//        if (Util.SDK_INT <= 23) {
//            releasePlayer();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (Util.SDK_INT > 23) {
//            releasePlayer();
//        }
//    }

    @Override
    public void onPause() {
        super.onPause();
        ExoPlayerVideoHandler.getInstance().goToBackground();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
    }

    /**
     * My implementation of the AsyncQueryHandler.
     */
    private static class StepCountAsyncQueryHandler extends AsyncQueryHandler {

        private final WeakReference<Fragment_Step> mFragment;
        private int stepId;

        private StepCountAsyncQueryHandler(Activity activity, Fragment_Step fragment, int step_id) {
            super(activity.getContentResolver());

            mFragment = new WeakReference<>(fragment);
            this.stepId = step_id;
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            // ToDo: Do I still need to call this?????
            //super.onQueryComplete(token, cookie, cursor);

            // Check if an error was returned
            if (null == cursor) {

                Log.i(Constants.LOG_TAG, "There was an error getting the steps for a count");

                // If the Cursor is empty, the provider found no matches
            } else if (cursor.getCount() < 1) {
                Log.i(Constants.LOG_TAG, "There was an error, the count returned 0 steps");
            } else {
                // Get the count
                int count = cursor.getCount();
                //Log.i(Constants.LOG_TAG, "Count returned is: " + count);

                if ((stepId + 1) < count) {

                    mFragment.get().setVisibilityNextStep(View.VISIBLE);
                } else {
                    mFragment.get().setVisibilityNextStep(View.INVISIBLE);
                }

                if (stepId == 0) {
                    mFragment.get().setVisibilityPreviousStep(View.INVISIBLE);
                } else {
                    mFragment.get().setVisibilityPreviousStep(View.VISIBLE);
                }
            }
        }
    }

//    /**
//     * Release the player when the fragment is destroyed.
//     */
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        releasePlayer();
//    }

//    /**
//     * Initialize ExoPlayer.
//     *
//     * @param mediaUri The URI of the sample to play.
//     */
//    private void initializePlayer(Uri mediaUri) {
//
//        Log.i(Constants.LOG_TAG, "initialize player called()");
//
//        if (mExoPlayer == null) {
//            Log.i(Constants.LOG_TAG, "mExoPlayer == null");
//            // Create an instance of the ExoPlayer.
//            TrackSelector trackSelector = new DefaultTrackSelector();
//            LoadControl loadControl = new DefaultLoadControl();
//            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
//            mPlayerView.setPlayer(mExoPlayer);
//
//            // Prepare the MediaSource.
//            String userAgent = Util.getUserAgent(getContext(), "BakingMagicApp");
//            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
//                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
//            mExoPlayer.prepare(mediaSource);
//            mExoPlayer.setPlayWhenReady(true);
//        }
//    }

    /**
     * Release ExoPlayer.
     */
//    private void releasePlayer() {
//        Log.i(Constants.LOG_TAG, "releasePlayer called()");
//        if (mExoPlayer != null) {
//            mExoPlayer.stop();
//            mExoPlayer.release();
//            mExoPlayer = null;
//        }
//
//    }
    public void setVisibilityPreviousStep(int visible) {
        mPreviousStep.setVisibility(visible);
    }

    public void setVisibilityNextStep(int visible) {
        mNextStep.setVisibility(visible);
    }
}
