package com.example.lib_audio.mediaplayer.core

import android.util.Log
import com.example.lib_audio.mediaplayer.event.AudioCompleteEvent
import com.example.lib_audio.mediaplayer.event.AudioErrorEvent
import com.example.lib_audio.mediaplayer.event.AudioPlayModeEvent
import com.example.lib_audio.mediaplayer.event.AudioRemoveEvent
import com.example.lib_audio.mediaplayer.model.AudioBean
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

/**
 * @author:SunShibo
 * @date:2022-06-11 13:01
 * @feature:音频控制类
 */
object AudioController {
    enum class PlayMode{
        LOOP,
        RANDOM,
        REPEAT
    }

    private var audioPlayer: AudioPlayer = AudioPlayer()
    private var queue: ArrayList<AudioBean> = ArrayList()
    private var indexQueue = 0
    private var playMode: PlayMode

    init {
        EventBus.getDefault().register(this)
        indexQueue = 0
        playMode = PlayMode.LOOP
    }

    fun isStartState(): Boolean = CustomMediaPlayer.Status.STARTED == getStatus()
    fun isPauseState(): Boolean = CustomMediaPlayer.Status.PAUSED == getStatus()

    fun getQueue(bean: AudioBean) = queue

    fun addAudio(bean: AudioBean) = addAudio(0, bean)

    fun addAudio(index: Int, bean: AudioBean){
        val query = queue.indexOf(bean)
        if(query <= -1){
            queue.add(index, bean)
            //设置播放索引
            setPlayIndex(index)
        }else{
            val currentPlaying = getCurrentPlaying()
            if(currentPlaying.id != bean.id){
                setPlayIndex(query)
            }
        }
    }

    fun addAudio(list: ArrayList<AudioBean>) = queue.addAll(list)

    fun removeAudio(bean: AudioBean){
        if(queue.remove(bean)){
            EventBus.getDefault().post(AudioRemoveEvent())
        }
    }

    fun removeAudio(){
        val currentBean = getCurrentPlaying()
        queue.clear()
        //不移除当前播放的歌曲
        queue.add(currentBean)
    }

    fun setQueue(list: ArrayList<AudioBean>) = setQueue(list, 0)
    fun setQueue(list: ArrayList<AudioBean>, index: Int){
        queue.addAll(list)
        indexQueue = index
    }

    fun getPlayMode() = playMode

    fun setPlayMode(playMode: PlayMode){
        this.playMode = playMode
        EventBus.getDefault().post(AudioPlayModeEvent(playMode))
    }

    /**
     * 设置播放索引
     */
    fun setPlayIndex(index: Int){
        indexQueue = index
        play()
    }

    fun play() = audioPlayer.load(getCurrentPlaying())
    fun pause() = audioPlayer.pause()
    fun resume() = audioPlayer.resume()
    fun seekTo(time: Long) = audioPlayer.seekTo(time)
    fun next() = audioPlayer.load(getNextPlaying())
    fun previous() = audioPlayer.load(getPreviousPlaying())
    fun release() {
        audioPlayer.release()
        EventBus.getDefault().unregister(this)
    }

    private fun getNextPlaying(): AudioBean {
        when(playMode){
            //列表循环
            PlayMode.LOOP -> {
                indexQueue = (indexQueue + 1) % queue.size
                return getPlaying(indexQueue)
            }
            //随机播放
            PlayMode.RANDOM -> {
                indexQueue = Random.nextInt(queue.size) % queue.size
                return getPlaying(indexQueue)
            }
            //单曲循环
            PlayMode.REPEAT -> {
                return getPlaying(indexQueue)
            }
        }
    }

    private fun getPreviousPlaying(): AudioBean {
        when(playMode){
            //列表循环
            PlayMode.LOOP -> {
                indexQueue = (indexQueue - 1 + queue.size) % queue.size
                return getPlaying(indexQueue)
            }
            //随机播放
            PlayMode.RANDOM -> {
                indexQueue = Random.nextInt(queue.size) % queue.size
                return getPlaying(indexQueue)
            }
            //单曲循环
            PlayMode.REPEAT -> {
                return getPlaying(indexQueue)
            }
        }
    }

    fun getPlaying(index : Int): AudioBean {
        return if(queue.isNotEmpty() && index < queue.size && index >= 0){
            queue[index]
        }else{
            AudioBean()
        }
    }

    fun playOrPause(){
        if(getStatus() == CustomMediaPlayer.Status.IDLE){
            play()
            for(audio in queue){
                Log.e("tag", audio.name)
            }
        }
        if(isStartState()){
            pause()
        }else if(isPauseState()){
            resume()
        }
    }

    fun getNowPlaying(): AudioBean = getCurrentPlaying();

    private fun getCurrentPlaying(): AudioBean = getPlaying(indexQueue)
    private fun getStatus(): CustomMediaPlayer.Status = audioPlayer.getStatus()

    //插放完毕事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioCompleteEvent(event: AudioCompleteEvent){
        //播放下一首
        next()
    }

    //播放出错事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAudioErrorEvent(event: AudioErrorEvent){
        //播放下一首
        next()
    }

}