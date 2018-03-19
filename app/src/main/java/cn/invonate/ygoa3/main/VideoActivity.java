package cn.invonate.ygoa3.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoActivity extends AppCompatActivity {

    @BindView(R.id.jc_video)
    JCVideoPlayerStandard jcVideo;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        url = getIntent().getExtras().getString("url");
        jcVideo.setUp(HttpUtil.URL_FILE + url, JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, "");
        jcVideo.onAutoCompletion();
        jcVideo.startButton.performClick();
        jcVideo.fullscreenButton.setVisibility(View.GONE);
        jcVideo.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
