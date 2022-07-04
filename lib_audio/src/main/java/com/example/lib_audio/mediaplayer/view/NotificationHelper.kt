package com.example.lib_audio.mediaplayer.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.lib_audio.R
import com.example.lib_audio.mediaplayer.app.AudioHelper
import com.example.lib_audio.mediaplayer.core.AudioController

import com.example.lib_audio.mediaplayer.model.AudioBean
import com.example.lib_image_loader.app.ImageLoaderManager
import com.example.lib_audio.mediaplayer.service.MusicService
import com.example.lib_audio.mediaplayer.utils.MusicUtil


/**
 * @author:SunShibo
 * @date:2022-06-26 20:08
 * @feature: 通知实现类
 */
object NotificationHelper {

    private var isFavourite: Boolean = false
    private const val CHANNEL_ID = "channel_id_audio"
    private const val CHANNEL_NAME = "channel_name_audio"
    internal const val NOTIFICATION_ID = 0x111
    private const val REQUEST_CODE_PLAY = 1
    private const val REQUEST_CODE_PRE = 2
    private const val REQUEST_CODE_NEXT = 3
    private const val REQUEST_CODE_FAV = 4
    private const val REQUEST_CODE_CLOSE = 5


    /**
     * UI
     */
    internal lateinit var notification: Notification
    private lateinit var bigRemoteViews: RemoteViews
    private lateinit var smallRemoteViews: RemoteViews
    private lateinit var listener: NotificationHelperListener

