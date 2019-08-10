package com.example.ukrainianholes.feature.add_hole.add

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
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_item_photo.*
import org.jetbrains.annotations.NotNull

open class PhotoRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    open fun onItemClick(id: Long) {}

    private val items = mutableListOf<Long>()

    fun setItems(items: List<Long>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) 0 else 1
    }

    override fun onCreateViewHolder(@NotNull parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            PhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_photo, parent, false))
        } else {
            PlaceholderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_photo_placeholder,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == items.size) return

        val task = items[position]
        holder.itemView.setOnClickListener {
            onItemClick(task)
        }
        if (holder is PhotoViewHolder) {
            holder.bind(task)
        }
    }

    class PhotoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(id: Long) {
            Glide.with(imagePhoto).clear(imagePhoto)
            Glide
                .with(imagePhoto)
                .load("${Constants.BASE_URL}file/$id")
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(16)))
                .error(ColorDrawable(ContextCompat.getColor(imagePhoto.context, R.color.placeholderGray)))
                .into(imagePhoto)
        }
    }

    class PlaceholderViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer
}