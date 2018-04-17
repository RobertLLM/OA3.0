package cn.invonate.ygoa3.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.R;

public class FileWebActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.webView)
    WebView webView;

    private String url;
    private String path;

    private boolean is_local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_web);
        ButterKnife.bind(this);
        title.setText("文件预览");
        IX5WebViewExtension i5 = webView.getX5WebViewExtension();
        Log.i("i5", i5 == null ? "null" : i5.toString());
        WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        url = getIntent().getExtras().getString("url");
        if (url != null) {
            webView.loadUrl(url);
        }
        path = getIntent().getStringExtra("path");
        if (path != null) {
            is_local = true;
            webView.loadUrl("file:///" + Environment.getExternalStorageDirectory() + "/download/" + path);
        }
        webView.setWebViewClient(new YgWebViewClient());
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url,
                                     String message, final JsResult result) {
                //用Android组件替换
                new AlertDialog.Builder(FileWebActivity.this)
                        .setTitle("提示")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                //用Android组件替换
                new AlertDialog.Builder(FileWebActivity.this)
                        .setTitle("提示")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                //用Android组件替换
                new AlertDialog.Builder(FileWebActivity.this)
                        .setTitle("提示")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
                return true;
            }
        });
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }

    // Web视图
    private class YgWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.i("WebUrl", url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i("onPageFinished", url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //加载开始

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

    }

    public class BaseWebChromeClient extends WebChromeClient {

        public static final int FILECHOOSER_RESULTCODE = 10000;

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

        }

        //For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            return true;
        }

        public void onActivityResult(int resultCode, Intent data) {

        }
    }

    class WebChromeClientAboveFive extends BaseWebChromeClient {

        private ValueCallback<Uri[]> mUploadCallbackAboveFive;
        private Activity mActivity;

        public WebChromeClientAboveFive(Activity activity) {
            this.mActivity = activity;
        }

        /**
         * 兼容5.0及以上
         *
         * @param webView
         * @param valueCallback
         * @param fileChooserParams
         * @return
         */
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            Log.i("onShowFileChooser", "11111111");
            mUploadCallbackAboveFive = valueCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            mActivity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            return true;
        }

        @Override
        public void onActivityResult(int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                if (null == mUploadCallbackAboveFive) {
                    return;
                }
                Uri[] results = null;
                if (data != null) {
                    String dataString = data.getDataString();
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        int itemCount = clipData.getItemCount();
                        results = new Uri[itemCount];
                        for (int i = 0; i < itemCount; i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
                mUploadCallbackAboveFive.onReceiveValue(results);
                mUploadCallbackAboveFive = null;
            }
            return;
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            if (!is_local) {
                Log.i("tag", "url=" + url);
                Log.i("tag", "userAgent=" + userAgent);
                Log.i("tag", "contentDisposition=" + contentDisposition);
                Log.i("tag", "mimetype=" + mimetype);
                Log.i("tag", "contentLength=" + contentLength);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }
    }

}
