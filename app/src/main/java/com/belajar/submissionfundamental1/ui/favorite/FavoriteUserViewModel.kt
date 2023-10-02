package com.belajar.submissionfundamental1.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModel
import com.belajar.submissionfundamental1.data.database.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavoriteUsers() = favoriteUserRepository.getAllFavoriteUsers()

    init {
        getAllFavoriteUsers()
    }
}