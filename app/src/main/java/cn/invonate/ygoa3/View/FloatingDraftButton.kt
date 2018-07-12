package cn.invonate.ygoa3.View

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import cn.invonate.ygoa3.Util.ScreenUtils
import java.util.*

class FloatingDraftButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FloatingActionButton(context, attrs, defStyleAttr), View.OnTouchListener {
    internal var lastX: Int = 0
    internal var lastY: Int = 0
    internal var originX: Int = 0
    internal var originY: Int = 0
    internal val screenWidth: Int
    internal val screenHeight: Int
    val buttons = ArrayList<FloatingActionButton>()
    val buttonSize: Int
        get() = buttons.size
    //是否可拖拽  一旦展开则不允许拖拽
    val isDraftable: Boolean
        get() {
            for (btn in buttons) {
                if (btn.visibility == View.VISIBLE) {
                    return false
                }
            }
            return true
        }

    init {
        screenWidth = ScreenUtils.getScreenWidth(context)
        screenHeight = ScreenUtils.getScreenHeight(context)
        setOnTouchListener(this)
    }

    //注册归属的FloatingActionButton
    fun registerButton(floatingActionButton: FloatingActionButton) {
        buttons.add(floatingActionButton)
    }

    //当被拖拽后其所属的FloatingActionButton 也要改变位置
    private fun slideButton(l: Int, t: Int, r: Int, b: Int) {
        for (floatingActionButton in buttons) {
            floatingActionButton.layout(l, t, r, b)
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!isDraftable) {
            return false
        }
        val ea = event.action
        when (ea) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.rawX.toInt()// 获取触摸事件触摸位置的原始X坐标
                lastY = event.rawY.toInt()
                originX = lastX
                originY = lastY
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX.toInt() - lastX
                val dy = event.rawY.toInt() - lastY
                var l = v.left + dx
                var b = v.bottom + dy
                var r = v.right + dx
                var t = v.top + dy
                // 下面判断移动是否超出屏幕
                if (l < 0) {
                    l = 0
                    r = l + v.width
                }
                if (t < 0) {
                    t = 0
                    b = t + v.height
                }
                if (r > screenWidth) {
                    r = screenWidth
                    l = r - v.width
                }
                if (b > screenHeight) {
                    b = screenHeight
                    t = b - v.height
                }
                v.layout(l, t, r, b)
                slideButton(l, t, r, b)
                lastX = event.rawX.toInt()
                lastY = event.rawY.toInt()
                v.postInvalidate()
            }
            MotionEvent.ACTION_UP -> {
                val distance = event.rawX.toInt() - originX + event.rawY.toInt() - originY
                Log.e("DIstance", distance.toString() + "")
                if (Math.abs(distance) < 20) {
                    //当变化太小的时候什么都不做 OnClick执行
                } else {
                    return true
                }
            }
        }
        return false

    }
}
