package kotlindemo

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class MyApplication:Application(){

    var myApp: MyApplication? = null

    companion object {
        var instance: MyApplication? =null
        get() {
            if (field ==null){

                field = MyApplication()
            }
            return field

            @Synchronized
            fun getInstance(): MyApplication? {
                return this!!.instance
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        myApp = this
        Fresco.initialize(this@MyApplication)
    }
}