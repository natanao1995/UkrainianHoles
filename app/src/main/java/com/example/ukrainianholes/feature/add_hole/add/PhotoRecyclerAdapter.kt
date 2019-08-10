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

    open fun onAddItemClick() {}

    open fun onPhotoClick(id: Long) {}

    private val items = mutableListOf<AdapterItem>()

    fun setItems(items: List<AdapterItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type.id
    }

    override fun onCreateViewHolder(@NotNull parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemType.PHOTO_ITEM.id ->
                PhotoViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.recycler_item_photo, parent, false)
                )
            ItemType.ADD_ITEM.id ->
                PlaceholderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.view_photo_placeholder,
                        parent,
                        false
                    )
                )
            else -> TODO("Add this type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is PhotoItem -> {
                holder.itemView.setOnClickListener { onPhotoClick(item.photoId) }
                (holder as PhotoViewHolder).bind(item)
            }
            is AddItem -> {
                holder.itemView.setOnClickListener { onAddItemClick() }
            }
        }
    }

    fun addItems(vararg photoItem: PhotoItem) {
        items.addAll(0, photoItem.toList())
        notifyDataSetChanged()
    }

    class PhotoViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(id: AdapterItem) {
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