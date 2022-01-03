package com.zhujm.getconfig.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.fastjson.JSONObject
import com.zhujm.getconfig.GecResultCallback
import com.zhujm.getconfig.GecTaskBuilder
import com.zhujm.getconfig.GetConfig
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var getC: GetConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getC = GetConfig(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        getC.destroy()
    }

    /**
     * 示例1 可以获取github上公开的网页上的重要内容
     */
    fun clickLoad(view: View) {
        // 配置task
        var requestTask = GecTaskBuilder().apply {
            // 设置目标网页的url
            targetUrl = "https://github.com/zhujiaming/pdd_water/blob/master/pdd_water.sh"
            // 设置解析脚本
            script = ScriptSource._GITHUB.script
            // 设置超时时间（默认5s）
            timeout = 15000
            // 注册结果监听
            resultCallback = object :
                GecResultCallback {
                override fun onResult(result: String?) {
                    // 由于github网页获取到的文本内容有特殊符号，需要对数据进行处理
                    tvResult.text = result?.replace("\\t", "")?.replace("\\n", "\n")
                    Log.i("oh", "result ===>$result")
                }

                override fun onError(error: String?) {
                    Log.i("oh", "onError ===>$error")
                    tvResult.text = error
                }
            }
        }
        //处理task
        getC.process(requestTask)
    }

    /**
     * 示例2，可以基于分享的网页读取配置文件
     */
    fun clickLoad2(view: View) {
        // 配置task
        var requestTask = GecTaskBuilder().apply {
            // 设置目标网页的url
            targetUrl = "https://note.youdao.com/s/1O8E8D8q"
            // 设置解析脚本
            script = ScriptSource._NOTE.script
            // 超时时间（默认5s）
            timeout = 15000
            // 注册结果监听
            resultCallback = object :
                GecResultCallback {
                override fun onResult(result: String?) {
                    // 对json原始串进行处理，转义特殊字符，这里可以使用fastjson
                    tvResult2.text = JSONObject.parse(result) as String
                    Log.i("oh", "result2 ===>$result")
                }

                override fun onError(error: String?) {
                    Log.i("oh", "onError ===>$error")
                    tvResult.text = error
                }
            }
        }
        //处理task
        getC.process(requestTask)
    }

}
