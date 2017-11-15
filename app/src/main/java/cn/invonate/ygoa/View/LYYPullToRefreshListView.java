package cn.invonate.ygoa.View;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by liyangyang on 2017/10/22.
 */

public class LYYPullToRefreshListView extends PullToRefreshListView {

    public LYYPullToRefreshListView(Context context) {
        super(context);
        init();
    }

    public LYYPullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LYYPullToRefreshListView(Context context, Mode mode) {
        super(context, mode);
        init();
    }

    public LYYPullToRefreshListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        init();
    }

    private void init() {
        this.setMode(Mode.PULL_FROM_START);
        // 上拉加载更多，分页加载
        this.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        this.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        this.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        this.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        this.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        this.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
    }


}
