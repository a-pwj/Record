package com.pwj.record.ui.adpter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pwj.record.ext.yes

/**
 * @Author:          pwj
 * @Date:            2020/5/19 16:40
 * @FileName:        BaseAdapter
 * @Description:     description
 */
abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    val mData = mutableListOf<T>()

    lateinit var OnItemClickListenr: (position: Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        val holder = BaseViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (::OnItemClickListenr.isInitialized) OnItemClickListenr.invoke(position)
        }
        return holder
    }

    abstract fun getLayoutId(): Int

    fun setData(c: Collection<T>) {
        mData.isNotEmpty().yes { mData.clear() }
        mData.addAll(c)
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    open class BaseViewHolder(private val mConvertView: View) : RecyclerView.ViewHolder(mConvertView) {

        private val mViews = SparseArray<View?>()

        fun <T : View> getView(viewId: Int): T {
            var view = mViews[viewId]
            if (view == null) {
                view = mConvertView.findViewById(viewId)
                mViews.put(viewId, view)
            }
            return view!! as T
        }
    }
}