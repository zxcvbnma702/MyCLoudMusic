package com.example.cloudmusic.view.home

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cloudmusic.R
import com.example.cloudmusic.databinding.ActivityHomeBinding
import com.example.cloudmusic.view.home.fragment.DiscoveryFragment
import com.example.cloudmusic.view.home.fragment.FriendFragment
import com.example.cloudmusic.view.home.fragment.MineFragment
import com.example.cloudmusic.view.login.LoginActivity
import com.example.cloudmusic.view.login.entity.LoginEvent
import com.example.cloudmusic.view.login.manager.UserManager
import com.example.lib_audio.mediaplayer.app.AudioHelper
import com.example.lib_audio.mediaplayer.model.AudioBean
import com.example.lib_image_loader.app.ImageLoaderManager
import com.google.android.material.tabs.TabLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HomeActivity : FragmentActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding
    private val mLists: ArrayList<AudioBean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun initData() {
        mLists.add(
            AudioBean(
                "100001", "http://192.168.0.9:8081/父亲的散文诗.m4a",
                "以你的名字喊我", "周杰伦", "七里香", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://portrait.gitee.com/uploads/avatars/user/412/1236427_aqie_1578947067.png!avatar60",
                "4:30"
            )
        )
        mLists.add(
            AudioBean(
                "100002", "http://192.168.0.9:8081/云雨成烟.m4a", "云雨成烟",
                "梁静茹", "勇气", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698193627&di=711751f16fefddbf4cbf71da7d8e6d66&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D213168965%2C1040740194%26fm%3D214%26gp%3D0.jpg",
                "4:40"
            )
        )
        mLists.add(
            AudioBean(
                "100003", "http://192.168.0.9:8081/walk on the water.m4a", "walk",
                "汪峰", "春天里", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698239736&di=3433a1d95c589e31a36dd7b4c176d13a&imgtype=0&src=http%3A%2F%2Fpic.zdface.com%2Fupload%2F201051814737725.jpg",
                "3:20"
            )
        )
        mLists.add(
            AudioBean(
                "100004", "http://192.168.0.9:8081/wind.m4a", "风停了我们还拥抱",
                "五月天", "小幸运", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698289780&di=5146d48002250bf38acfb4c9b4bb6e4e&imgtype=0&src=http%3A%2F%2Fpic.baike.soso.com%2Fp%2F20131220%2Fbki-20131220170401-1254350944.jpg",
                "2:45"
            )
        )
        AudioHelper.addAudio(mLists)
    }

    private fun initView() {
        initTab()
        initLogin()
    }

    private fun initLogin() {
        binding.unloginLayout.setOnClickListener(this)
    }

    private fun initTab() {

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { binding.viewPager.currentItem = it.position }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.viewPager.adapter = SimpleFragmentPagerAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private inner class SimpleFragmentPagerAdapter constructor(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val title = arrayOf("我的", "发现", "朋友")

        override fun getItem(position: Int): Fragment {
            when (position){
                0 -> return MineFragment()
                1 -> return DiscoveryFragment()
                2 -> return FriendFragment()
            }
            return MineFragment()
        }

        override fun getCount(): Int {
            return title.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return title[position]
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.unlogin_layout -> if(!UserManager.hasLogined()){
                                        LoginActivity.start(this)
                                    }else{
                                        binding.drawerLayout.closeDrawer(Gravity.LEFT)
                                    }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent){
        binding.unloginLayout.visibility = View.GONE
        binding.avatrView.visibility = View.VISIBLE
        ImageLoaderManager.getInstance().displayImageForCircle(binding.avatrView,
            UserManager.getUser()?.photoURL)
    }
}