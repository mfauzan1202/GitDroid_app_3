package com.fauzan.submission3.ui.favorite

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fauzan.submission3.R
import com.fauzan.submission3.UserAdapter
import com.fauzan.submission3.data.Result
import com.fauzan.submission3.data.local.entity.UserEntity
import com.fauzan.submission3.databinding.ActivityFavoriteBinding
import com.fauzan.submission3.ui.UserViewModel
import com.fauzan.submission3.ui.main.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
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
            this
        )

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
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
        viewModel.setFavoriteUser().observe(this@FavoriteActivity) {
            showLoading(true)
            getUser(it, userAdapter)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                viewModel.searchFavoriteUser(query).observe(this@FavoriteActivity) {
                    getUser(it, userAdapter)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                showLoading(true)
                viewModel.searchFavoriteUser(newText).observe(this@FavoriteActivity) {
                    getUser(it, userAdapter)
                }
                return true
            }
    })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun getUser(it: Result<List<UserEntity>>, userAdapter: UserAdapter){
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
                    this,
                    "Terjadi kesalahan" + it.error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}