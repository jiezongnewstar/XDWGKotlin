package kotlindemo.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebSettings
import com.xibei.xdwgkotlin.R
import kotlindemo.util.Constant.Companion.BASEURL
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_toolbar.*

class MainActivity : Activity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v!!.id){
           R.id.iv_scan -> webview.reload()
            R.id.iv_home -> {
                path = "/app/index.asp"
                webview.loadUrl(BASEURL+path)
            }
            R.id.iv_refresh ->{
                fromWhere = "qrcode"

            }
        }
    }


    private val RESQUEST_CODE_QRCODE_PREMISSIONS =1
    private lateinit var webSettings: WebSettings
    private var path = "/app/index.asp"
    private val firstTime = 0
    var lateinit mUploadMessageForAndroid5: ValueCallback<Uri[]>
    var lateinit mUploadMessage: ValueCallback<Uri[]>
    val FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5 = 2
    val FILE_CHOOSER_RESULT_CODE = 3
    var fromWhere = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()
    }

    private fun initListener() {
        iv_scan.setOnClickListener(this@MainActivity)
        iv_home.setOnClickListener(this@MainActivity)
        iv_back.setOnClickListener(this@MainActivity)
        iv_refresh.setOnClickListener(this@MainActivity)
        ll_faild.setOnClickListener(this@MainActivity)
    }


}