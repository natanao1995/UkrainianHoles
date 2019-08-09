package com.example.ukrainianholes.feature.home

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.BaseActivity
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.feature.map.MapActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {
    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupUi()
        setupObserve()

        viewModel.getLastWins()
    }

    private fun setupObserve() {
        viewModel.lastWinsLiveData.observe(this, Observer {
            when (it) {
                is ResultSuccess -> {
                    (recyclerLastWins.adapter as? LastWinsRecyclerAdapter)?.setItems(it.data)
                }
            }
        })
    }

    private fun setupUi() {
        recyclerLastWins.adapter = LastWinsRecyclerAdapter()
        buttonAddHole.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
    }
}
