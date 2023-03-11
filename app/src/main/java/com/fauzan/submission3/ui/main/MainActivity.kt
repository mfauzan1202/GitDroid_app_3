package com.fauzan.submission3.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fauzan.submission3.R
import com.fauzan.submission3.UserAdapter
import com.fauzan.submission3.data.Result
import com.fauzan.submission3.data.local.entity.UserEntity
import com.fauzan.submission3.databinding.ActivityMainBinding
import com.fauzan.submission3.ui.UserViewModel
import com.fauzan.submission3.ui.favorite.FavoriteActivity
import com.fauzan.submission3.ui.themePreference.MainViewModel
import com.fauzan.submission3.ui.themePreference.SettingPreference
import com.fauzan.submission3.ui.themePreference.ThemeModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: UserViewModel by viewModels {
            factory
        }
        val userAdapter = UserAdapter({ user ->
                if (user.isLoved) {
                    viewModel.deleteUser(user)
                } else {
                    viewModel.saveUser(user)
                }
            },
            this,
        )

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = userAdapter
        }
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchMenu
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setQuery("", true)
        searchView.isFocusable = true
        searchView.setIconifiedByDefault(false)
        searchView.requestFocusFromTouch()
        showLoading(false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                viewModel.setUser(query).observe(this@MainActivity) {
                    searchUser(it, userAdapter)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                showLoading(true)
                viewModel.setUser(newText).observe(this@MainActivity) {
                    searchUser(it, userAdapter)
                }
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        val switch = menu.findItem(R.id.myswitch)
        switch.setActionView(R.layout.switch_layout)
        val switchTheme = switch.actionView.findViewById<SwitchMaterial>(R.id.switch_theme)

        val pref = SettingPreference.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ThemeModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menu_fav -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun searchUser(it: Result<List<UserEntity>>, userAdapter: UserAdapter){
            when (it) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    userAdapter.submitList(it.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@MainActivity,
                        "Terjadi kesalahan" + it.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}