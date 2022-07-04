package com.example.lib_audio.mediaplayer.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.example.lib_audio.mediaplayer.app.AudioHelper
import com.example.lib_audio.mediaplayer.core.AudioController
import com.example.lib_audio.mediaplayer.event.AudioLoadEvent
import com.example.lib_audio.mediaplayer.event.AudioPauseEvent
import com.example.lib_audio.mediaplayer.event.AudioReleaseEvent
import com.example.lib_audio.mediaplayer.event.AudioStartEvent
import com.example.lib_audio.mediaplayer.view.NotificationHelper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

//通知栏组件
class MusicService : Service(), NotificationHelper.NotificationHelperListener{


    private var mReceiver: NotificationReceiver? = null


    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
        registerBroadcastReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        unRegisterBroadcastReceiver()
        stopForeground(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent != null && ACTION_START == intent.action){
            //初始化前台Notification
            NotificationHelper.init(this);
        }
        return super.onStartCommand(intent, flags, startId)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioLoadEvent(event: AudioLoadEvent) {
        //更新notifacation为load状态
        NotificationHelper.showLoadStatus(event.audioBean)
        Log.e("ttt", "载入")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioPauseEvent(event: AudioPauseEvent?) {
        //更新notifacation为暂停状态
        NotificationHelper.showPauseStatus()
        Log.e("ttt", "暂停")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioStartEvent(event: AudioStartEvent?) {
        //更新notifacation为播放状态
        NotificationHelper.showPlayStatus()
        Log.e("ttt", "开始")
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioReleaseEvent(event: AudioReleaseEvent?) {
        //移除notifacation
        NotificationHelper.notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)
    }



    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onNotificationInit() {
        startForeground(NotificationHelper.NOTIFICATION_ID, NotificationHelper.notification);
    }

    private fun registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = NotificationReceiver()
            val filter = IntentFilter()
            filter.addAction(NotificationReceiver.ACTION_STATUS_BAR)
            registerReceiver(mReceiver, filter)
        }
    }

    private fun unRegisterBroadcastReceiver() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver)
        }
    }

    companion object{
        private const val ACTION_START = "ACTION_START"

        fun startMusicService() {
            val intent = Intent(AudioHelper.context, MusicService::class.java)
            intent.action = ACTION_START
            if (ServiceUtil.isServiceRunning(AudioHelper.context, MusicService.javaClass.`package`.name)){
                Log.e("tasg", "ddddd")
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AudioHelper.context.startForegroundService(intent);
            } else {
                AudioHelper.context.startService(intent)
            }

        }
    }

    class NotificationReceiver : BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent == null){
                return
            }
            when(intent.getStringExtra(EXTRA)){
                EXTRA_PLAY -> {
                    Log.e("service", "PLAY广播接受成功,开始播放")
                    AudioController.playOrPause();
                }
                EXTRA_PRE -> {
                    Log.e("service", "PRE广播接受成功,上一首")
                    AudioController.previous();
                }
                EXTRA_NEXT -> {
                    Log.e("service", "NEXT广播接受成功,下一首")
                    AudioController.next()
                }
                EXTRA_FAV -> {

                }
                EXTRA_CANCEL -> {
                    //清除通知
                    NotificationHelper.notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)
                    val closeIntent = Intent(AudioHelper.context, MusicService::class.java)
                    closeIntent.action = ACTION_START
                    AudioHelper.context.stopService(closeIntent)
                }
            }
        }


        companion object{
            val ACTION_STATUS_BAR = AudioHelper.context.packageName.toString() + ".NOTIFICATION_ACTIONS"
            const val EXTRA = "extra"
            const val EXTRA_PLAY = "play_pause"
            const val EXTRA_NEXT = "play_next"
            const val EXTRA_PRE = "play_previous"
            const val EXTRA_FAV = "play_favourite"
            const val EXTRA_CANCEL = "cancel_notification"
        }
    }
}