    internal val notificationManager by lazy {
        AudioHelper.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val packageName by lazy{
        AudioHelper.context.packageName
    }
    private var audioBean = AudioController.getNowPlaying()


    fun init(listener: NotificationHelperListener){
        this.listener = listener
        initNotification()
        this.listener.onNotificationInit()
    }

    private fun initNotification() {
        initRemoteViews()

//        val intent = Intent(AudioHelper.context, MusicPlayerActivity::class.java)
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(
//            AudioHelper.context, 0, intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
        //8.0适配
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.apply {
                enableLights(false)
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
//            .setContentIntent(pendingIntent)
        val builder = NotificationCompat.Builder(AudioHelper.context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_notification)
            .setCustomBigContentView(bigRemoteViews) //大布局
            .setContent(smallRemoteViews) //小布局
            .setAutoCancel(false);

        notification = builder.build();
        if(!TextUtils.isEmpty(audioBean.id)){
            notificationManager.notify(NOTIFICATION_ID, notification);
            showLoadStatus(audioBean);
        }
    }

    private fun initRemoteViews() {
        val bigLayoutId = R.layout.notification_big_layout
        bigRemoteViews = RemoteViews(packageName, bigLayoutId)
        bigRemoteViews.setTextViewText(R.id.title_view, audioBean.name)
        bigRemoteViews.setTextViewText(R.id.tip_view, audioBean.albumInfo)

        val smallLayoutId = R.layout.notification_small_layout
        smallRemoteViews = RemoteViews(packageName, smallLayoutId)
        smallRemoteViews.setTextViewText(R.id.title_view, audioBean.name)
        smallRemoteViews.setTextViewText(R.id.tip_view, audioBean.albumInfo)

        //播放暂停广播
        val playIntent = Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR)
        playIntent.putExtra(MusicService.NotificationReceiver.EXTRA, MusicService.NotificationReceiver.EXTRA_PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(AudioHelper.context, REQUEST_CODE_PLAY, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        bigRemoteViews.apply{
            setOnClickPendingIntent(R.id.play_view, playPendingIntent)
            setImageViewResource(R.id.play_view, R.mipmap.note_btn_play_white)
        }
        smallRemoteViews.apply{
            setOnClickPendingIntent(R.id.play_view, playPendingIntent)
            setImageViewResource(R.id.play_view, R.mipmap.note_btn_play_white)
        }

        //上一首
        val previousIntent = Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR)
        previousIntent.putExtra(MusicService.NotificationReceiver.EXTRA, MusicService.NotificationReceiver.EXTRA_PRE)
        val previousPendingIntent = PendingIntent.getBroadcast(AudioHelper.context, REQUEST_CODE_PRE, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        bigRemoteViews.apply{
            setOnClickPendingIntent(R.id.previous_view, previousPendingIntent)
            setImageViewResource(R.id.previous_view, R.mipmap.note_btn_pre_white)
        }

        //下一首
        val nextIntent = Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR)
        nextIntent.putExtra(MusicService.NotificationReceiver.EXTRA, MusicService.NotificationReceiver.EXTRA_NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(AudioHelper.context, REQUEST_CODE_NEXT, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        bigRemoteViews.apply{
            setOnClickPendingIntent(R.id.next_view, nextPendingIntent)
            setImageViewResource(R.id.next_view, R.mipmap.note_btn_next_white)
        }
        smallRemoteViews.apply{
            setOnClickPendingIntent(R.id.next_view, nextPendingIntent)
            setImageViewResource(R.id.next_view, R.mipmap.note_btn_next_white)
        }

        //收藏
        val favouriteIntent = Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR)
        playIntent.putExtra(MusicService.NotificationReceiver.EXTRA, MusicService.NotificationReceiver.EXTRA_FAV)
        val favouritePendingIntent = PendingIntent.getBroadcast(AudioHelper.context, REQUEST_CODE_FAV, favouriteIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        bigRemoteViews.apply{
            setOnClickPendingIntent(R.id.favourite_view, favouritePendingIntent)
        }

        //关闭
        val closeIntent = Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR)
        closeIntent.putExtra(MusicService.NotificationReceiver.EXTRA, MusicService.NotificationReceiver.EXTRA_CANCEL)
        val closePendingIntent = PendingIntent.getBroadcast(AudioHelper.context, REQUEST_CODE_CLOSE, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        bigRemoteViews.setOnClickPendingIntent(R.id.close_view, closePendingIntent)
        smallRemoteViews.setOnClickPendingIntent(R.id.close_view, closePendingIntent)
    }

    /**
     * 加载状态
     */
    fun showLoadStatus(audioBean: AudioBean){
        this.audioBean = audioBean
        bigRemoteViews.apply {
            setImageViewResource(R.id.play_view, R.mipmap.note_btn_play_white)
            setTextViewText(R.id.title_view, audioBean.name)
            setTextViewText(R.id.tip_view, audioBean.albumInfo)
        }
        ImageLoaderManager.getInstance()
            .displayImageForNotification(AudioHelper.context, bigRemoteViews, R.id.image_view,
                notification, NOTIFICATION_ID, MusicUtil.getAlbumPicture(AudioHelper.context, audioBean.url, 0));
        //TODO("更新收藏状态")
        smallRemoteViews.apply {
            setImageViewResource(R.id.play_view, R.mipmap.note_btn_play_white)
            setTextViewText(R.id.title_view, audioBean.name)
            setTextViewText(R.id.tip_view, audioBean.albumInfo)
        }
        ImageLoaderManager.getInstance()
            .displayImageForNotification(AudioHelper.context, smallRemoteViews, R.id.image_view,
                notification, NOTIFICATION_ID, MusicUtil.getAlbumPicture(AudioHelper.context, audioBean.url, 0));

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * 播放状态
     */
    fun showPlayStatus(){
        bigRemoteViews.setImageViewResource(R.id.play_view, R.mipmap.note_btn_pause_white)
        smallRemoteViews.setImageViewResource(R.id.play_view, R.mipmap.note_btn_pause_white)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * 暂停状态
     */
    fun showPauseStatus(){
        bigRemoteViews.setImageViewResource(R.id.play_view, R.mipmap.note_btn_play_white)
        smallRemoteViews.setImageViewResource(R.id.play_view, R.mipmap.note_btn_play_white)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun showFavouriteStatus(){
        bigRemoteViews.setImageViewResource(
            R.id.favourite_view,
            if (isFavourite) R.mipmap.note_btn_loved else R.mipmap.note_btn_love_white
        )
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    interface NotificationHelperListener {
        fun onNotificationInit()
    }
}