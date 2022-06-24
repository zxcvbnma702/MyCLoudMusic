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
import com.example.lib_image_loader.app.ImageLoaderManager
import com.google.android.material.tabs.TabLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeActivity : FragmentActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding

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