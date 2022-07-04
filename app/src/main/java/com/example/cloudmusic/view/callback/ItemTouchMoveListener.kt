package com.example.cloudmusic.view.callback

/**
 * @author:SunShibo
 * @date:2022-07-01 23:36
 * @feature:
 */
interface ItemTouchMoveListener {
    /**
     * @param fromPosition 从什么位置拖
     * @param toPosition 到什么位置
     * @return 是否执行了move
     */
    fun onItemMove(fromPosition: Int, endPosition: Int): Boolean

    /**
     * 当条目被移除时回调
     * @param position 移除的位置
     * @return
     */
    fun onItemRemove(position: Int): Boolean
}