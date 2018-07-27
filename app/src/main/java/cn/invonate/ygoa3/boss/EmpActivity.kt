package cn.invonate.ygoa3.boss

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import cn.invonate.ygoa3.Entry.Employee
import cn.invonate.ygoa3.R
import cn.invonate.ygoa3.httpUtil.HttpUtil4
import kotlinx.android.synthetic.main.activity_emp.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import rx.Subscriber
import java.util.*

class EmpActivity : AppCompatActivity() {

    private lateinit var input: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emp)
        val pk = intent.getStringExtra("pk")
        getEmpList(pk)
        pic_back.setOnClickListener { finish() }
        refresh.setOnRefreshListener { getEmpList(pk) }
        val view = LayoutInflater.from(this).inflate(R.layout.item_emp_head, null)
        input = view.find(R.id.input)
        list_emp.addHeaderView(view)
    }

    /**
     * 获取部门成员列表
     */
    private fun getEmpList(pk: String) {
        val subscriber = object : Subscriber<Employee>() {
            override fun onNext(data: Employee?) {
                if (data!!.code == "0000") {
                    val adapter = EmpAdapter(data.result, this@EmpActivity)
                    list_emp.adapter = adapter
                    list_emp.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
                        startActivity<EmpInfoActivity>("code" to data.result[i - 1].empNO)
                    }
                    input.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            filterData(input.text.toString(), data.result, adapter)
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                        }

                    })
                } else {
                    Snackbar.make(refresh, data.message, Snackbar.LENGTH_LONG).show()
                }
                refresh.finishRefresh()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                refresh.finishRefresh(false)
            }
        }


        HttpUtil4.getInstance(this, false).getEmpList(subscriber, pk)
    }

    class EmpAdapter(var data: List<Employee.ResultBean>, var context: Context) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val holder: ViewHolder
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_emp, parent, false)
                holder = ViewHolder(view)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.name.text = data[position].empName
            holder.job.text = data[position].empJob
            holder.job.visibility = if (data[position].empJob == "") {
                View.GONE
            } else {
                View.VISIBLE
            }
            return view
        }

        fun updateListView(data: List<Employee.ResultBean>) {
            this.data = data
            notifyDataSetChanged()
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

        inner class ViewHolder(var view: View) {
            var name: TextView = view.find(R.id.name)
            var job: TextView = view.find(R.id.job)
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private fun filterData(filterStr: String, data: MutableList<Employee.ResultBean>, adapter: EmpAdapter) {
        var filterDateList: MutableList<Employee.ResultBean> = ArrayList()
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = data
        } else {
            filterDateList.clear()
            for (emp in data) {
                val name = emp.empName
                val job = emp.empJob
                val code = emp.empNO
                if (name.contains(filterStr) || job.contains(filterStr) || code.contains(filterStr)) {
                    filterDateList.add(emp)
                }
            }
        }
        adapter.updateListView(filterDateList)
    }
}
