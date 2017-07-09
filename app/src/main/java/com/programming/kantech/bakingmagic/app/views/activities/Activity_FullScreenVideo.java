package com.programming.kantech.bakingmagic.app.views.activities;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.programming.kantech.bakingmagic.app.R;
import com.programming.kantech.bakingmagic.app.data.model.pojo.Step;
import com.programming.kantech.bakingmagic.app.media.ExoPlayerVideoHandler;
import com.programming.kantech.bakingmagic.app.utils.Constants;

public class Activity_FullScreenVideo extends AppCompatActivity {

    private boolean destroyVideo = true;
    private Step mStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(Constants.STATE_INFO_STEP)) {
                Log.i(Constants.LOG_TAG, "we found the step key in savedInstanceState");
                mStep = savedInstanceState.getParcelable(Constants.STATE_INFO_STEP);
            }

        } else {
            Log.i(Constants.LOG_TAG, "Activity_Details savedInstanceState is null, get data from intent");
            mStep = getIntent().getParcelableExtra(Constants.EXTRA_STEP);
        }


    }

    @Override
    protected void onResume(){
        super.onResume();
        SimpleExoPlayerView exoPlayerView =
                (SimpleExoPlayerView)findViewById(R.id.exoplayer_video);

        ExoPlayerVideoHandler.getInstance()
                .prepareExoPlayerForUri(getApplicationContext(),
                        Uri.parse(mStep.getVideoURL()), exoPlayerView);

        ExoPlayerVideoHandler.getInstance().goToForeground();

        findViewById(R.id.exo_fullscreen_button).setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        destroyVideo = false;
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed(){
        destroyVideo = false;
        super.onBackPressed();
    }

    @Override
    protected void onPause(){
        super.onPause();
        ExoPlayerVideoHandler.getInstance().goToBackground();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(destroyVideo){
            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        }
    }
}
