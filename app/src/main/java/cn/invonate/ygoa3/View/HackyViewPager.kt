package cn.invonate.ygoa3.View

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent

class HackyViewPager : ViewPager {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            //不理会
            Log.e(TAG, "hacky viewpager error1")
            false
        } catch (e: ArrayIndexOutOfBoundsException) {
            //不理会
            Log.e(TAG, "hacky viewpager error2")
            false
        }

    }

    companion object {
        private val TAG = "HackyViewPager"
    }

}
