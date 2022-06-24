package com.example.cloudmusic.view.login.manager

import com.example.cloudmusic.view.login.entity.User

/**
 * @author:SunShibo
 * @date:2022-05-31 21:34
 * @feature: 用户信息管理
 */
object UserManager {
    private var user: User? = null

    /**
     * 保存用户
     */
    fun saveUser(user: User) {
        this.user = user
        saveLocal(user)
    }

    /**
     * 从本地获取用户
     */
    fun getUser(): User? {
        return getLocal();
    }

    /**
     * 从本地删除用户
     */
    fun removeUser() {
        user = null
        removeLocal()
    }

    /**
     * 判断是否登录
     */
    fun hasLogined(): Boolean = getUser() != null


    /**
     * 从本地获取信息
     */
    private fun getLocal(): User? {
        return null
    }

    /**
     * 从本地删除信息
     */
    private fun removeLocal() {

    }

    /**
     * 保存信息到本地
     */
    private fun saveLocal(user: User) {

    }
}