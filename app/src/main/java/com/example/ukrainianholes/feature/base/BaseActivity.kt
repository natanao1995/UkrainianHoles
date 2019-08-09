package com.example.ukrainianholes.feature.base

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        setupNavigation()
        setupUi()
        setupObserve()
    }

    private fun setupNavigation() {
        NavigationUI.setupWithNavController(bottomNavigation, Navigation.findNavController(this, R.id.fragment))
        bottomNavigation.setOnNavigationItemReselectedListener { }
    }

    private fun setupUi() {

    }

    private fun setupObserve() {

    }
}
