package cn.invonate.ygoa3.View

import android.content.Context
import android.util.AttributeSet
import com.handmark.pulltorefresh.library.PullToRefreshBase

import com.handmark.pulltorefresh.library.PullToRefreshListView

/**
 * Created by liyangyang on 2017/10/22.
 */

class LYYPullToRefreshListView : PullToRefreshListView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, mode: PullToRefreshBase.Mode) : super(context, mode) {
        init()
    }

    constructor(context: Context, mode: PullToRefreshBase.Mode, style: PullToRefreshBase.AnimationStyle) : super(context, mode, style) {
        init()
    }

    private fun init() {
        this.mode = PullToRefreshBase.Mode.PULL_FROM_START
        // 上拉加载更多，分页加载
        this.getLoadingLayoutProxy(false, true).setPullLabel("加载更多")
        this.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...")
        this.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载")
        // 下拉刷新
        this.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新")
        this.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...")
        this.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新")
    }


}
