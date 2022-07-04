package com.example.cloudmusic.view.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.cloudmusic.R
import com.example.cloudmusic.databinding.ActivityLoginBinding
import com.example.cloudmusic.utils.SpUtil
import com.example.cloudmusic.utils.ToastUtil
import com.example.cloudmusic.view.event.LoginEvent
import org.greenrobot.eventbus.EventBus

class LoginActivity : FragmentActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {

        if(SpUtil.getString("userName", "0") != "0" && SpUtil.getString("userName", "0") != "0"){
            binding.loginName.setText(SpUtil.getString("userName", "0"))
            binding.loginPwd.setText(SpUtil.getString("userPwd", "0"))
            binding.loginView.text = getString(R.string.login_text_login)
        }else{
            binding.loginView.text = getString(R.string.login_text_register)
        }

        binding.loginView.setOnClickListener {
            if (binding.loginSave.isChecked){
                SpUtil.putString("userName", binding.loginName.text.toString())
                SpUtil.putString("userPwd", binding.loginPwd.text.toString())
            }else{
                ToastUtil.MyToast("未保存数据", ToastUtil.LONG)
            }
            EventBus.getDefault().post(LoginEvent())
            finish()
        }
    }

    companion object{
        fun startLoginActivity(context: Context){
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}