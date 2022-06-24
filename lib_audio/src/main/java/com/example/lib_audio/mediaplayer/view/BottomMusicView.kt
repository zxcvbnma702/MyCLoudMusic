package com.example.lib_audio.mediaplayer.view

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.lib_audio.R
import com.example.lib_audio.mediaplayer.core.AudioController
import com.example.lib_audio.mediaplayer.event.AudioLoadEvent
import com.example.lib_audio.mediaplayer.event.AudioPauseEvent
import com.example.lib_audio.mediaplayer.event.AudioProgressEvent
import com.example.lib_audio.mediaplayer.event.AudioStartEvent
import kotlin.jvm.JvmOverloads
import com.example.lib_audio.mediaplayer.model.AudioBean
import com.example.lib_image_loader.app.ImageLoaderManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author:SunShibo
 * @date:2022-06-11 21:14
 * @feature: 播放器底部View
 */
class BottomMusicView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(
    mContext, attrs, defStyleAttr
) {
    /*
     * View
     */
    private var mLeftView: ImageView? = null
    private var mTitleView: TextView? = null
    private var mAlbumView: TextView? = null
    private var mPlayView: ImageView? = null
    private var mRightView: ImageView? = null

    /*
     * data
     */
    private var mAudioBean: AudioBean? = null
    private fun initView() {
        val rootView = LayoutInflater.from(mContext).inflate(R.layout.bottom_view, this)

        rootView.setOnClickListener {
//            MusicPlayerActivity.start(mContext as Activity)
        }

        // 左侧mLeftView 不停旋转
        mLeftView = rootView.findViewById(R.id.album_view)

        val animator = ObjectAnimator.ofFloat(mLeftView, ROTATION.name, 0f, 360f)
        animator.apply {
            duration = 10000
            interpolator = LinearInterpolator()
            repeatCount = -1
        }
        animator.start()

        mTitleView = rootView.findViewById(R.id.audio_name_view)
        mAlbumView = rootView.findViewById(R.id.audio_album_view)
        mPlayView = rootView.findViewById(R.id.play_view)

        (mPlayView as ImageView).setOnClickListener {

        }
        // 显示音乐列表
        mRightView = rootView.findViewById(R.id.show_list_view)
        (mRightView as ImageView).setOnClickListener{ //显示音乐列表对话框
//            val dialog = MusicListDialog(mContext)
//            dialog.show()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioLoadEvent(event: AudioLoadEvent) {
        //更新当前view为load状态
        mAudioBean = event.audioBean
        showLoadView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioPauseEvent(event: AudioPauseEvent?) {
        //更新当前view为暂停状态
        showPauseView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioStartEvent(event: AudioStartEvent?) {
        //更新当前view为播放状态
        showPlayView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioProgrssEvent(event: AudioProgressEvent?) {
        //更新当前view的播放进度
    }

    private fun showLoadView() {
        //目前loading状态的UI处理与pause逻辑一样，分开为了以后好扩展
        if (mAudioBean != null) {
            ImageLoaderManager.getInstance().displayImageForCircle(mLeftView, mAudioBean!!.albumPic)
            mTitleView!!.text = mAudioBean!!.name
            mAlbumView!!.text = mAudioBean!!.album
            mPlayView!!.setImageResource(R.mipmap.note_btn_pause_white)
        }
    }

    private fun showPauseView() {
        if (mAudioBean != null) {
            mPlayView!!.setImageResource(R.mipmap.note_btn_play_white)
        }
    }

    private fun showPlayView() {
        if (mAudioBean != null) {
            mPlayView!!.setImageResource(R.mipmap.note_btn_pause_white)
        }
    }

    init {
        EventBus.getDefault().register(this)
        initView()
    }
}