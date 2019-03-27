package kotlindemo.util

import android.content.Context
import android.widget.Toast

class ToastUtil{

    companion object {
        private var toast: Toast?=null

        fun show(context: Context, resourceID:Int){
            show(context, resourceID, Toast.LENGTH_SHORT)
        }

        fun show(context:Context,text:String){
            show(context, text, Toast.LENGTH_SHORT)
        }

        fun show(context:Context,resourceID:Int,duration:Int){
            val text = context.resources.getString(resourceID)
            show(context, text, duration)
        }

        fun show(context:Context,text:String,duration: Int){
            toast = if(toast == null){
                Toast.makeText(context, text, duration)
            }else{
                toast?.cancel()
                Toast.makeText(context, text, duration)
            }
            toast?.show()
        }
    }



    }




