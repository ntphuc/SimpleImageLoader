package com.line.assignment.simpleimageloader.ui.base

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection

/**
 * Created by PhucNguyen
 */


abstract class BaseActivity : AppCompatActivity() {

    protected abstract fun initializeViewModel()
    abstract fun observeViewModel()
    protected abstract fun initViewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        initViewBinding()
        initializeViewModel()
        observeViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun showToast(mes: String) {
        Toast.makeText(
                this,
                mes,
                Toast.LENGTH_LONG
        ).show()

    }

}
