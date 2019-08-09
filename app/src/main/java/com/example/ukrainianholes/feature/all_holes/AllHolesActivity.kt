package com.example.ukrainianholes.feature.all_holes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.BaseActivity
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.data.remote.entity.Filter.ALL
import com.example.ukrainianholes.data.remote.entity.Filter.MY
import com.example.ukrainianholes.data.remote.entity.Filter.NEAR
import com.example.ukrainianholes.feature.home.HoleRecyclerAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_all_holes.*
import org.koin.android.viewmodel.ext.android.viewModel

class AllHolesActivity : BaseActivity() {

    private val viewModel by viewModel<AllHolesViewModel>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_holes)

        setupUi()
        setupObserve()
        viewModel.getAllHoles()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setupObserve() {
        viewModel.allHolesLiveData.observe(this, Observer { list ->
            list ?: return@Observer

            swipeRefresh.isRefreshing = false
            when (list) {
                is ResultSuccess -> {
                    (recyclerAllHoles.adapter as? HoleRecyclerAdapter)?.setItems(list.data)
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun setupUi() {
        recyclerAllHoles.adapter = HoleRecyclerAdapter()
        recyclerAllHoles.isNestedScrollingEnabled = false
        imageBack.setOnClickListener {
            onBackPressed()
        }
        swipeRefresh.setOnRefreshListener {
            viewModel.getAllHoles()
        }

        radioGroupFilter.setOnCheckedChangeListener { _, _ ->
            when (radioGroupFilter.checkedRadioButtonId) {
                R.id.radioNear -> {
                    fusedLocationProviderClient.lastLocation?.addOnSuccessListener {
                        it ?: return@addOnSuccessListener

                        viewModel.updateLatLng(LatLng(it.latitude, it.longitude))
                        viewModel.updateFilter(NEAR)
                    }
                }
                R.id.radioMy -> viewModel.updateFilter(MY)
                R.id.radioAll -> viewModel.updateFilter(ALL)
            }
        }
    }
}
