package com.fauzan.submission3.ui.detail

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.fauzan.submission3.R
import com.fauzan.submission3.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]

        viewModel.setDetail(username!!)
        viewModel.getDetailUser().observe(this) {
            if (it != null) {
                binding.apply {
                    tvDetailLogin.text =
                        this@DetailActivity.resources.getString(R.string.userplaceholder, it.login)
                    if (it.name != null) tvDetailName.text = it.name else tvDetailName.text =
                        getString(R.string.empty_value)
                    if (it.location != null) tvDetailLocation.text =
                        it.location else tvDetailLocation.text = getString(R.string.empty_value)
                    if (it.company != null) tvDetailCompany.text =
                        it.company else tvDetailCompany.text = getString(R.string.empty_value)
                    tvDetailFollowers.text = it.followers.toString()
                    tvDetailFollowing.text = it.following.toString()
                    tvDetailRepo.text = it.repos.toString()
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(imageView)
                }
            }
        }
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = SectionsPagerAdapter(this, bundle)
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }
}