package com.programming.kantech.bakingmagic.app.media;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceView;

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

// This is a singleton class copied from
// https://medium.com/tall-programmer/fullscreen-functionality-with-android-exoplayer-5fddad45509f

public class ExoPlayerVideoHandler {

    private static ExoPlayerVideoHandler instance;

    public static ExoPlayerVideoHandler getInstance() {
        if (instance == null) {
            instance = new ExoPlayerVideoHandler();
        }
        return instance;
    }

    private SimpleExoPlayer mPlayer;
    private Uri mPlayerUri;
    private boolean isPlayerPlaying;

    private ExoPlayerVideoHandler() {
    }

    public void prepareExoPlayerForUri(Context context, Uri uri, SimpleExoPlayerView exoPlayerView) {

        if (context != null && uri != null && exoPlayerView != null) {
            if (!uri.equals(mPlayerUri) || mPlayer == null) {

                // Create a new player if the player is null or
                // we want to play a new video
                mPlayerUri = uri;


                // Do all the standard ExoPlayer code here...

                // Create an instance of the ExoPlayer.
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
                //exoPlayerView.setPlayer(mPlayer);

                // Prepare the MediaSource.
                String userAgent = Util.getUserAgent(context, "BakingMagicApp");
                MediaSource mediaSource = new ExtractorMediaSource(mPlayerUri, new DefaultDataSourceFactory(
                        context, userAgent), new DefaultExtractorsFactory(), null, null);
                mPlayer.prepare(mediaSource);
                //mPlayer.setPlayWhenReady(true);
            }

            // Clears any Surface, SurfaceHolder, SurfaceView
            // or TextureView currently set on the player.
            mPlayer.clearVideoSurface();


            // Sets the SurfaceView onto which video will be rendered.
            mPlayer.setVideoSurfaceView(
                    (SurfaceView) exoPlayerView.getVideoSurfaceView());
            mPlayer.seekTo(mPlayer.getCurrentPosition() + 1);
            exoPlayerView.setPlayer(mPlayer);
        }
    }

    public void releaseVideoPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
        }
        mPlayer = null;
    }

    public void goToBackground() {
        if (mPlayer != null) {
            isPlayerPlaying = mPlayer.getPlayWhenReady();
            mPlayer.setPlayWhenReady(false);
        }
    }

    public void goToForeground() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(isPlayerPlaying);
        }
    }
}
