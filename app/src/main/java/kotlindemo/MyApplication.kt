package kotlindemo

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class MyApplication:Application(){

    var myApp: MyApplication? = null

    override fun onCreate() {
        super.onCreate()
        myApp = this
        Fresco.initialize(this@MyApplication)
    }
}