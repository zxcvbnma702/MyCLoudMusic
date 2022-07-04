package com.example.cloudmusic.view.callback

import android.graphics.Canvas
import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudmusic.R
import kotlin.math.abs

/**
 * @author:SunShibo
 * @date:2022-07-01 23:42
 * @feature:
 */
class MyItemTouchHelperCallback(private val moveListener: ItemTouchMoveListener): ItemTouchHelper.Callback(){

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlag, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if(viewHolder.itemViewType != target.itemViewType){
            return false
        }
        return moveListener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        moveListener.onItemRemove(viewHolder.adapterPosition)
    }

    override fun isLongPressDragEnabled(): Boolean = true

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            viewHolder?.itemView?.setBackgroundColor(viewHolder.itemView.context.resources.getColor(
                R.color.color_999999))
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        //恢复
        viewHolder.itemView.setBackgroundColor(Color.WHITE)
        viewHolder.itemView.alpha = 1F
        //设置滑出大小
        //滑动后条目的缩放
        viewHolder.itemView.scaleX = 0.8F
        viewHolder.itemView.scaleX = 1F
        super.clearView(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ){
        //dX:水平方向移动的增量(负：往左；正：往右) 0-view.getWidth()
        val  alpha = 1 - abs(dX) / viewHolder.itemView.width
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            viewHolder.itemView.alpha = alpha
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}