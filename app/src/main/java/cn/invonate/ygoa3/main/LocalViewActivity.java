package cn.invonate.ygoa3.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.WpsModel;

public class LocalViewActivity extends BaseActivity implements TbsReaderView.ReaderCallback {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.reader)
    RelativeLayout reader;

    TbsReaderView mTbsReaderView;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_view);
        ButterKnife.bind(this);
        path = getIntent().getExtras().getString("path");
        mTbsReaderView = new TbsReaderView(this, this);
        reader.addView(mTbsReaderView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        displayFile();
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    private void displayFile() {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", getLocalFile().getPath());
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        boolean result = mTbsReaderView.preOpen(getFileType(path), false);
        if (result) {
            mTbsReaderView.openFile(bundle);
        }
    }

    private File getLocalFile() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), path);
    }

    @Override
    protected void onDestroy() {
        mTbsReaderView.onStop();
        super.onDestroy();
    }

    /***
     * 获取文件类型
     *
     * @param path 文件路径
     * @return 文件的格式
     */
    private String getFileType(String path) {
        String str = "";

        if (TextUtils.isEmpty(path)) {
            return str;
        }
        int i = path.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = path.substring(i + 1);
        return str;
    }

    @OnClick({R.id.pic_back, R.id.other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.other:
                boolean flag = openFile(getLocalFile().getPath());
                if (!flag) {
                    Toast.makeText(this, "打开文件失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean openFile(String path) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.Companion.getOPEN_MODE(), WpsModel.OpenMode.INSTANCE.getNORMAL()); // 打开模式
        bundle.putBoolean(WpsModel.Companion.getSEND_CLOSE_BROAD(), true); // 关闭时是否发送广播
        bundle.putString(WpsModel.Companion.getTHIRD_PACKAGE(), getPackageName()); // 第三方应用的包名，用于对改应用合法性的验证
        bundle.putBoolean(WpsModel.Companion.getCLEAR_TRACE(), true);// 清除打开记录
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.INSTANCE.getNORMAL(), WpsModel.ClassName.INSTANCE.getNORMAL());

        File file = new File(path);
        if (file == null || !file.exists()) {
            System.out.println("文件为空或者不存在");
            return false;
        }

        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtras(bundle);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            System.out.println("打开wps异常：" + e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
