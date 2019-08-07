package com.example.ukrainianholes.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ukrainianholes.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_item_last_win.*
import org.jetbrains.annotations.NotNull

open class LastWinsRecyclerAdapter : RecyclerView.Adapter<LastWinsRecyclerAdapter.LastWinViewHolder>() {

    open fun onItemClick(lastWin: LastWin) {}

    private val items = mutableListOf<LastWin>()

    fun setItems(items: List<LastWin>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(@NotNull parent: ViewGroup, viewType: Int): LastWinViewHolder {
        return LastWinViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_last_win, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LastWinViewHolder, position: Int) {
        val task = items[position]

        holder.itemView.setOnClickListener {
            onItemClick(task)
        }
        holder.bind(task)
    }

    class LastWinViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(lastWin: LastWin) {
            textAddress.text = lastWin.address
            textDate.text = lastWin.date
            textDaysFromOpen.text = lastWin.daysFromOpen
            textStatus.text = lastWin.status
            imageAvatar.setBackgroundResource(R.color.bottomNavigationSelectedItemColor)
//            textTitle.text = task.title
            //I wanted to use this format in list, but sorting by expiration time on server works wrong) "MM/dd/yyyy hh:mm"
//            textDueTo.text = DateFormat.format("MM/dd/yyyy", task.dueBy).toString()
        }
    }
}