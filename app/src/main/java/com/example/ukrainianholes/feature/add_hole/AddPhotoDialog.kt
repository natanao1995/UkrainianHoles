package com.example.ukrainianholes.feature.add_hole

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.ukrainianholes.R
import kotlinx.android.synthetic.main.dialog_add_photo.*

open class AddPhotoDialog(context: Context) : Dialog(context, R.style.ChooseDialog) {
    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_photo)
//        window?.setGravity(Gravity.BOTTOM)
        setupListeners()
    }

    private fun setupListeners() {
        buttonFromCamera.setOnClickListener {
            takeFromCamera()
        }
        buttonFromGallery.setOnClickListener {
            takeFromGallery()
        }
        buttonSkip.setOnClickListener {
            onSkipClick()
        }
    }

    open fun takeFromCamera() {

    }

    open fun takeFromGallery() {

    }

    open fun onSkipClick() {

    }
}