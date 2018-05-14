package cn.invonate.ygoa3.Cycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Entry.CycleMessage;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

public class MyCycleFragment extends Fragment {


    Unbinder unbinder;

    YGApplication app;
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private int type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_cycle, container, false);
        unbinder = ButterKnife.bind(this, view);
        app = (YGApplication) getActivity().getApplication();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getMessage(1);
            }
        });
        refresh.autoRefresh();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getMessage(int page) {
        Subscriber subscriber = new Subscriber<CycleMessage>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                Toast.makeText(getActivity(), "请求超时，请检查网络是否连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(CycleMessage data) {
                Log.i("getMessage", data.toString());

            }
        };
        HttpUtil.getInstance(getActivity(), false).getMessageList(subscriber, page, 20, app.getUser().getUser_id());
    }
}
