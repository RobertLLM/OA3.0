package cn.invonate.ygoa3.boss

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cn.invonate.ygoa3.BaseActivity
import cn.invonate.ygoa3.Entry.BossDepart
import cn.invonate.ygoa3.R
import cn.invonate.ygoa3.httpUtil.HttpUtil4
import kotlinx.android.synthetic.main.activity_connect.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import rx.Subscriber
import java.io.Serializable

class ConnectActivity : BaseActivity() {

    private var list_depart: MutableList<BossDepart.ResultBean>? = null

    private var pk: String? = null

    private var task: Int = 0

    private lateinit var finishBroadcast: FinishBroadcast

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)
        val bundle = intent.extras
        if (bundle != null) {
            task = bundle.getInt("task", 0)
            list_depart = bundle.getSerializable("list") as MutableList<BossDepart.ResultBean>
            pk = bundle.getString("pk")
        }
        if (list_depart != null) {
            rv_tab.visibility = View.VISIBLE
            rv_tab.adapter = TabAdapter(list_depart!!, this)
            rv_tab.setOnItemClickListener { _, _, i, _ ->
                val intent = Intent()
                intent.action = "Connect"
                intent.putExtra("back", i)
                sendBroadcast(intent)
            }
        } else {
            rv_tab.visibility = View.GONE
            list_depart = mutableListOf()
        }
        pic_back.setOnClickListener { finish() }
        layout.setOnClickListener { startActivity<SearchActivity>() }
        // 注册广播
        finishBroadcast = FinishBroadcast(task, this)
        val filter = IntentFilter()
        filter.addAction("Connect")
        registerReceiver(finishBroadcast, filter)
        //
        refresh.setOnRefreshListener {
            getDepart()
        }
        getDepart()
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        unregisterReceiver(finishBroadcast)
        super.onDestroy()
    }

    /**
     * 获取集团通讯录部门
     */
    private fun getDepart() {
        val subscriber = object : Subscriber<BossDepart>() {
            override fun onNext(data: BossDepart?) {
                val adapter = BossDepartAdapter(data!!.result, this@ConnectActivity)
                adapter.onPersonClickListener = object : OnPersonClickListener {
                    override fun onPersonClick(position: Int) {
                        startActivity<EmpActivity>("pk" to data.result[position].depPK)
                    }
                }

                adapter.onDepartClickListener = object : OnDepartClickListener {
                    override fun onDepartClick(position: Int) {
                        startActivity<DepartInfoActivity>("pk" to data.result[position].depPK)
                    }

                }
                depart.adapter = adapter
                depart.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                    if (data.result[position].leafNode == 0) {
                        val list: MutableList<BossDepart.ResultBean> = mutableListOf()
                        for (bean in list_depart!!) list.add(bean)
                        list.add(data.result[position])
                        val intent = Intent(this@ConnectActivity, ConnectActivity::class.java)
                        val bundle = Bundle()
                        bundle.putInt("task", task + 1)
                        bundle.putSerializable("list", list as Serializable)
                        bundle.putString("pk", data.result[position].depPK)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                }
                refresh.finishRefresh()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                refresh.finishRefresh(false)
                Snackbar.make(refresh, "获取数据失败", Snackbar.LENGTH_LONG).show()
            }
        }
        HttpUtil4.getInstance(this, false).getDepart(subscriber, pk)
    }


    /**
     * 部门列表适配
     */
    class BossDepartAdapter(var data: List<BossDepart.ResultBean>, var context: Context) : BaseAdapter() {
        var onPersonClickListener: OnPersonClickListener? = null

        var onDepartClickListener: OnDepartClickListener? = null

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val holder: ViewHolder
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_boss_depart, parent, false)
                holder = ViewHolder(view)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.txt_name.text = data[position].depName
            if (data[position].leafNode == 0) {
                holder.img_depart.visibility = View.GONE
                holder.img_user.visibility = View.GONE
                holder.img_next.visibility = View.VISIBLE
            } else {
                holder.img_depart.visibility = View.VISIBLE
                holder.img_user.visibility = View.VISIBLE
                holder.img_next.visibility = View.GONE
            }

            holder.img_user.setOnClickListener {
                onPersonClickListener?.onPersonClick(position)
            }
            holder.img_depart.setOnClickListener {
                onDepartClickListener?.onDepartClick(position)
            }
            return view
        }

        override fun getItem(position: Int): Any {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return data.size
        }

        class ViewHolder(var view: View) {
            var txt_name: TextView = view.find(R.id.txt_name)
            var img_depart: ImageView = view.find(R.id.img_depart)
            var img_user: ImageView = view.find(R.id.img_user)
            var img_next: ImageView = view.find(R.id.img_next)
        }
    }

    class TabAdapter(var data: MutableList<BossDepart.ResultBean>, var context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val holder: ViewHolder
            val view: View
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false)
                holder = ViewHolder(view)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.tab_name.text = data[position].depName
            return view
        }

        override fun getItem(position: Int): Any {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return data.size
        }

        class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var tab_name: TextView = itemView!!.find(R.id.tab_name)
        }
    }

    /**
     * 关闭Activity的广播接收器
     */
    class FinishBroadcast(var task: Int, var activity: ConnectActivity) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val back = intent!!.getIntExtra("back", -1)
            if (task > back) {
                activity.finish()
            }
        }
    }

    /**
     * 点击用户信息的监听
     */
    interface OnPersonClickListener {
        fun onPersonClick(position: Int)
    }

    /**
     * 点击部门信息的监听
     */
    interface OnDepartClickListener {
        fun onDepartClick(position: Int)
    }
}
