package cn.invonate.ygoa.main.work.mail;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa.BaseActivity;
import cn.invonate.ygoa.R;
import cn.invonate.ygoa.Util.MailHolder;

public class FileActivity extends BaseActivity {
    final String digits = "0123456789ABCDEF";

    @BindView(R.id.web_file)
    WebView webFile;

    byte[] base64_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        ButterKnife.bind(this);
        int i = getIntent().getExtras().getInt("i");
        int j = getIntent().getExtras().getInt("j");
        base64_file = MailHolder.mail_model.get(i).getAttachmentsInputStreams().get(j);
        String url = Base64.encodeToString(base64_file, Base64.DEFAULT);
        Log.i("url", url);

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {

    }
}
