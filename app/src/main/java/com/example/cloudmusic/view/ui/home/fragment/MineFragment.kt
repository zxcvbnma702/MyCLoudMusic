package com.example.cloudmusic.view.ui.home.fragment

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloudmusic.databinding.FragmentMineBinding
import com.example.cloudmusic.view.adapter.MusicAdapter
import com.example.lib_audio.mediaplayer.app.AudioHelper
import com.example.lib_audio.mediaplayer.utils.MusicUtil

class MineFragment : Fragment() {

    private lateinit var binding: FragmentMineBinding
    private val adapter by lazy{
        MusicAdapter(music)
    }
    private val music by lazy{
        MusicUtil.getSong(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMineBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun initView() {
        AudioHelper.addAudio(music)

        binding.localMusic.setOnClickListener {
            binding.localMusicList.visibility = View.VISIBLE
            binding.localMusic.visibility = View.GONE
        }
        binding.localMusicList.layoutManager = LinearLayoutManager(requireContext())
        binding.localMusicList.adapter = adapter
    }

}