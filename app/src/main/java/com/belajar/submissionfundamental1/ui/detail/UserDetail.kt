package com.belajar.submissionfundamental1.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.belajar.submissionfundamental1.R
import com.belajar.submissionfundamental1.ui.SectionsPagerAdapter
import com.belajar.submissionfundamental1.databinding.UserDetailBinding
import com.belajar.submissionfundamental1.ui.ViewModelFactory
import com.belajar.submissionfundamental1.data.database.FavoriteUser
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetail : AppCompatActivity() {
    private lateinit var binding: UserDetailBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        supportActionBar?.title = "User's Detail"

        val avatar = intent.getStringExtra("avatar")
        val username = intent.getStringExtra("username")

        val userViewModel = obtainUserViewModel(this@UserDetail)
        if (username != null) {
            userViewModel.detailUser(username)
        }

        userViewModel.user.observe(this) { user ->
            binding.detailUsername.text = "${user.username}"
            binding.detailName.text = "${user.name}"
            binding.followers.text = "${user.followers} followers"
            binding.following.text = "${user.following} following"
            binding.fab.contentDescription = "${user.isFavorite.toString()}"
            Glide.with(this)
                .load("${user.avatarUrl}")
                .into(binding.detailAvatar)

            binding.apply {
                if (!user.isFavorite) {
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@UserDetail, R.drawable.favorite_border
                        )
                    )
                } else {
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@UserDetail, R.drawable.favorite_filled
                        )
                    )
                }
            }
        }

        userViewModel.isLoadingDetail.observe(this) {
            showLoading(it)
        }

        binding.apply {
            fab.setOnClickListener {
                val userFavorite = FavoriteUser(
                    name = detailName.text.toString(),
                    username = detailUsername.text.toString(),
                    avatarUrl = avatar.toString(),
                    isFavorite = true,
                    followers = followers.text.toString(),
                    following = following.text.toString()
                )

                val currentIcon = fab.contentDescription
                if (currentIcon.equals("true")) {
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@UserDetail, R.drawable.favorite_border
                        )
                    )
                    userViewModel.deleteUserFavorite(userFavorite)
                    fab.contentDescription = "false"
                } else {
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@UserDetail, R.drawable.favorite_filled
                        )
                    )
                    userViewModel.insertUserFavorite(userFavorite)
                    fab.contentDescription = "true"
                }
            }
        }
    }

    private fun obtainUserViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(this@UserDetail, factory)[UserViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarDetail.visibility = View.VISIBLE
        } else {
            binding.progressBarDetail.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}