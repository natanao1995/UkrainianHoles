package com.example.ukrainianholes.architecture.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.ukrainianholes.R
import com.pd.chocobar.ChocoBar

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
    }

    protected fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun showError(message: String) {
        ChocoBar.builder().setActivity(this)
            .setText(message)
            .setDuration(ChocoBar.LENGTH_SHORT)
            .red()
            .show()
    }

    protected fun showMessage(message: String) {
        ChocoBar.builder().setActivity(this)
            .setText(message)
            .setDuration(ChocoBar.LENGTH_SHORT)
            .green()
            .show()
    }
}