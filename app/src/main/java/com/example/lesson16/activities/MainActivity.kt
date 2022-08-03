package com.example.lesson16.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson16.R
import com.example.lesson16.activities.ResultActivity.Companion.KEY_TRANSMISSION_TEXT
import com.example.lesson16.adapters.MenuItemAdapter
import com.example.lesson16.databinding.ActivityMainBinding
import com.example.lesson16.interfaces.MenuNavigationListener
import com.example.lesson16.models.ItemMenu

class MainActivity : AppCompatActivity(), MenuNavigationListener {
    private var bindingMain: ActivityMainBinding? = null

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain?.root)

        addToolBar()

        fillingMenu()

        setUpAdapter()

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        listenerBtnResult()
    }

    override fun onDestroy() {
        super.onDestroy()
        bindingMain = null
    }

    override fun openSettingsActivity(name: String) {
        if (name == resources.getString(R.string.settings)) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        bindingMain?.drawerLayout?.closeDrawer(GravityCompat.START)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    private fun addToolBar() {
        val drawerLayout = bindingMain?.drawerLayout
        val toolBar = bindingMain?.toolBar

        setSupportActionBar(toolBar)

        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolBar,
            R.string.open,
            R.string.close
        )
        drawerLayout?.addDrawerListener(drawerToggle)
    }

    private fun fillingMenu(): List<ItemMenu> {
        return listOf(
            ItemMenu(
                resources.getString(R.string.home_page),
                R.drawable.ic_baseline_home_24
            ),
            ItemMenu(
                resources.getString(R.string.settings),
                R.drawable.ic_baseline_settings_24
            )
        )
    }

    private fun setUpAdapter() {
        val adapterMenu = MenuItemAdapter(this, fillingMenu(), this)

        bindingMain?.menuList?.apply {
            adapter = adapterMenu
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun listenerBtnResult() {
        bindingMain?.btnResult?.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra(KEY_TRANSMISSION_TEXT, bindingMain?.container?.text)
            startActivity(intent)
        }
    }
}