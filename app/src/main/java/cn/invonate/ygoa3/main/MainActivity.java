package cn.invonate.ygoa3.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;

import java.io.File;
import java.util.List;
import java.util.Set;

import butterknife.BindColor;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Version;
import cn.invonate.ygoa3.Permission.PermissionsActivity;
import cn.invonate.ygoa3.Permission.PermissionsChecker;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.Util.DownLoadRunnable;
import cn.invonate.ygoa3.Util.MyProvide;
import cn.invonate.ygoa3.Util.MyUtils;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.Subscriber;

public class MainActivity extends BaseActivity {
    @BindViews({R.id.pic_message, R.id.pic_work, R.id.pic_pic, R.id.pic_connect, R.id.pic_mine,})
    List<ImageView> list_imgs;

    @BindViews({R.id.text_message, R.id.text_work, R.id.text_pic, R.id.text_connect, R.id.text_mine})
    List<TextView> list_txt;

    //点击的字体颜色
    @BindColor(R.color.colorPrimary)
    public int colorMainClick;

    //未点击的字体颜色
    @BindColor(R.color.fragemnt_text)
    public int colorMainUnClick;

    private Fragment[] fragments;

    private int currentItem = 1;

    private FinishBroadCast finish;

    private RefreshBroadCast refresh;

    YGApplication app;

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    private static final int REQUEST_CODE = 0x999; // 请求码
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        mPermissionsChecker = new PermissionsChecker(this);
        fragments = new Fragment[]{new MessageFragment(), new WorkFragment(), new PicFragment(), new ContactsFragment(), new MineFragment()};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, fragments[0])
                .add(R.id.content, fragments[1])
                .add(R.id.content, fragments[2])
                .add(R.id.content, fragments[3])
                .add(R.id.content, fragments[4])
                .hide(fragments[0]).hide(fragments[2])
                .hide(fragments[3])
                .hide(fragments[4])
                .show(fragments[1])
                .commit();
        list_imgs.get(1).setSelected(true);
        list_txt.get(1).setTextColor(colorMainClick);

        finish = new FinishBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("finish");
        registerReceiver(finish, intentFilter);

        refresh = new RefreshBroadCast();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("refresh");
        registerReceiver(refresh, intentFilter2);
        getVersion();
        Log.i("JPushreceiver", JPushInterface.getRegistrationID(this));
        setAlias();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    /**
     *
     */
    private void startPermissionsActivity() {
        PermissionsActivity.Companion.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
        Log.i("onActivityResult", "requestCode=" + requestCode + "resultCode" + resultCode);
        if (requestCode == 26) {
            checkO();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(finish);
        unregisterReceiver(refresh);
        super.onDestroy();
    }

    /**
     * 切换Fragment的方法
     *
     * @param index 页码
     */
    private void changeFragment(int index) {
        if (index == 0) {
            Toast.makeText(this, "该功能暂未开放", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentItem != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentItem]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.content, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        list_imgs.get(currentItem).setSelected(false);
        list_imgs.get(index).setSelected(true);

        list_txt.get(currentItem).setTextColor(colorMainUnClick);
        list_txt.get(index).setTextColor(colorMainClick);
        currentItem = index;
    }

    @OnClick({R.id.layout_message, R.id.layout_work, R.id.layout_pic, R.id.layout_connect, R.id.layout_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_message:
                changeFragment(0);
                break;
            case R.id.layout_work:
                changeFragment(1);
                break;
            case R.id.layout_pic:
                changeFragment(2);
                break;
            case R.id.layout_connect:
                changeFragment(3);
                break;
            case R.id.layout_mine:
                changeFragment(4);
                break;
        }
    }

    class FinishBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    class RefreshBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (fragments != null && fragments[3] != null)
                ((ContactsFragment) fragments[3]).refreshLocal();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 检查版本更新
     */
    private void getVersion() {
        Subscriber subscriber = new Subscriber<Version>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("version_error", e.toString());
            }

            @Override
            public void onNext(final Version data) {
                Log.i("version", data.toString());
                int version_new = data.getVersion_code();
                int version_local = GetVersion(MainActivity.this);
                if (version_new == -1 || version_local == -1) {
                    return;
                }
                if (version_new > version_local) {
                    AlertDialog dialog = new AlertDialog(MainActivity.this).builder();
                    dialog.setTitle("有新版本，是否更新")
                            .setPositiveButton("确定", new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    String url = data.getVersion_url();
                                    if (url.startsWith("/"))
                                        url = url.substring(1);
                                    new Thread(new DownLoadRunnable(MainActivity.this, HttpUtil.URL_FILE + url, "永钢办公v3", 0, handler)).start();
                                }
                            }).setNegativeButton("取消", null)
                            .show();
                }
            }
        };
        HttpUtil.getInstance(this, false).getVersion(subscriber);
    }

    //handler更新ui
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    checkO();
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(MainActivity.this, "更新出错", Toast.LENGTH_SHORT).show();
                    break;
                case DownloadManager.STATUS_PENDING:
                    break;
            }
            return false;
        }
    });


    // 取得版本号
    public static int GetVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionCode;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 检查安卓8.0
     */
    private void checkO() {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (haveInstallPermission) {//有权限
                install();
            } else { // 没有权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10010);
            }
        } else {
            install();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10010:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    install();
                } else {
                    startInstallPermissionSettingActivity();
                }
                break;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        startActivityForResult(intent, 26);
    }

    public void install() {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + "/" + MyUtils.PACKAGE_NAME + "/" + MyUtils.APP_NAME);
        if (file == null || !file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 在Boradcast中启动活动需要添加Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri data = MyProvide.getUriForFile(this, "cn.invonate.ygoa3.fileprovider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(data, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
        }
        startActivity(intent);
        Log.i("install", "finish");
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias() {
        String code = app.getUser().getUser_code();

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, code));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("JPushInterface", logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("JPushInterface", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("JPushInterface", logs);
            }
        }
    };

    private static final int MSG_SET_ALIAS = 1001;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("JPushInterface", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i("JPushInterface", "Unhandled msg - " + msg.what);
            }
        }
    };

}
