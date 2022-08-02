package com.example.lesson16.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.lesson16.R
import com.example.lesson16.databinding.ActivitySettingsBinding
import com.example.lesson16.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    private var bindingSettings: ActivitySettingsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingSettings = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(bindingSettings?.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SettingsFragment())
            .commit()

        addToolBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingSettings = null
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun addToolBar() {
        setSupportActionBar(bindingSettings?.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}