package com.example.ukrainianholes.feature.main

import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
