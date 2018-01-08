package cn.invonate.ygoa.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.invonate.ygoa.Chat.ChatActivity;
import cn.invonate.ygoa.R;
import cn.invonate.ygoa.main.work.mail.MailActivity;

/**
 * Created by liyangyang on 2017/10/22.
 */

public class WorkFragment extends Fragment {
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_work, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_notice, R.id.layout_allow, R.id.layout_mession, R.id.layout_mail, R.id.layout_report, R.id.layout_space, R.id.layout_study, R.id.layout_meeting})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.layout_notice:
                intent = new Intent(getActivity(), ChatActivity.class);
                break;
            case R.id.layout_allow:
                break;
            case R.id.layout_mession:
                break;
            case R.id.layout_mail:
                intent = new Intent(getActivity(), MailActivity.class);
                break;
            case R.id.layout_report:
                break;
            case R.id.layout_space:
                break;
            case R.id.layout_study:
                break;
            case R.id.layout_meeting:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
