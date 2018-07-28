package cn.invonate.ygoa3.boss

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import cn.invonate.ygoa3.Entry.EmpInfo
import cn.invonate.ygoa3.R
import cn.invonate.ygoa3.httpUtil.HttpUtil4
import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog
import kotlinx.android.synthetic.main.activity_emp_info.*
import rx.Subscriber

class EmpInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emp_info)
        pic_back.setOnClickListener { finish() }
        val code = intent.getStringExtra("code")
        getEmpInfo(code)
    }

    /**
     * 获取人员信息
     */
    private fun getEmpInfo(code: String) {
        val subscriber = object : Subscriber<EmpInfo>() {
            override fun onNext(data: EmpInfo?) {
                if (data!!.code == "0000") {
                    if (!data.result.isEmpty()) {
                        layout.visibility = View.VISIBLE
                        name.text = data.result[0].empName
                        empNo.text = data.result[0].empNo
                        phone.text = data.result[0].phoneNum
                        job.text = data.result[0].empJob
                        wage.text = data.result[0].wageLevel
                        shares.text = data.result[0].stockLevel
                        age.text = data.result[0].workingAge
                        edu.text = data.result[0].education
                        lesson.text = data.result[0].specialty
                        phone.setOnClickListener {
                            call(data.result[0].phoneNum)
                        }

                    } else {
                        Snackbar.make(layout, "该员工无信息", Snackbar.LENGTH_LONG).show()
                    }

                } else {
                    Snackbar.make(layout, data.message, Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                Snackbar.make(layout, "获取失败", Snackbar.LENGTH_LONG).show()
            }

        }
        HttpUtil4.getInstance(this, false).getEmpInfo(subscriber, code)
    }

    // 调用系统电话功能
    private fun call(number: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
        } else {
            val alert = AlertDialog(this).builder()
            alert.setPositiveButton("呼叫") {
                //用intent启动拨打电话
                val intent = Intent("android.intent.action.CALL", Uri.parse("tel:$number"))
                startActivity(intent)
            }.setNegativeButton("取消") { }.setMsg(number).show()
        }


    }


}
