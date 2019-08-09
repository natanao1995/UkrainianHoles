package com.example.ukrainianholes.feature.home

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.ukrainianholes.Constants
import com.example.ukrainianholes.R
import com.example.ukrainianholes.data.remote.entity.HoleResponse
import com.example.ukrainianholes.data.remote.entity.Status
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_item_last_win.*
import org.jetbrains.annotations.NotNull

open class HoleRecyclerAdapter : RecyclerView.Adapter<HoleRecyclerAdapter.LastWinViewHolder>() {

    open fun onItemClick(lastWin: HoleResponse) {}

    private val items = mutableListOf<HoleResponse>()

    fun setItems(items: List<HoleResponse>) {
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
        fun bind(lastWin: HoleResponse) {
            textAddress.text = lastWin.address
            textDays.text = lastWin.insertedAt
            textLikes.text = lastWin.likesNumber.toString()
            if (lastWin.like) {
                textLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_filled, 0, 0, 0)
            } else {
                textLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_empty, 0, 0, 0)
            }
            when (lastWin.status) {
                Status.CREATED -> {
                    textStatus.text = "Нова"
                    textStatus.setBackgroundResource(R.drawable.bg_status_new)
                    textStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
                Status.IN_PROGRESS -> {
                    textStatus.text = "В роботі"
                    textStatus.setBackgroundResource(R.drawable.bg_status_in_progress)
                    textStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
                Status.FIXED -> {
                    textStatus.text = "Готово"
                    textStatus.setBackgroundResource(R.drawable.bg_status_done)
                    textStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
                Status.CONFIRMED -> {
                    textStatus.text = "Перевірено заявником"
                    textStatus.setBackgroundResource(R.drawable.bg_status_confirmed)
                    textStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star, 0, 0, 0)
                }
            }
            Glide.with(imageAvatar).clear(imageAvatar)
            Glide
                .with(imageAvatar)
                .load("${Constants.BASE_URL}file/${lastWin.photos.firstOrNull()?.id}")
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(16)))
                .error(ColorDrawable(ContextCompat.getColor(imageAvatar.context, R.color.placeholderGray)))
                .into(imageAvatar)
        }
    }
}