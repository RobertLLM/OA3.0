package cn.invonate.ygoa3.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.Adapter.FundAdapter;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Amount;
import cn.invonate.ygoa3.Entry.Fund;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.View.LYYPullToRefreshListView;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import rx.Subscriber;

public class FundActivity extends BaseActivity {

    @BindView(R.id.list_fund)
    LYYPullToRefreshListView listFund;

    YGApplication app;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.start_date)
    TextView startDate;
    @BindView(R.id.end_date)
    TextView endDate;

    private String start_date;
    private String end_date;

    private AlertDialog dialog1;
    private AlertDialog dialog2;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Date end = new Date();
    Date start;

    FundAdapter adapter;

    private List<Fund.FundBean> list_fun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund);
        ButterKnife.bind(this);
        listFund.setMode(PullToRefreshBase.Mode.BOTH);
        app = (YGApplication) getApplication();
        end_date = sdf.format(end);
        //计算前30天
        Calendar c = Calendar.getInstance();
        c.setTime(end);
        c.add(Calendar.DATE, -30);
        start = c.getTime();
        start_date = sdf.format(start);
        Log.i("start_date", start_date);
        Log.i("end_date", end_date);
        initDialog();
        getAmount(1);
        listFund.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getAmount(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getAmount(adapter.getCount() / 10 + 1);
            }
        });


    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }

    private void initDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.custom_date_picker, null);
        final DatePicker dpStartDate1 = (DatePicker) customView.findViewById(R.id.dpDate);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(FundActivity.this);
        builder1.setView(customView);
        builder1.setTitle("选择开始日期");
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                start_date = sdf.format(new Date(dpStartDate1.getYear() - 1900, dpStartDate1.getMonth(), dpStartDate1.getDayOfMonth()));
                startDate.setText(start_date);
                getAmount(1);
            }
        });
        dialog1 = builder1.create();

        View customView2 = inflater.inflate(R.layout.custom_date_picker, null);
        final DatePicker dpStartDate2 = (DatePicker) customView2.findViewById(R.id.dpDate);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(FundActivity.this);
        builder2.setView(customView2);
        builder2.setTitle("选择结束日期");
        builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                end_date = sdf.format(new Date(dpStartDate2.getYear() - 1900, dpStartDate2.getMonth(), dpStartDate2.getDayOfMonth()));
                endDate.setText(end_date);
                getAmount(1);
            }
        });
        dialog2 = builder2.create();
    }

    /**
     *
     */
    public void getAmount(final int page) {
        Subscriber subscriber = new Subscriber<Amount>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listFund.onRefreshComplete();
            }

            @Override
            public void onNext(Amount data) {
                Log.i("getAmount", data.toString() + "元");
                amount.setText(data.getData());
                startDate.setText(start_date);
                endDate.setText(end_date);
                startDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.show();
                    }
                });
                endDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.show();
                    }
                });
                getFund(page);
            }
        };
        HttpUtil.getInstance(this, false).getAmount(subscriber, app.getUser().getSessionId());
    }

    /**
     * 获取电子钱包
     *
     * @param page
     */
    private void getFund(final int page) {
        Subscriber subscriber = new Subscriber<Fund>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listFund.onRefreshComplete();
            }

            @Override
            public void onNext(Fund data) {
                Log.i("getFund", data.toString());
                if (data.getSuccess() == 0) {
                    if (page == 1) {
                        list_fun = data.getData();
                        adapter = new FundAdapter(list_fun, FundActivity.this);
                        listFund.setAdapter(adapter);
                    } else {
                        list_fun.addAll(data.getData());
                        adapter.notifyDataSetChanged();
                    }
                    listFund.onRefreshComplete();
                    if (adapter.getCount() % 10 != 0) {
                        listFund.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    } else {
                        listFund.setMode(PullToRefreshBase.Mode.BOTH);
                    }
                }
            }
        };
        Log.i("start_date", start_date);
        Log.i("end_date", end_date);
        HttpUtil.getInstance(this, false).getFund(subscriber, app.getUser().getSessionId(), start_date + " 00:00:00", end_date + " 00:00:00", 10, page);
    }
}
