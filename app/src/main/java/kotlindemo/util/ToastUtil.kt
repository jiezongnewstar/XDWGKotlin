package kotlindemo.util

import android.app.Activity
import android.content.Context
import android.widget.Toast
import kotlindemo.MyApplication

class ToastUtil{

    object ToastUtil{
        private var mToast: Toast? = null

        var mContext = MyApplication.instance

        private val toast: Toast? = null

        fun showText(text: CharSequence) {
            if (mContext != null) {
                if (mToast == null) {
                    mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT)
                } else {
                    mToast!!.setText(text)
                    mToast!!.duration = Toast.LENGTH_SHORT
                }
                try {
                    mToast!!.show()
                } catch (var2: Throwable) {
                    var2.printStackTrace()
                }

            }
        }
    }























//    companion object {
//
//        private lateinit var mToast:Toast
//        private val mContext = MyApplication.javaClass.newInstance()
//
//        fun showText(text: CharSequence){
//            mToast = Toast.makeText(mContext,text,Toast.LENGTH_SHORT)
//        }
//    }
}