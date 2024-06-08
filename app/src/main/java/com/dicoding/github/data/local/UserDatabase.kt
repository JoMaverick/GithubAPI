package com.dicoding.github.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavUser::class],
    version = 1
)

abstract class UserDatabase : RoomDatabase() {
    companion object{
        var Instance : UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase?{
            if(Instance == null){
                synchronized(UserDatabase::class){
                    Instance = Room.databaseBuilder(context.applicationContext,
                        UserDatabase::class.java, "user_database").build()
                }
            }
            return Instance
        }
    }
    abstract fun favUserDao(): FavUserDao
}