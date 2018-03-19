package cn.invonate.ygoa3.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.CircleImageView;
import cn.invonate.ygoa3.WebView.WebViewActivity;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.login.FundActivity;
import cn.invonate.ygoa3.login.PropertyActivity;
import cn.invonate.ygoa3.login.SettingActivity;
import cn.invonate.ygoa3.login.WageActivity;


/**
 * Created by liyangyang on 2017/10/22.
 */

public class MineFragment extends Fragment {
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.txt_name)
    TextView txtName;
    Unbinder unbinder;

    private YGApplication app;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        app = (YGApplication) getActivity().getApplication();
        txtName.setText(app.getUser().getUser_name());
        Glide.with(getContext())
                .load(HttpUtil.URL_FILE + app.getUser().getUser_photo())
                .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                .skipMemoryCache(true).into(imgHead);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_head, R.id.layout_group, R.id.layout_money, R.id.layout_day, R.id.layout_gz, R.id.layout_wallet, R.id.layout_setting})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.img_head:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "个人信息");
                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=8");
                break;
            case R.id.layout_group:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "群组");
                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=9");
                break;
            case R.id.layout_money:
                intent = new Intent(getActivity(), PropertyActivity.class);
//                intent.putExtra("name", "资产");
//                intent.putExtra("url", "http://zcgl.yong-gang.cn:8080/eam/assetLedger/assetLedger/queryPersonAsset.action?user_code=" + app.getUser().getUser_code());

                break;
            case R.id.layout_day:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "日程");
                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=10");
                break;
            case R.id.layout_gz:
                intent = new Intent(getActivity(), WageActivity.class);
//                intent.putExtra("name", "工资福利");
//                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
//                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=11");
                break;
            case R.id.layout_wallet:
                intent = new Intent(getActivity(), FundActivity.class);
//                intent.putExtra("name", "电子钱包");
//                intent.putExtra("url", HttpUtil.BASE_URL + "/ygoa/LoginForMobile?user_code="
//                        + app.getUser().getUser_code() + "&sessionId=" + app.getUser().getSessionId() + "&type=12");
                break;
            case R.id.layout_setting:
                intent = new Intent(getActivity(), SettingActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
