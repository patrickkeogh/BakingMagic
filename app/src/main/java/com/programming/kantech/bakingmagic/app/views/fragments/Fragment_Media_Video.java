package com.programming.kantech.bakingmagic.app.views.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.programming.kantech.bakingmagic.app.utils.Constants;

/**
 * Created by patri on 2017-06-24.
 */

public class Fragment_Media_Video extends Fragment  implements View.OnClickListener {

    private Step mStep;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public Fragment_Media_Video() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Load the saved state if there is one
        if(savedInstanceState != null) {
            //mIngredients = savedInstanceState.getParcelableArrayList(Constants.STATE_INFO_INGEDIENTS_LIST);
            //mRecipeId = savedInstanceState.getInt(Constants.STATE_INFO_RECIPE_ID);
        }

        // Inflate the Ingredients List Fragment
        View rootView = inflater.inflate(R.layout.fragment_media_video, container, false);

        // Initialize the player view.
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);
        mPlayerView.requestFocus();

        TextView tv_title = (TextView) rootView.findViewById(R.id.tv_step_title);
        TextView tv_description = (TextView) rootView.findViewById(R.id.tv_step_description);

        Bundle args = getArguments();
        mStep = args.getParcelable(Constants.EXTRA_STEP);

        if (mStep != null) {
            tv_title.setText(mStep.getShortDescription());
            tv_description.setText(mStep.getDescription());
        }


        Log.i(Constants.LOG_TAG, "Step in Fragment Media:" + mStep.toString());

        // Initialize the player.
        //String videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdae8_-intro-cheesecake/-intro-cheesecake.mp4";

        initializePlayer(Uri.parse(mStep.getVideoURL()));

        return rootView;
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
            String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
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

    @Override
    public void onClick(View view) {

    }
}
