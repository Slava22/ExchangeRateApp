package andrushkva.com.exchangerate

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import java.util.*

class DragManageAdapter(adapter: CurrenciesAdapter, dragDirs: Int, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    var currenciesAdapter = adapter

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        Collections.swap(currenciesAdapter.listCurrencies, viewHolder!!.adapterPosition, target!!.adapterPosition)
        Collections.swap(currenciesAdapter.listCurrenciesId, viewHolder.adapterPosition, target.adapterPosition)
        currenciesAdapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder!!.itemView.setBackgroundColor(Color.LTGRAY)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        viewHolder!!.itemView.setBackgroundColor(Color.WHITE)
        super.clearView(recyclerView, viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

    }

    override fun getSwipeDirs(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        return 0
    }
}