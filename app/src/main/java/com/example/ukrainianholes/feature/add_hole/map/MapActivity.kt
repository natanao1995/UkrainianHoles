package com.example.ukrainianholes.feature.add_hole.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.example.ukrainianholes.Constants.MAP_INITIAL_ZOOM
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.BaseActivity
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.architecture.base.ResultError
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.data.LatLngAddress
import com.example.ukrainianholes.feature.add_hole.AddPhotoDialog
import com.example.ukrainianholes.feature.add_hole.add.AddHoleActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_map.*
import org.koin.android.viewmodel.ext.android.viewModel

class MapActivity : BaseActivity(), OnMapReadyCallback {

    private val viewModel by viewModel<MapViewModel>()

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var marker: Marker? = null
    private var addPhotoDialog: AddPhotoDialog? = null

    private val bitmapDescriptorMarker: BitmapDescriptor by lazy {
        val markerSize = resources.getDimension(R.dimen.mapMarkerSize).toInt()

        val bitmapFar = BitmapFactory.decodeResource(resources, R.drawable.marker)
        val smallBitmapFar = Bitmap.createScaledBitmap(bitmapFar, markerSize, markerSize, false)
        BitmapDescriptorFactory.fromBitmap(smallBitmapFar)
    }

    companion object {
        const val REQUEST_CODE_GALLERY = 1
        const val REQUEST_CODE_CAMERA = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        map.onCreate(savedInstanceState)

        setupUi()
        setupObserve()

        viewModel.checkLocationPermissionGranted(this)

        MapsInitializer.initialize(applicationContext)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setupObserve() {
        viewModel.locationPermissionGrantedLiveData.observe(this, Observer { granted ->
            updateIsPermissionGranted(granted)
        })

        viewModel.latLngAddressLiveData.observe(this, Observer { latLngAddress ->
            updateLatLngAddress(latLngAddress)
        })

        buttonNext.setOnClickListener {
            showAddPhotoDialog()
        }
        viewModel.takePhotoFromGalleryIntent.observe(this, Observer { intent ->
            when (intent) {
                is ResultSuccess -> startActivityForResult(intent.data, REQUEST_CODE_GALLERY)
                is ResultError -> showError("Упс, щось пішло не так")
            }
        })

        viewModel.takePhotoFromCameraIntent.observe(this, Observer { intent ->
            when (intent) {
                is ResultSuccess -> startActivityForResult(intent.data, REQUEST_CODE_CAMERA)
                is ResultError -> showError("Упс, щось пішло не так")
            }
        })

        viewModel.uploadedFileIdLiveData.observe(this, Observer { id ->
            id ?: return@Observer

            when (id) {
                is ResultSuccess -> {
                    startAddHoleActivity(id.data)
                }
                is ResultError -> showError("Упс, щось пішло не так")
            }
        })
    }

    private fun startAddHoleActivity(holeId: Long? = null) {
        val intent = Intent(this, AddHoleActivity::class.java)
        holeId?.let {
            intent.putExtra("123321", it)
        }
        startActivity(intent)
    }

    private fun showAddPhotoDialog() {
        addPhotoDialog?.let {
            it.show()
            return
        }

        addPhotoDialog = object : AddPhotoDialog(this) {
            override fun takeFromCamera() {
                super.takeFromCamera()
                viewModel.takePhotoFromCamera(this@MapActivity)
            }

            override fun takeFromGallery() {
                super.takeFromGallery()
                viewModel.takePhotoFromGallery(this@MapActivity)
            }

            override fun onSkipClick() {
                super.onSkipClick()
                startAddHoleActivity()
            }
        }
        addPhotoDialog?.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GALLERY -> {
                    showMessage(data?.data.toString())
                    viewModel.uploadFileFromGallery(data?.data)
                }
                REQUEST_CODE_CAMERA -> {
                    showMessage(viewModel.imageUri.toString())
                    viewModel.uploadFileFromCamera()
                }
            }
        }
    }

    private fun updateLatLngAddress(latLngAddress: Result<LatLngAddress>?) {
        when (latLngAddress) {
            is ResultSuccess -> {
                editTextSearch.setText(latLngAddress.data.address)
                editTextSearch.setSelection(editTextSearch.text.length)
                if (marker == null) {
                    marker = mMap.addMarker(
                        MarkerOptions()
                            .position(latLngAddress.data.latLng)
                            .icon(bitmapDescriptorMarker)
                            .draggable(true)
                    )
                } else {
                    marker?.position = latLngAddress.data.latLng
                }
                mMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder()
                            .target(latLngAddress.data.latLng)
                            .zoom(MAP_INITIAL_ZOOM)
                            .bearing(0f)
                            .build()
                    )
                )
            }
        }
    }

    private fun setupUi() {
        buttonGrantPermission.setOnClickListener {
            viewModel.checkLocationPermissionGranted(this)
        }

        imageClose.setOnClickListener {
            onBackPressed()
        }

        imageSearch.setOnClickListener {
            viewModel.updateLatLngAddress(editTextSearch.text.toString())
            hideKeyboard()
        }

        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.updateLatLngAddress(editTextSearch.text.toString())
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun updateIsPermissionGranted(granted: Boolean?) {
        if (granted != true) {
            buttonGrantPermission?.visibility = View.VISIBLE
            map?.visibility = View.GONE
            constraintSearch?.visibility = View.GONE
            buttonNext?.visibility = View.GONE
        } else {
            buttonGrantPermission?.visibility = View.GONE
            map?.visibility = View.VISIBLE
            constraintSearch?.visibility = View.VISIBLE
            buttonNext?.visibility = View.VISIBLE
            map?.getMapAsync(this)
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isMyLocationButtonEnabled = true
        }

        mMap.setOnMapClickListener { latLng ->
            latLng ?: return@setOnMapClickListener

            viewModel.updateLatLngAddress(latLng)
        }

        fusedLocationProviderClient.lastLocation?.addOnSuccessListener {
            it ?: return@addOnSuccessListener

            viewModel.updateLatLngAddress(LatLng(it.latitude, it.longitude))
        }
    }

    override fun onStart() {
        super.onStart()
        map?.onStart()
    }

    override fun onResume() {
        super.onResume()
        map?.onResume()
    }

    override fun onStop() {
        super.onStop()
        map?.onStop()
        addPhotoDialog?.dismiss()
    }

    override fun onPause() {
        super.onPause()
        map?.onPause()
    }

    override fun onDestroy() {
        map?.onDestroy()
        super.onDestroy()
    }
}
