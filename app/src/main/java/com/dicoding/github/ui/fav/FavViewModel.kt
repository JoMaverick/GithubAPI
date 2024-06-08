package com.dicoding.github.ui.fav

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.github.data.local.FavUser
import com.dicoding.github.data.local.FavUserDao
import com.dicoding.github.data.local.UserDatabase

class FavViewModel(application: Application) : AndroidViewModel(application) {

    private var userDao: FavUserDao?
    private var userDB : UserDatabase?

    init {
        userDB = UserDatabase.getDatabase(application)
        userDao = userDB?.favUserDao()
    }

    fun getFavUser(): LiveData<List<FavUser>>? {
        return userDao?.getFavUser()
    }
}