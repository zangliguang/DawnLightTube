//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.2.
//


package zang.liguang.tube.activity;

import java.io.Serializable;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;
import zang.liguang.tube.R.id;
import zang.liguang.tube.R.layout;
import zang.liguang.tube.bean.YouTubeVideoModle;
import zang.liguang.tube.view.dragpanel.DraggablePanel;

public final class YoutubeVideoPlayActivity_
    extends YoutubeVideoPlayActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String YOU_TUBE_VIDEO_MODLE_EXTRA = "youTubeVideoModle";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_youtube_video_play);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        injectExtras_();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static YoutubeVideoPlayActivity_.IntentBuilder_ intent(Context context) {
        return new YoutubeVideoPlayActivity_.IntentBuilder_(context);
    }

    public static YoutubeVideoPlayActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new YoutubeVideoPlayActivity_.IntentBuilder_(fragment);
    }

    public static YoutubeVideoPlayActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new YoutubeVideoPlayActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        draggablePanel = ((DraggablePanel) hasViews.findViewById(id.draggable_panel));
        initView();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(YOU_TUBE_VIDEO_MODLE_EXTRA)) {
                youTubeVideoModle = ((YouTubeVideoModle) extras_.getSerializable(YOU_TUBE_VIDEO_MODLE_EXTRA));
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<YoutubeVideoPlayActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, YoutubeVideoPlayActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), YoutubeVideoPlayActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), YoutubeVideoPlayActivity_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent, requestCode);
                } else {
                    super.startForResult(requestCode);
                }
            }
        }

        public YoutubeVideoPlayActivity_.IntentBuilder_ youTubeVideoModle(YouTubeVideoModle youTubeVideoModle) {
            return super.extra(YOU_TUBE_VIDEO_MODLE_EXTRA, ((Serializable) youTubeVideoModle));
        }

    }

}
