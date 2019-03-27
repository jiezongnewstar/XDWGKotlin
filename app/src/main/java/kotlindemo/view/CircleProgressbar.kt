package kotlindemo.view

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.os.Build
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.widget.TextView
import com.xibei.xdwgkotlin.R

class CircleProgressbar constructor(context:Context,attrs: AttributeSet):TextView(context,attrs) {


    //外部轮廓的颜色
    private var outLineColor = Color.BLACK
    //外部轮廓的宽度
    private var outlinLineWidth = 2
    //内部圆的颜色
    private var inCircleColors = ColorStateList.valueOf(Color.TRANSPARENT)
    //中心圆的颜色
    private  var circleColor: Int ?=null
    //进度条的颜色
    private var progressLineColor = Color.BLUE
    //进度条宽度
    private var progressLineWidth = 8
    //画笔
    private val mpaint = Paint()
    //进度条的矩形区域
    private var mArcRect = RectF()
    //进度
    private var progress = 100
    //进度条类型
    private var mProgressType = ProgressType.COUNT_BACK
    //进度条倒计时时间
    private var timeMills :Long = 3000
    //View的显示区域
    private val bounds = Rect()
    //进度条通知
    private lateinit var mCountdownProgressListener: OnCountdownProgressListener
    private var listenrWhat = 0

    private val progressChangeTask :Runnable = object :Runnable {
        override fun run() {
                        removeCallbacks(this)
            when(mProgressType){
                ProgressType.COUNT -> progress+=1
                else -> progress-=1
            }
            if (progress in (0..100)){
                invalidate()
                    mCountdownProgressListener.onProgress(listenrWhat,progress)
                    invalidate()
                    postDelayed(this, timeMills / 100)
            }else{
                 setProgerss(progress)
            }

        }
    }


    fun CircleProgressbar(context: Context, attrs: AttributeSet, defStyleAttr: Int){
        initialize(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun CircleProgressbar(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int){
        initialize(context, attrs)
    }


    @SuppressLint("Recycle")
    fun initialize (context: Context, attrs: AttributeSet){
        mpaint.isAntiAlias = true
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressbar)
        if (typedArray.hasValue(R.styleable.CircleProgressbar_in_circle_color)){
            inCircleColors = typedArray.getColorStateList(R.styleable.CircleProgressbar_in_circle_color)
        }else{
            inCircleColors = ColorStateList.valueOf(Color.TRANSPARENT)
            circleColor = inCircleColors.getColorForState(drawableState,Color.TRANSPARENT)
        }

    }


    //设置外部轮廓的颜色
    fun setOutLineColor(@ColorInt outLineColor: Int){
        this.outLineColor = outLineColor
        invalidate()
    }

    //设置外部轮廓宽度
    fun setOutLineWidth(@ColorInt outLineWidth: Int){
        invalidate()
    }

    //设置中心圆的颜色
    fun setInCircleColor(@ColorInt inCircleColor: Int) {
        this.inCircleColors = ColorStateList.valueOf(inCircleColor)
        invalidate()
    }




    private fun validateCircleColor(){
        val circleColorTemp = inCircleColors.getColorForState(drawableState,Color.TRANSPARENT)
        if (circleColor !=circleColorTemp){
            circleColor = circleColorTemp
            invalidate()
        }
    }

    //设置圆形进度条颜色
    fun setProgressColor(@ColorInt progressLineColor :Int){
        this.progressLineColor = progressLineColor
        invalidate()
    }

    //设置圆形进度条宽度
    fun setProgressLineWidth(progressLineWidth :Int){
        this.progressLineWidth = progressLineWidth
        invalidate()
    }

    //设置进度条值
    fun setProgerss(progress: Int){
        if (progress >100) {
            this.progress = 100
        } else if (progress < 0) {
            this.progress = 0
        }
    }

    //获取进度值
    fun getProgress():Int{

        return progress
    }

    //设置倒计时
    fun setTimeMills(timeMills :Long){
        this.timeMills = timeMills
    }

    //获取倒计时时间
    fun getTimeMills() :Long{

        return this.timeMills
    }


    //设置进度条类型  0-100 还是 100-0
    fun setProgressType(progressType : ProgressType){
        this.mProgressType = progressType
        resetProgress()
        invalidate()
    }


    private fun resetProgress() = when(mProgressType){
        ProgressType.COUNT -> progress =0
        else -> progress = 100
    }

    //获取进度条类型
    fun getProgressType() : ProgressType {
        return mProgressType
    }

    //进度条监听
    fun setCountdownProgressListener(
        what: Int,
        mCountdownProgressListener: OnCountdownProgressListener){
        this.listenrWhat = what
        this.mCountdownProgressListener = mCountdownProgressListener

    }

    fun start(){
        stop()
        post(progressChangeTask)
    }

    //开始旋转倒计时
    fun reStart(){
        resetProgress()
        start()
    }

    fun stop(){

        removeCallbacks(progressChangeTask)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //获取View边界
        getDrawingRect(bounds)
        val size = if (bounds.height() >bounds.width()) bounds.width() else bounds.height()
        val outerRadius = size/2
        //画内部背景
        val cirlceColor = inCircleColors.getColorForState(drawableState,0)
        mpaint.style = Paint.Style.FILL
        mpaint.color = cirlceColor
        canvas!!.drawCircle(bounds.centerX().toFloat(), bounds.centerY().toFloat(),
            (outerRadius - outlinLineWidth).toFloat(),mpaint)
        //画边框圆
        mpaint.style = Paint.Style.STROKE
        mpaint.strokeWidth = outlinLineWidth.toFloat()
        mpaint.color = outLineColor
        canvas.drawCircle(
            bounds.centerX().toFloat(),
            bounds.centerY().toFloat(),
            (outerRadius -outlinLineWidth/2).toFloat(),
            mpaint)

        //画字
        paint.color = currentTextColor
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.CENTER
        val textY = bounds.centerY() - (paint.descent() + paint.ascent())/2
        canvas.drawText(text.toString(),
            bounds.centerX().toFloat(),
            textY,
            paint)
        //画进度条
        mpaint.color = progressLineColor
        mpaint.style = Paint.Style.STROKE
        mpaint.strokeWidth = progressLineWidth.toFloat()
        mpaint.isAntiAlias = true
        val deleteWidth = progressLineWidth + outlinLineWidth
        mArcRect.set(
            (bounds.left+deleteWidth/2).toFloat(),
            (bounds.top+deleteWidth/2).toFloat(),
            (bounds.right-deleteWidth/2).toFloat(),
            (bounds.bottom - deleteWidth/2).toFloat())
        canvas.drawArc(mArcRect,
            (-90).toFloat(),
            (-360*progress/100).toFloat(),
            false,
            mpaint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val lineWidth = 4 *(outlinLineWidth+progressLineWidth)
        val width = measuredWidth
        val height = measuredHeight
        val size = (if (width>height)width else height) + lineWidth
        setMeasuredDimension(size,size)
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        validateCircleColor()
    }



    enum class ProgressType{
        COUNT,COUNT_BACK
    }

    interface OnCountdownProgressListener{
        fun onProgress(what: Int,progress: Int)
    }

}