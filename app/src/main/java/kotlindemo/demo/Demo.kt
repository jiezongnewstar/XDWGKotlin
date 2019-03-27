package kotlindemo.demo

//手写handler

class KtHandler: Thread(){

    var ktHandlerThead: KtHandlerThead = KtHandlerThead();

    override fun run() {
        super.run()

    }



    class KtHandlerThead : Thread(){
        var ktLooper: KtLooper? = null
        override fun run() {
            super.run()
        }
    }


    class KtLooper{

        var quit: Boolean =false

        var runnable: Runnable? = null

        fun setKtLooper(task :Runnable){
            while (quit){
                if (runnable!=null){
                    runnable!!.run()
                }
            }
        }



    }

}
