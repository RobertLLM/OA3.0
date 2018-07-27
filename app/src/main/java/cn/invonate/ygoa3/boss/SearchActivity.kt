package cn.invonate.ygoa3.boss

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import cn.invonate.ygoa3.Entry.EmpInfo
import cn.invonate.ygoa3.R
import cn.invonate.ygoa3.httpUtil.HttpUtil4
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import rx.Subscriber

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        pic_back.setOnClickListener { finish() }

        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                search(input.text.toString())
            }
        })
    }

    /**
     * 按照关键字搜索
     */
    private fun search(keyword: String) {
        val subscriber = object : Subscriber<EmpInfo>() {
            override fun onNext(data: EmpInfo?) {
                if (data!!.code == "0000") {
                    list.adapter = EmpAdapter(data.result, this@SearchActivity)
                    list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        startActivity<EmpInfoActivity>("code" to data.result[position].empNo)
                    }
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }
        }
        HttpUtil4.getInstance(this, false).getEmpInfo(subscriber, keyword)
    }

    /**
     *
     */
    class EmpAdapter(var data: List<EmpInfo.ResultBean>, var context: Context) : BaseAdapter() {

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
}
