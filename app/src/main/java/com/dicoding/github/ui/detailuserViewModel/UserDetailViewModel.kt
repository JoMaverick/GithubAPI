package com.dicoding.github.ui.detailuserViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.github.data.local.FavUser
import com.dicoding.github.data.local.FavUserDao
import com.dicoding.github.data.local.UserDatabase
import com.dicoding.github.data.response.DetailUserResponse
import com.dicoding.githubapp.data.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DetailUserResponse>()

    private var userDao: FavUserDao?
    private var userDB : UserDatabase?

    init {
        userDB = UserDatabase.getDatabase(application)
        userDao = userDB?.favUserDao()
    }

    fun setDetailUser(username: String) {
        ApiConfig.getApiService().getDetailUser(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.d("onFailure:", t.message ?: "Unknown error")
                }

            })
    }

    fun getDetailUser(): LiveData<DetailUserResponse> {
        return user
    }

    fun addToFav(username: String, id: Int, avatarUrl: String){
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavUser(
                username,
                id,
                avatarUrl
            )
            userDao?.addToFavorite(user)
        }
    }
    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFav(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFav(id)
        }
    }
}