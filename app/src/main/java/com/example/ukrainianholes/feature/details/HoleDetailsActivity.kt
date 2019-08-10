package com.example.ukrainianholes.feature.details

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.data.remote.entity.HoleResponse
import com.example.ukrainianholes.data.remote.entity.Photo
import com.example.ukrainianholes.feature.add_hole.map.MapActivity
import com.example.ukrainianholes.feature.help.HelpActivity
import kotlinx.android.synthetic.main.activity_hole_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class HoleDetailsActivity : AppCompatActivity() {
    private val viewModel: HoleDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hole_details)
        setupListeners()
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.holeLiveData.observe(this, Observer { hole ->
            loadHoleInfo(hole)
        })
        viewModel.shareHoleLiveData.observe(this, Observer { intent ->
            if (intent == null) {
                showError("Не вдалось поділитись ямою :(")
            }
            startActivity(intent)
        })
        viewModel.likeHoleLiveData.observe(this, Observer { like ->
            if (like is ResultSuccess) {
                updateLikeState(like.data)
            }
        })
    }

    private fun updateLikeState(isLike: Boolean) {
        if (isLike) {
            showUnlikeImage()
        } else {
            showLikeImage()
        }
    }

    private fun showLikeImage() {
        Glide.with(imageLike)
            .load(R.drawable.ic_like)
            .into(imageLike)
    }

    private fun showUnlikeImage() {
        Glide.with(imageLike)
            .load(R.drawable.ic_unlike)
            .into(imageLike)
    }

    private fun showError(s: String = "Ой. Щось пішло не так...") {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private fun setupListeners() {
        buttonOnMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            val holeLocation = viewModel.getCurrentHoleLocation()
            intent.putExtra("hole_lat", holeLocation)
            intent.putExtra("hole_lng", holeLocation)
            startActivity(intent)
        }
        buttonHelp.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }
        imageClose.setOnClickListener {
            onBackPressed()
        }
        imageShare.setOnClickListener {
            viewModel.shareHole()
        }
        imageLike.setOnClickListener {
            viewModel.likeHole()
        }
    }

    private fun loadHoleInfo(hole: HoleResponse) {
        loadAvatar(hole.photos.firstOrNull())
        textComment.text = hole.comment
        textAddress.text = hole.address
        updateLikeState(hole.like)
    }

    private fun loadAvatar(photo: Photo?) {
        Glide
            .with(imageAvatar)
            .load(viewModel.getFullPhotoUrl(photo))
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(16)))
            .error(ColorDrawable(ContextCompat.getColor(imageAvatar.context, R.color.placeholderGray)))
            .into(imageAvatar)
    }
}