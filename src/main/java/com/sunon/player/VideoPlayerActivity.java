package com.sunon.player;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Window;

/**
 * Created by tcking on 15/10/27.
 */
public class VideoPlayerActivity extends Activity {

    VideoPlayer player;
    String videoUrl = "/storage/emulated/0/消除眼睛疲劳.mp4";
//    String videoUrl = "http://10.1.1.137:88/resources/micro/860/行政公文-《一分钟让你学会写请示》+Logo.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        Config config = getIntent().getParcelableExtra("config");
//        if (config == null || TextUtils.isEmpty(config.url)) {
//            Toast.makeText(this, R.string.giraffe_player_url_empty, Toast.LENGTH_SHORT).show();
//        } else {
            player = new VideoPlayer(this);
//            player.setTitle(config.title);
//            player.setDefaultRetryTime(config.defaultRetryTime);
//            player.setFullScreenOnly(config.fullScreenOnly);
//            player.setScaleType(TextUtils.isEmpty(config.scaleType) ? VideoPlayer.SCALETYPE_FITPARENT : config.scaleType);
//            player.setTitle(TextUtils.isEmpty(config.title) ? "" : config.title);
//            player.setShowNavIcon(config.showNavIcon);
            player.play(videoUrl);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    /**
     * play video
     *
     * @param context
     * @param url     url,title
     */
    public static void play(Activity context, String... url) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("url", url[0]);
        if (url.length > 1) {
            intent.putExtra("title", url[1]);
        }
        context.startActivity(intent);
    }

    public static Config configPlayer(Activity activity) {
        return new Config(activity);
    }

    public static class Config implements Parcelable {

        private Activity activity;
        private String scaleType;
        private boolean fullScreenOnly;
        private long defaultRetryTime = 5 * 1000;
        private String title;
        private String url;
        private boolean showNavIcon = true;


        public Config setTitle(String title) {
            this.title = title;
            return this;
        }


        public Config(Activity activity) {
            this.activity = activity;
        }

        public void play(String url) {
            this.url = url;
            Intent intent = new Intent(activity, VideoPlayerActivity.class);
            intent.putExtra("config", this);
            activity.startActivity(intent);
        }

        public Config setDefaultRetryTime(long defaultRetryTime) {
            this.defaultRetryTime = defaultRetryTime;
            return this;
        }

        public Config setScaleType(String scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public Config setFullScreenOnly(boolean fullScreenOnly) {
            this.fullScreenOnly = fullScreenOnly;
            return this;
        }

        private Config(Parcel in) {
            scaleType = in.readString();
            fullScreenOnly = in.readByte() != 0;
            defaultRetryTime = in.readLong();
            title = in.readString();
            url = in.readString();
            showNavIcon = in.readByte() != 0;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(scaleType);
            dest.writeByte((byte) (fullScreenOnly ? 1 : 0));
            dest.writeLong(defaultRetryTime);
            dest.writeString(title);
            dest.writeString(url);
            dest.writeByte((byte) (showNavIcon ? 1 : 0));
        }

        public static final Creator<Config> CREATOR = new Creator<Config>() {
            public Config createFromParcel(Parcel in) {
                return new Config(in);
            }

            public Config[] newArray(int size) {
                return new Config[size];
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
