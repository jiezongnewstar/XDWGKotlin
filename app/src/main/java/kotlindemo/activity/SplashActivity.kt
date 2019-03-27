package kotlindemo.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.xibei.xdwgkotlin.R
import kotlindemo.util.Constant
import kotlindemo.view.CircleProgressbar
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity: Activity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.WHITE
            window.navigationBarColor = Color.TRANSPARENT
        }

        setContentView(R.layout.activity_splash)
        initView()
    }

    private fun initView() {
        initData()
        initListener()
    }

    private fun initListener() {
        tv_red_skip.setOnClickListener {
            tv_red_skip.stop()
            startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            finish()
        }

        tv_red_skip.setTimeMills(5000)
        tv_red_skip.setProgressColor(Color.BLACK)

        tv_red_skip.setCountdownProgressListener(5,
            object :CircleProgressbar.OnCountdownProgressListener{
                override fun onProgress(what: Int, progress: Int) {
                    if (progress==5){
                        startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                        finish()
                    }
                }
            }
        )

        tv_red_skip.start()

    }

    private fun initData() {
        val uri = Uri.parse(Constant.BASEURL+"/app/img/hy.jpg")
        var imagePipeline = Fresco.getImagePipeline()
        imagePipeline.evictFromMemoryCache(uri)
        imagePipeline.evictFromDiskCache(uri)
        imagePipeline.evictFromCache(uri)
        simpleDraweeView.setImageURI(uri)

    }


}