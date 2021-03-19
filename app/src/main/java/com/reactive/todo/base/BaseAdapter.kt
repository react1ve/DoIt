package com.reactive.todo.base

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.reactive.todo.utils.extensions.inflate

abstract class BaseAdapter<T>(@LayoutRes val layoutID: Int) : RecyclerView.Adapter<ViewHolder>() {

    protected var items = arrayListOf<T>()
    open fun setData(data: List<T>) {
        items = ArrayList(data)
        notifyDataSetChanged()
    }

    open fun isEmpty() = items.isEmpty()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(parent.inflate(layoutID))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bindViewHolder(this, items[holder.adapterPosition])
        }
    }

    abstract fun bindViewHolder(holder: ViewHolder, data: T)

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view)