package com.example.ukrainianholes.feature.add_hole.add

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.BaseActivity
import com.example.ukrainianholes.architecture.base.ResultError
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.data.remote.entity.AccidentRate.HIGH
import com.example.ukrainianholes.data.remote.entity.AccidentRate.LOW
import com.example.ukrainianholes.data.remote.entity.AccidentRate.MEDIUM
import com.example.ukrainianholes.feature.home.HomeActivity
import com.example.ukrainianholes.util.afterTextChanged
import kotlinx.android.synthetic.main.activity_add_hole.*
import org.koin.android.viewmodel.ext.android.viewModel

class AddHoleActivity : BaseActivity() {

    companion object {
        const val EXTRA_KEY_LAT = "key_lat"
        const val EXTRA_KEY_LNG = "key_lng"
        const val EXTRA_KEY_ADDRESS = "key_address"
        const val EXTRA_KEY_IMAGE_ID = "key_image_id"
    }

    private val viewModel by viewModel<AddHoleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hole)

        intent?.let {
            val photoId = intent.getLongExtra(EXTRA_KEY_IMAGE_ID, -1)
            if (photoId != -1L) {
                viewModel.addPhoto(photoId)
            }
            viewModel.setLatLng(
                intent.getDoubleExtra(EXTRA_KEY_LAT, 0.0),
                intent.getDoubleExtra(EXTRA_KEY_LNG, 0.0)
            )
            val address = intent.getStringExtra(EXTRA_KEY_ADDRESS)
            viewModel.setAddress(address)
            editTextAddress.setText(address)
        }

        setupUi()
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.saveHoleResultLiveData.observe(this, Observer { result ->
            when (result) {
                is ResultSuccess -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                }
                is ResultError -> {
                    result.exception?.toString()?.let {
                        showError(it)
                    }
                }
            }
        })

        viewModel.newHoleLiveData.observe(this, Observer { hole ->
            hole ?: return@Observer

            (recyclerPhotos.adapter as? PhotoRecyclerAdapter)?.setItems(hole.photos)
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
        })
    }

    private fun setupUi() {
        editTextComment.afterTextChanged {
            viewModel.setComment(it)
        }
        recyclerPhotos.adapter = PhotoRecyclerAdapter()
        buttonNext.setOnClickListener {
            viewModel.addHole()
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
}