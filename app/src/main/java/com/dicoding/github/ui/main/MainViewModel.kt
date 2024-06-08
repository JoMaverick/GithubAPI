package com.dicoding.github.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.github.data.response.GithubResponse
import com.dicoding.github.data.response.ItemsItem
import com.dicoding.githubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<ItemsItem>>(

    )

    fun setUsers(query: String){
        ApiConfig.getApiService().getGithub(query)
            .enqueue(object : Callback<GithubResponse>{
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    if (response.isSuccessful){
                        listUsers.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    Log.d("onFailure:", t.message ?: "Unknown error")
                }

            })
    }

    fun getUsers(): LiveData<ArrayList<ItemsItem>>{
        return listUsers
    }
}