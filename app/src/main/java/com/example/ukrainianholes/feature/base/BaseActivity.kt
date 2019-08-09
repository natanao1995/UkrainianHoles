package com.example.ukrainianholes.feature.base

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.ukrainianholes.R
import com.example.ukrainianholes.architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
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
