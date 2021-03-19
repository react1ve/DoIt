package com.reactive.todo.ui.adapters

import com.reactive.todo.R
import com.reactive.todo.base.BaseAdapter
import com.reactive.todo.base.ViewHolder
import com.reactive.todo.ui.screens.todolist.STATUS
import kotlinx.android.synthetic.main.item_status.view.*

data class StatusData(val name: STATUS, var isChecked: Boolean = false)

class StatusAdapter(private val listener: (position: Int) -> Unit) : BaseAdapter<StatusData>(R.layout.item_status) {

    override fun bindViewHolder(holder: ViewHolder, data: StatusData) {
        holder.itemView.apply {
            data.apply {

                text.text = when (name) {
                    STATUS.NEW -> resources.getString(R.string.status_new)
                    STATUS.IN_PROGRESS -> resources.getString(R.string.status_in_progress)
                    STATUS.DONE -> resources.getString(R.string.status_done)
                    STATUS.ALL -> resources.getString(R.string.status_none)
                }
                text.setBackgroundResource(if (isChecked) R.drawable.status_back_trans else 0)
                setOnClickListener { listener.invoke(holder.adapterPosition) }
            }
        }
    }

}