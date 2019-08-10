package com.example.ukrainianholes.feature.details

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.ResultError
import com.example.ukrainianholes.architecture.base.ResultLoading
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.data.remote.entity.AccidentRate.HIGH
import com.example.ukrainianholes.data.remote.entity.AccidentRate.LOW
import com.example.ukrainianholes.data.remote.entity.AccidentRate.MEDIUM
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

        val hole = intent.getParcelableExtra<HoleResponse>(KEY_HOLE)
        if (hole == null) {
            showError()
            finish()
        }
        viewModel.setHole(hole)
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
            showLikeProgress(false)

            when (like) {
                is ResultSuccess -> updateLikeState(like.data)
                is ResultError -> showError()
                is ResultLoading -> showLikeProgress(true)
            }
        })
    }

    private fun showLikeProgress(show: Boolean) {
        if (show) {
            imageLike.visibility = View.GONE
            progressLike.visibility = View.VISIBLE
        } else {
            imageLike.visibility = View.VISIBLE
            progressLike.visibility = View.GONE
        }
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
            .load(R.drawable.ic_heart_empty)
            .into(imageLike)
    }

    private fun showUnlikeImage() {
        Glide.with(imageLike)
            .load(R.drawable.ic_heart_filled)
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
        cardLow.setOnClickListener {
            viewModel.setAccidentRate(LOW)
        }
        cardMedium.setOnClickListener {
            viewModel.setAccidentRate(MEDIUM)
        }
        cardHigh.setOnClickListener {
            viewModel.setAccidentRate(HIGH)
        }
    }

    private fun loadHoleInfo(hole: HoleResponse) {
        loadAvatar(hole.photos.firstOrNull())
        textComment.text = hole.comment
        textAddress.text = hole.address

        updateLikeState(hole.like)
        showLikeProgress(false)

        when (hole.accidentRate) {
            1 -> {
                cardLow.foreground = getDrawable(R.drawable.bg_button_stroke)
                cardMedium.foreground = null
                cardHigh.foreground = null
            }
            2 -> {
                cardLow.foreground = null
                cardMedium.foreground = getDrawable(R.drawable.bg_button_stroke)
                cardHigh.foreground = null
            }
            3 -> {
                cardLow.foreground = null
                cardMedium.foreground = null
                cardHigh.foreground = getDrawable(R.drawable.bg_button_stroke)
            }
        }
    }

    private fun loadAvatar(photo: Photo?) {
        Glide
            .with(imageAvatar)
            .load(viewModel.getFullPhotoUrl(photo))
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(16)))
            .error(ColorDrawable(ContextCompat.getColor(imageAvatar.context, R.color.placeholderGray)))
            .into(imageAvatar)
    }

    companion object {
        const val KEY_HOLE = "key_hole"
    }
}