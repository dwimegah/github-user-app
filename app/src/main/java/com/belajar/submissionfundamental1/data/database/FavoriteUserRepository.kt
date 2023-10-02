package com.belajar.submissionfundamental1.data.database

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoritesUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoritesUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoritesUserDao.getAllFavoriteUsers()

    fun insert(favorite: FavoriteUser) {
        executorService.execute { mFavoritesUserDao.insert(favorite) }
    }

    fun delete(favorite: FavoriteUser) {
        executorService.execute { mFavoritesUserDao.delete(favorite) }
    }

    fun isFavorite(username: String): Boolean {
        return mFavoritesUserDao.isFavorite(username)
    }
}