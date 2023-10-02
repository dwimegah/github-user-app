package com.belajar.submissionfundamental1.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belajar.submissionfundamental1.data.response.DetailUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.belajar.submissionfundamental1.data.response.ItemsItem
import com.belajar.submissionfundamental1.data.retrofit.ApiConfig
import com.belajar.submissionfundamental1.data.database.FavoriteUser
import com.belajar.submissionfundamental1.data.database.FavoriteUserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application): ViewModel() {
    private val favoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    private val _user = MutableLiveData<FavoriteUser>()
    val user: LiveData<FavoriteUser> = _user

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    private val _isLoadingDetail = MutableLiveData<Boolean>()
    val isLoadingDetail: LiveData<Boolean> = _isLoadingDetail

    private val _isLoadingFollowers = MutableLiveData<Boolean>()
    val isLoadingFollowers: LiveData<Boolean> = _isLoadingFollowers

    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing

    companion object{
        private const val TAG = "UserViewModel"
    }

    fun insertUserFavorite(favoriteUser: FavoriteUser) {
        favoriteUserRepository.insert(favoriteUser)
    }

    fun deleteUserFavorite(favoriteUser: FavoriteUser) {
        favoriteUserRepository.delete(favoriteUser)
    }

    fun detailUser(username: String) {
        _isLoadingDetail.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoadingDetail.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        viewModelScope.launch {
                            val isFavoriteUser =
                                favoriteUserRepository.isFavorite(responseBody.login)
                            val currentUser = FavoriteUser(
                                username = responseBody.login,
                                name = responseBody.name,
                                avatarUrl = responseBody.avatarUrl,
                                followers = responseBody.followers.toString(),
                                following = responseBody.following.toString(),
                                isFavorite = isFavoriteUser
                            )
                            _user.postValue(currentUser)
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoadingDetail.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowers(username: String) {
        _isLoadingFollowers.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoadingFollowers.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _followers.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollowers.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoadingFollowing.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _following.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}