package com.dicoding.github.ui.detailuserViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.github.data.response.ItemsItem
import com.dicoding.githubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    val listFollowers = MutableLiveData<ArrayList<ItemsItem>>()

    fun setFollowersList (username: String){
        ApiConfig.getApiService().getFollowers(username)
            .enqueue(object : Callback<ArrayList<ItemsItem>>{
                override fun onResponse(
                    call: Call<ArrayList<ItemsItem>>,
                    response: Response<ArrayList<ItemsItem>>
                ) {
                    if(response.isSuccessful){
                        listFollowers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                    Log.d("onFailure:", t.message ?: "Unknown error")
                }

            })
    }

    fun getFollowersList(): LiveData<ArrayList<ItemsItem>>{
        return listFollowers
    }
}