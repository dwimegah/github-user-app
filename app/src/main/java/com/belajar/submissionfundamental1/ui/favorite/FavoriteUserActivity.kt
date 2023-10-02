package com.belajar.submissionfundamental1.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.belajar.submissionfundamental1.databinding.ActivityFavoriteUserBinding
import com.belajar.submissionfundamental1.ui.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.show()
        supportActionBar?.title = "Favorite Users"

        val layoutManager = LinearLayoutManager(this)
        binding.recycleViewFav.layoutManager = layoutManager

        favoriteUserViewModel = obtainFavoriteUserViewModel(this@FavoriteUserActivity)
        favoriteUserViewModel.getAllFavoriteUsers().observe(this) {
            val adapter = FavoriteUserAdapter()
            adapter.submitList(it)
            binding.recycleViewFav.adapter = adapter
        }
    }

    private fun obtainFavoriteUserViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(
            this@FavoriteUserActivity,
            factory
        )[FavoriteUserViewModel::class.java]
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