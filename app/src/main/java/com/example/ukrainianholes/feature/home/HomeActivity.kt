package com.example.ukrainianholes.feature.home

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.BaseActivity
import com.example.ukrainianholes.architecture.base.ResultSuccess
import com.example.ukrainianholes.data.remote.entity.HoleResponse
import com.example.ukrainianholes.feature.add_hole.map.MapActivity
import com.example.ukrainianholes.feature.all_holes.AllHolesActivity
import com.example.ukrainianholes.feature.details.HoleDetailsActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_stats.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {
    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupUi()
        setupObserve()

        viewModel.getStats()
    }

    private fun setupObserve() {
        viewModel.statsLiveData.observe(this, Observer {
            swipeRefresh.isRefreshing = false
            when (it) {
                is ResultSuccess -> {
                    (recyclerLastWins.adapter as? HoleRecyclerAdapter)?.setItems(it.data.holes)
                    textNewAmount?.text = it.data.createdCount.toString()
                    textInProgressAmount?.text = it.data.inProgressCount.toString()
                    textDoneAmount?.text = it.data.fixedCount.toString()
                }
            }
        })
    }

    private fun setupUi() {
        recyclerLastWins.adapter = object : HoleRecyclerAdapter() {
            override fun onItemClick(lastWin: HoleResponse) {
                super.onItemClick(lastWin)
                val intent = Intent(this@HomeActivity, HoleDetailsActivity::class.java)
                intent.putExtra(HoleDetailsActivity.KEY_HOLE, lastWin)
                startActivity(intent)
            }
        }
        recyclerLastWins.isNestedScrollingEnabled = false
        imageBack.setOnClickListener {
            onBackPressed()
        }
        constraintAdd.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
        swipeRefresh.setOnRefreshListener {
            viewModel.getStats()
        }
        constraintAll.setOnClickListener {
            startActivity(Intent(this, AllHolesActivity::class.java))
        }
    }
}
