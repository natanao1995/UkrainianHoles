package com.example.ukrainianholes.feature.add_hole.add

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.BaseActivity
import com.example.ukrainianholes.architecture.base.ResultError
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.feature.add_hole.AddPhotoDialog
import com.example.ukrainianholes.feature.add_hole.map.MapActivity
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

    private var addPhotoDialog: AddPhotoDialog? = null

    private val viewModel by viewModel<AddHoleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hole)

        intent?.let {
            viewModel.addPhoto(intent.getLongExtra(EXTRA_KEY_IMAGE_ID, -1))
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

            val items = ArrayList<AdapterItem>(hole.photos.map { PhotoItem(it) })
            items.add(AddItem())
            (recyclerPhotos.adapter as? PhotoRecyclerAdapter)?.setItems(items)
        })

        viewModel.takePhotoFromGalleryIntent.observe(this, Observer { intent ->
            when (intent) {
                is ResultSuccess -> startActivityForResult(intent.data, MapActivity.REQUEST_CODE_GALLERY)
                is ResultError -> showError("Упс, щось пішло не так")
            }
        })

        viewModel.takePhotoFromCameraIntent.observe(this, Observer { intent ->
            when (intent) {
                is ResultSuccess -> startActivityForResult(intent.data, MapActivity.REQUEST_CODE_CAMERA)
                is ResultError -> showError("Упс, щось пішло не так")
            }
        })

        viewModel.uploadedFileIdLiveData.observe(this, Observer { id ->
            id ?: return@Observer

            when (id) {
                is ResultSuccess -> {
                    viewModel.addPhoto(id.data)
                    (recyclerPhotos.adapter as? PhotoRecyclerAdapter)?.addItems(PhotoItem(id.data))
                }
                is ResultError -> showError("Упс, щось пішло не так")
            }
        })
    }

    private fun setupUi() {
        editTextComment.afterTextChanged {
            viewModel.setAddress(it)
        }
        recyclerPhotos.adapter = object : PhotoRecyclerAdapter() {
            override fun onAddItemClick() {
                super.onAddItemClick()
                showAddPhotoDialog()
            }

            override fun onPhotoClick(id: Long) {
                super.onPhotoClick(id)
            }
        }
        buttonNext.setOnClickListener {
            viewModel.addHole()
        }
    }

    private fun showAddPhotoDialog() {
        addPhotoDialog?.let {
            it.show()
            return
        }

        addPhotoDialog = object : AddPhotoDialog(this) {
            override fun takeFromCamera() {
                super.takeFromCamera()
                viewModel.takePhotoFromCamera(this@AddHoleActivity)
            }

            override fun takeFromGallery() {
                super.takeFromGallery()
                viewModel.takePhotoFromGallery(this@AddHoleActivity)
            }

            override fun onSkipClick() {
                super.onSkipClick()
                dismiss()
            }

            override fun setupBottomButtonText(text: String?) {
                super.setupBottomButtonText("Скасувати")
            }
        }
        addPhotoDialog?.show()
    }
}