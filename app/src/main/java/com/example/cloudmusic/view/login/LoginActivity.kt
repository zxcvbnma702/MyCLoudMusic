package com.example.cloudmusic.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cloudmusic.databinding.ActivityLoginBinding
import com.example.cloudmusic.net.RequestCenter
import com.example.cloudmusic.view.login.entity.LoginEvent
import com.example.cloudmusic.view.login.entity.User
import com.example.cloudmusic.view.login.manager.UserManager
import com.example.lib_common_ui.base.BaseActivity
import com.example.lib_network.okhttp.listener.DisposeDataListener
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO: 解析数据有问题, 不知道是网络请求出问题还是没发过来
        binding.loginView.setOnClickListener {
            RequestCenter.login(object: DisposeDataListener {
                override fun onSuccess(responseObj: Any) {
                    val user = responseObj as User
                    Log.e("reason", responseObj.toString())
                    Log.e("data", user.name + user.mobile + user.photoURL)
                    UserManager.saveUser(user)
                    EventBus.getDefault().post(LoginEvent())
                    finish()
                }

                override fun onFailure(reasonObj: Any) {
                    Toast.makeText(this@LoginActivity, "登录失败${reasonObj.toString()}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}