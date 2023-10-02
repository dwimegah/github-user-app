package com.belajar.submissionfundamental1.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.belajar.submissionfundamental1.R
import com.belajar.submissionfundamental1.data.response.ItemsItem
import com.belajar.submissionfundamental1.databinding.ActivityMainBinding
import com.belajar.submissionfundamental1.ui.favorite.FavoriteUserActivity
import com.belajar.submissionfundamental1.ui.setting.SettingActivity
import com.belajar.submissionfundamental1.ui.setting.SettingPreferences
import com.belajar.submissionfundamental1.ui.setting.SettingViewModel
import com.belajar.submissionfundamental1.ui.setting.SettingViewModelFactory
import com.belajar.submissionfundamental1.ui.setting.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User's Search"
        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        with(binding) {
            search.setupWithSearchBar(searchBar)
            searchBar.inflateMenu(R.menu.option_menu)
            search
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = search.text
                    search.hide()
                    mainViewModel.searchUser(search.text.toString())
                    false
                }
            searchBar.setOnMenuItemClickListener{menuItem ->
                when(menuItem.itemId) {
                    R.id.menu1 -> {
                        val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.menu2 -> {
                        val intent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
        }

        mainViewModel.listUser.observe(this) { user ->
            setUserData(user)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.recycleView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recycleView.addItemDecoration(itemDecoration)

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setUserData(user: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(user)
        binding.recycleView.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}