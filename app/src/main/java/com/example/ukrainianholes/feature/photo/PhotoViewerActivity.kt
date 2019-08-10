package com.example.ukrainianholes.feature.photo

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.ukrainianholes.R
import kotlinx.android.synthetic.main.activity_photo_viewer.*

class PhotoViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_viewer)

        imageClose.setOnClickListener {
            onBackPressed()
        }

        val photoUrl = intent.getStringExtra(KEY_PHOTO_URL)
        Glide.with(imageMain)
            .load(photoUrl)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(this@PhotoViewerActivity, "Не вдалося відкрити фото :(", Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageProgress.visibility = View.GONE
                    return false
                }
            })
            .error(ColorDrawable(ContextCompat.getColor(imageMain.context, R.color.placeholderGray)))
            .into(imageMain)
    }

    companion object {
        const val KEY_PHOTO_URL = "photo_url"
    }
}