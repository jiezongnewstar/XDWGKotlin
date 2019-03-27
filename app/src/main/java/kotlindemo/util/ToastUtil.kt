package kotlindemo.util

import android.app.Activity
import android.content.Context
import android.widget.Toast

class ToastUtil{
     var mToast: Toast? = null
     var mContext:Context ?= null
     var toast : Toast? = null

     fun ToastUtil(){

     }

    fun toUi(context: Activity,content:String){
        context.runOnUiThread(Runnable {
            kotlin.run {

            }
        })
    }

}