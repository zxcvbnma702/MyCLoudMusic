package com.example.cloudmusic.view.ui.home

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cloudmusic.R
import com.example.cloudmusic.databinding.ActivityHomeBinding
import com.example.cloudmusic.utils.ToastUtil
import com.example.cloudmusic.view.event.LoginEvent
import com.example.cloudmusic.view.ui.home.fragment.DiscoveryFragment
import com.example.cloudmusic.view.ui.home.fragment.FriendFragment
import com.example.cloudmusic.view.ui.home.fragment.MineFragment
import com.example.cloudmusic.view.ui.login.LoginActivity
import com.example.cloudmusic.view.ui.two.TwoActivity
import com.example.lib_audio.mediaplayer.model.AudioBean
import com.google.android.material.tabs.TabLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HomeActivity : FragmentActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding
    private val mLists: ArrayList<AudioBean> = ArrayList()

    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        isGranted ->
            for((name, granted) in isGranted){
                if (!granted){
                    ToastUtil.MyToast("${name}权限请求失败", ToastUtil.LONG)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventBus.getDefault().register(this)

        initPermission()
        initView()
        initData()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun initPermission() {
        requestPermissions.launch(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun initData() {
        mLists.add(
            AudioBean(
                "100001", "http://ws.stream.qqmusic.qq.com/C400001PxAKE1xglQ0.m4a?guid=301230377&vkey=5B37F1A1A94AB07607506D6A686829E45B9DB4FEA2788598A3F1C87398A81193DD9A65096B90617F938FD575C9818103D2EC32742D8713FE&uin=&fromtag=120032",
                "以你的名字喊我", "周杰伦", "七里香", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://portrait.gitee.com/uploads/avatars/user/412/1236427_aqie_1578947067.png!avatar60",
                "4:30"
            )
        )
        mLists.add(
            AudioBean(
                "100002", "http://ws.stream.qqmusic.qq.com/C400001MeDUC1ENTAd.m4a?guid=525620222&vkey=DCA7975C3B7BBBF9D7620FB1A7D77AA10B6421D2F1AC33B0FF4F5101F45FAF3B5EEF005D3CCA3BB2A1CC9EE1EDB04A1BBB228394285B4D98&uin=&fromtag=120032", "云雨成烟",
                "梁静茹", "勇气", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://profile.csdnimg.cn/7/F/E/3_qq_28261343",
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
//        AudioHelper.addAudio(mLists)
    }

    private fun initView() {
        initTab()
        initDrawer()
    }

    private fun initDrawer() {
        binding.unloginLayout.setOnClickListener(this@HomeActivity)
        binding.avatrView.setOnClickListener(this@HomeActivity)
        binding.twoBut.setOnClickListener(this@HomeActivity)
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

        binding.toggleView.setOnClickListener(this@HomeActivity)
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

        override fun getPageTitle(position: Int): CharSequence {
            return title[position]
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.unlogin_layout -> LoginActivity.startLoginActivity(this)
            R.id.toggle_view -> binding.drawerLayout.openDrawer(Gravity.LEFT)
            R.id.avatr_view -> {
                binding.drawerLayout.closeDrawer(Gravity.LEFT)
                binding.unloginLayout.visibility = View.VISIBLE
                binding.avatrView.visibility = View.GONE
            }
            R.id.two_but -> TwoActivity.startTwoActivity(this, binding.twoEdi.text.toString())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent){
        binding.unloginLayout.visibility = View.GONE
        binding.avatrView.visibility = View.VISIBLE
        binding.avatrView.setImageResource(R.mipmap.ic_launcher)
    }
}