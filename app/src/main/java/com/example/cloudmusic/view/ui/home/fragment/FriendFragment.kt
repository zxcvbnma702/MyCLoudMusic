package com.example.cloudmusic.view.ui.home.fragment

import android.app.AlertDialog
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.TimeUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloudmusic.R
import com.example.cloudmusic.application.MusicApplication
import com.example.cloudmusic.databinding.FragmentFriendBinding
import com.example.cloudmusic.model.Time
import com.example.cloudmusic.utils.TimeUtil
import com.example.cloudmusic.view.adapter.TimeAdapter
import java.util.*


class FriendFragment : Fragment() {

    private var timerStatus = false
    private lateinit var binding :FragmentFriendBinding

    private val list by lazy{
        ArrayList<Time>()
    }

    private val adapter by lazy{
        TimeAdapter(list)
    }

    private val TIMER = 1000

    private var timestamp = 0L
    private var interval = 0L

    private var mSoundPool: SoundPool? = null
    private var mSoundId = 0
    private var mStreamId = -1
    private var count  = 1

    private var mTimer: Timer? = null
    private var mTimerTask: TimerTask? = null
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                TIMER -> {
                    timestamp += 10
                    interval += 10
                    binding.tvTimer.text = TimeUtil.intervalToString(timestamp)
                    binding.tvInterval.text = TimeUtil.intervalToString(interval)
                }
                else -> {}
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.lvTimerLog.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        binding.lvTimerLog.adapter = adapter
        binding.ibPlayPause.setOnClickListener {
            timerStatus = !timerStatus;
            if (timerStatus) {
                binding.ibPlayPause.background = getDrawable(MusicApplication.context, R.drawable.note_btn_pause_white);
                startTimer();
            } else {
                binding.ibPlayPause.background = getDrawable(MusicApplication.context, R.drawable.note_btn_play_white);
                stopTimer();
            }
            binding.ibReset.isEnabled = !timerStatus;
            binding.ibTick.isEnabled = timerStatus;
        }
        binding.ibTick.setOnClickListener {
            adapter.addDate(Time(timestamp, count,  interval))
            count ++
            interval = 0L
            binding.tvInterval.text = TimeUtil.intervalToString(interval)
        }
        binding.ibReset.setOnClickListener {
            binding.ibReset.isEnabled = false;
            timestamp = 0L;
            interval = 0L;
            binding.tvTimer.text = TimeUtil.intervalToString(timestamp);
            binding.tvInterval.text = TimeUtil.intervalToString(interval);
            adapter.clear();
        }
    }
    private fun initData() {
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        mSoundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        mSoundId = mSoundPool!!.load(requireContext(), R.raw.guide, 1)
    }

    private fun startTimer() {
        mTimer = Timer()
        mTimerTask = object : TimerTask() {
            override fun run() {
                mHandler.sendEmptyMessage(TIMER)
            }
        }
        mTimer!!.schedule(mTimerTask, 0, 10)
        if (mStreamId == -1) {
            mStreamId = mSoundPool!!.play(mSoundId, 50f, 50f, 1, -1, 1f)
        } else {
            mSoundPool!!.resume(mStreamId)
        }
    }

    private fun stopTimer() {
        mTimer!!.cancel()
        mTimerTask!!.cancel()
        mSoundPool!!.pause(mStreamId)
    }
}