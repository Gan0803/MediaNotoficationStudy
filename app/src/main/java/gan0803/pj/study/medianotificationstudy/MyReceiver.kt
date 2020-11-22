package gan0803.pj.study.medianotificationstudy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyReceiver : BroadcastReceiver() {
    companion object {
        val TAG: String = this::class.java.simpleName
        const val ACTION_STOP = "gan0803.pj.study.medianotificationstudy.action.ACTION_STOP"
        const val ACTION_RUN = "gan0803.pj.study.medianotificationstudy.action.ACTION_RUN"
    }

    private var callback: IMyCallback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Log.d(TAG, "onReceive, action: {$action}")

        when (action) {
            ACTION_RUN -> {
                callback?.onReceiveRun()
            }
            ACTION_STOP -> {
                callback?.onReceiveStop()
            }
        }
    }

    fun registerCallback(callback: IMyCallback) {
        this.callback = callback
    }

    interface IMyCallback {
        fun onReceiveRun()
        fun onReceiveStop()
    }
}