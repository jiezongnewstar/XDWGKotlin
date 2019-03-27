package kotlindemo.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.View.GONE
import android.webkit.RenderProcessGoneDetail
import cn.bingoogolapple.qrcode.core.BarcodeType
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.xibei.xdwgkotlin.R
import kotlindemo.util.Constant.Companion.BASEURL
import kotlindemo.util.ToastUtil
import kotlinx.android.synthetic.main.activity_qrcode.*
import kotlinx.android.synthetic.main.item_toolbar.*

class QrCodeActivity: Activity(), View.OnClickListener, QRCodeView.Delegate {

    var flash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        iv_back.setOnClickListener(this)
        iv_flash.setOnClickListener(this)
        iv_scan.visibility = GONE
        iv_home.visibility = GONE
        iv_refresh.visibility = GONE
        zxingview.setType(BarcodeType.ONLY_QR_CODE,null)
        zxingview.setDelegate(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_back -> finish()
            R.id.iv_flash ->{
                if (flash){
                    flash = false
                    iv_flash.setBackgroundResource(R.mipmap.open)
                    zxingview.closeFlashlight()
                }else{
                    flash = true
                    iv_flash.setBackgroundResource(R.mipmap.close)
                    zxingview.openFlashlight()
                }
            }


        }
    }

    override fun onStart() {
        super.onStart()
        zxingview.startCamera()
        zxingview.startSpotAndShowRect()
    }

    override fun onStop() {
        super.onStop()
        zxingview.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        zxingview.onDestroy()
    }


    private fun vibrate(){
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }


    override fun onScanQRCodeSuccess(result: String?) {
        vibrate()
        if (result!!.length>=18){
            if (result.substring(0,18) ==BASEURL){
                val data = Intent()
                data.putExtra("result",result)
                setResult(1001,data)
                finish()
            }else{
                ToastUtil.ToastUtil.showText("请扫描正确二维码！")
                zxingview.startSpotAndShowRect()
                return
            }
        }

    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        if (isDark&&!flash){
            ToastUtil.ToastUtil.showText("光线太暗，青请到光亮的地方或者打开闪光灯")
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        ToastUtil.ToastUtil.showText("打开摄像头出错")
    }



}