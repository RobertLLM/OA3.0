package cn.invonate.ygoa3.View

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.widget.Adapter
import android.widget.LinearLayout

/**
 * Created by liyangyang on 2017/4/27.
 */

class LinearLayoutForListView : LinearLayout {
    var adapter: Adapter? = null
        set(adapter) {
            field = adapter
            if (this.adapter == null) {
                removeAllViews()
            } else {
                bindView()
            }

        }
    private var mViewHolders: SparseArray<View>? = null
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    private fun bindView() {
        removeAllViews()
        orientation = LinearLayout.VERTICAL
        val count = adapter!!.count
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        mViewHolders = SparseArray(count)
        for (i in 0 until count) {
            val v = adapter!!.getView(i, null, null)
            v.setOnClickListener {
                if (onItemClickListener != null) {
                    onItemClickListener!!.onclick(i)
                }
            }
            mViewHolders!!.put(i, v)
            addView(v, layoutParams)
        }
    }

    interface OnItemClickListener {
        fun onclick(position: Int)
    }

}
