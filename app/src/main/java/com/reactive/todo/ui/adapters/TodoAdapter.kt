package com.reactive.todo.ui.adapters

import android.annotation.SuppressLint
import android.os.Handler
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.reactive.todo.R
import com.reactive.todo.base.BaseAdapter
import com.reactive.todo.base.ViewHolder
import com.reactive.todo.data.db.TodoRecord
import com.reactive.todo.ui.screens.todolist.STATUS
import com.reactive.todo.utils.extensions.autoNotify
import com.reactive.todo.utils.extensions.gone
import com.reactive.todo.utils.extensions.visible
import com.reactive.todo.utils.views.SwipeLayout
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoAdapter(private val listener: (data: TodoRecord, deleted: Boolean) -> Unit) :
        BaseAdapter<TodoRecord>(R.layout.item_todo), Filterable {

    private var backup = arrayListOf<TodoRecord>()
    private var itemsOffset = IntArray(0)
    override fun setData(data: List<TodoRecord>) {
        backup = ArrayList(data)
        autoNotify(items, data) { old, new ->
            old.id == new.id
                    && old.title == new.title
                    && old.content == new.content
                    && old.date == new.date
                    && old.status == new.status
        }
        items = ArrayList(data)
        itemsOffset = IntArray(data.size)
    }

    @SuppressLint("SetTextI18n")
    override fun bindViewHolder(holder: ViewHolder, data: TodoRecord) {
        holder.itemView.apply {
            data.let {
                dataBlock.setOnClickListener { listener.invoke(data, false) }

                title.text = it.title
                desc.text = it.content

                when (it.status) {
                    STATUS.NEW -> {
                        statusLayout.visible()
                        statusLayout.setBackgroundResource(R.drawable.new_status_back)
                        status.text = context.getString(R.string.status_new)
                    }
                    STATUS.IN_PROGRESS -> {
                        statusLayout.visible()
                        statusLayout.setBackgroundResource(R.drawable.inprogress_status_back)
                        status.text = context.getString(R.string.status_in_progress)
                    }
                    STATUS.DONE -> {
                        statusLayout.visible()
                        statusLayout.setBackgroundResource(R.drawable.done_status_back)
                        status.text = context.getString(R.string.status_done)
                    }
                    else -> statusLayout.gone()
                }
            }
        }

        bindSwipe(holder, holder.adapterPosition)
    }

    private fun bindSwipe(holder: ViewHolder, position: Int) {
        holder.itemView.swipeLayout.apply {
            offset = itemsOffset[position]
            setOnSwipeListener(object : SwipeLayout.OnSwipeListener {
                override fun onBeginSwipe(
                        swipeLayout: SwipeLayout,
                        moveToRight: Boolean
                ) {
                }

                override fun onSwipeClampReached(
                        swipeLayout: SwipeLayout,
                        moveToRight: Boolean
                ) {
                    Handler().postDelayed({
                        swipeLayout.animateReset()
                        listener.invoke(items[holder.adapterPosition], true)
                    }, 500)
                }

                override fun onLeftStickyEdge(
                        swipeLayout: SwipeLayout,
                        moveToRight: Boolean
                ) {
                }

                override fun onRightStickyEdge(
                        swipeLayout: SwipeLayout,
                        moveToRight: Boolean
                ) {
                }
            })
        }

    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        if (holder.adapterPosition != RecyclerView.NO_POSITION) {
            itemsOffset[holder.adapterPosition] = holder.itemView.swipeLayout.offset
        }
    }

    /**
     * Search Filter implementation
     * */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                items = if (charString.isEmpty()) {
                    backup
                } else {
                    ArrayList(backup.filter {
                        it.title.toLowerCase().contains(charString.toLowerCase()) || it.content.contains(charString.toLowerCase())
                    })
                }
                val filterResults = FilterResults()
                filterResults.values = items
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                items = ArrayList(p1?.values as List<TodoRecord>)
                notifyDataSetChanged()
            }
        }
    }
}