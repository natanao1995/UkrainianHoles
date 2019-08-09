package com.example.ukrainianholes.feature.map

import android.annotation.SuppressLint
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
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.data.LatLngAddress
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

    private val bitmapDescriptorMarker: BitmapDescriptor by lazy {
        val markerSize = resources.getDimension(R.dimen.mapMarkerSize).toInt()

        val bitmapFar = BitmapFactory.decodeResource(resources, R.drawable.marker)
        val smallBitmapFar = Bitmap.createScaledBitmap(bitmapFar, markerSize, markerSize, false)
        BitmapDescriptorFactory.fromBitmap(smallBitmapFar)
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
        } else {
            buttonGrantPermission?.visibility = View.GONE
            map?.visibility = View.VISIBLE
            constraintSearch?.visibility = View.VISIBLE
            map?.getMapAsync(this)
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

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
