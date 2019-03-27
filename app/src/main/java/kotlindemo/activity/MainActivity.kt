package kotlindemo.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.*
import com.bumptech.glide.Glide
import com.xibei.xdwgkotlin.R
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlindemo.util.Constant.Companion.BASEURL
import kotlindemo.util.ToastUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_toolbar.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : Activity(), View.OnClickListener , EasyPermissions.PermissionCallbacks, SwipeRefreshLayout.OnRefreshListener {

    private val REQUEST_CODE_QRCODE_PERMISSIONS =1
    private lateinit var webSettings: WebSettings
    private var path = "/app/index.asp"
    private var firstTime: Long = 0
    val FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5 = 2
    val FILE_CHOOSER_RESULT_CODE = 3
    var fromWhere = ""
    var mUploadMessageForAndroid5: ValueCallback<Array<Uri>>? = null
    var mUploadMessage: ValueCallback<Uri>? = null


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
        initWebView()
    }

    private fun initWebView() {
        webview.clearCache(true)
        webview.clearHistory()
        webSettings = webview.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true
        webSettings.useWideViewPort = true
        webSettings.domStorageEnabled =true
        webSettings.defaultTextEncodingName = "UTF-8"
        webSettings.allowContentAccess = true
        webSettings.allowFileAccess = true
        webSettings.allowFileAccessFromFileURLs = false
        webSettings.allowUniversalAccessFromFileURLs = false


        webview.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                webview.visibility = VISIBLE
                ll_faild.visibility = GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, BASEURL+path)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                //>6.0
                webview.visibility = GONE
                ll_faild.visibility = VISIBLE
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                //<6.0
                webview.visibility = GONE
                ll_faild.visibility = VISIBLE
            }

        }

        webview.webChromeClient = object :WebChromeClient(){
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                if (title!=null){
                    if (title.contains("404") or
                        title.contains("System Error") or
                            title.contains("无法找到")){
                        webview.visibility = GONE
                        ll_faild.visibility = VISIBLE

                    }else{
                        webview.visibility = VISIBLE
                        ll_faild.visibility = GONE
                    }
                }
            }


            // For Android < 3.0
            fun openFileChooser(valueCallback: ValueCallback<Uri>) {

            }

            // For Android  >= 3.0
            fun openFileChooser(valueCallback: ValueCallback<*>, acceptType: String) {

            }


            // For Android < 5.0
            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
                //                openFileChooserImpl(uploadMsg);
                select(null, uploadMsg)
                //                uploadMsg.onReceiveValue(null);
            }

            // For Android => 5.0
            override fun onShowFileChooser(webView: WebView, uploadMsg: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
                //                onenFileChooseImpleForAndroid(uploadMsg);
                select(uploadMsg, null)
                //                uploadMsg.onReceiveValue(null);
                return true
            }



            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    progressBar1.setVisibility(View.GONE)
                    //                    swiperefresh.setRefreshing(false);
                    if (webview.canGoBack()) {
                        iv_back.visibility = View.VISIBLE
                    } else {
                        iv_back.visibility = View.GONE
                    }
                } else {
                    progressBar1.setVisibility(View.VISIBLE)
                    progressBar1.setProgress(newProgress)
                }
            }

            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                val b = AlertDialog.Builder(this@MainActivity)
                b.setTitle("提醒：")
                b.setMessage(message)
                b.setPositiveButton(
                    android.R.string.ok
                ) { dialog, which -> result.confirm() }
                b.setCancelable(false)
                b.create().show()
                return true
            }

            //设置响应js 的Confirm()函数
            override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
                val b = AlertDialog.Builder(this@MainActivity)
                b.setTitle("确定：")
                b.setMessage(message)
                b.setPositiveButton(
                    android.R.string.ok
                ) { dialog, which -> result.confirm() }
                b.setNegativeButton(
                    android.R.string.cancel
                ) { dialog, which -> result.cancel() }
                b.create().show()
                return true
            }

            //设置响应js 的Prompt()函数
            override fun onJsPrompt(
                view: WebView,
                url: String,
                message: String,
                defaultValue: String,
                result: JsPromptResult
            ): Boolean {
                val b = AlertDialog.Builder(this@MainActivity)
                b.setTitle("请输入：")
                b.setPositiveButton(
                    android.R.string.ok
                ) { dialog, which -> result.confirm() }
                b.setNegativeButton(
                    android.R.string.cancel
                ) { dialog, which -> result.cancel() }
                b.create().show()
                return true
            }

        }

        webview.loadUrl(BASEURL+path)

    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_scan -> webview.reload()
            R.id.iv_home -> {
                path = "/app/index.asp"
                webview.loadUrl(BASEURL+path)
            }
            R.id.iv_refresh ->{
                fromWhere = "qrcode"
                requestCodeQRCodePermissions()
            }
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val secondTime = System.currentTimeMillis()
        if (keyCode == KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack()
            return true
        } else {
            if (secondTime - firstTime > 2000) {
                ToastUtil.show(this@MainActivity,"再按一次退出程序")
                firstTime = secondTime
                return true
            } else {
                System.exit(0)
                finish()
            }
            return true

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            if (mUploadMessage != null) {
                mUploadMessage!!.onReceiveValue(null)
            }

            if (mUploadMessageForAndroid5 != null) {
                mUploadMessageForAndroid5!!.onReceiveValue(null)
            }
        }
        if (requestCode == 1001 && resultCode == 1001) {
            if (data != null)
                webview.loadUrl(data.getStringExtra("result"))
        }

        val resultlist = if (data == null) null else Matisse.obtainResult(data)
        when (requestCode) {
            FILE_CHOOSER_RESULT_CODE  //android 5.0以下 选择图片回调
            -> {

                if (null == mUploadMessage)
                    return
                mUploadMessage!!.onReceiveValue(resultlist!![0])
                mUploadMessage = null
                if (resultCode == Activity.RESULT_CANCELED) {
                    mUploadMessage!!.onReceiveValue(null)

                }
            }

            FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5 -> {

                if (null == mUploadMessageForAndroid5 || resultlist == null)
                    return
                if (resultlist[0] != null) {
                    mUploadMessageForAndroid5!!.onReceiveValue(arrayOf(resultlist[0]))
                    mUploadMessageForAndroid5 = null
                } else {
                    mUploadMessageForAndroid5!!.onReceiveValue(arrayOf())
                    mUploadMessageForAndroid5 = null
                }
                mUploadMessageForAndroid5 = null
                if (resultCode == Activity.RESULT_CANCELED) {
                    mUploadMessageForAndroid5!!.onReceiveValue(null)

                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (fromWhere == "qrcode") {
            fromWhere = ""
            startActivityForResult(Intent(this@MainActivity, QrCodeActivity::class.java), 1001)
        }


    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        requestCodeQRCodePermissions()
    }

    private fun requestCodeQRCodePermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!EasyPermissions.hasPermissions(this, *perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机权限", REQUEST_CODE_QRCODE_PERMISSIONS, *perms)
        } else {
            if (fromWhere == "qrcode") {
                fromWhere = ""
                startActivityForResult(Intent(this@MainActivity, QrCodeActivity::class.java), 1001)
            }

        }
    }

    override fun onRefresh() {
        webview.reload()
    }


    /**
     * android 5.0 以下开启图片选择（原生）
     *
     * 可以自己改图片选择框架。
     */
    private fun openFileChooserImpl(uploadMsg: ValueCallback<Uri>) {
        mUploadMessage = uploadMsg
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_CHOOSER_RESULT_CODE)

    }

    /**
     * android 5.0(含) 以上开启图片选择（原生）
     *
     * 可以自己改图片选择框架。
     */
    private fun onenFileChooseImpleForAndroid(filePathCallback: ValueCallback<Array<Uri>>) {
        mUploadMessageForAndroid5 = filePathCallback
        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
        contentSelectionIntent.type = "image/*"
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
        startActivityForResult(chooserIntent, FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5)
    }


    fun select(filePathCallback: ValueCallback<Array<Uri>>?,uploadMsg: ValueCallback<Uri>?){
        mUploadMessageForAndroid5 = filePathCallback
        mUploadMessage = uploadMsg
        Matisse.from(this@MainActivity)
            .choose(MimeType.ofAll())
            .countable(true)
            .capture(true)
            .captureStrategy(CaptureStrategy(true,"com.xii.xdpi.fireproofed"))
            .maxSelectable(1)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .autoHideToolbarOnSingleTap(true)
            .forResult(FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5)
    }

}

