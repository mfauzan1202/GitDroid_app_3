package com.fauzan.submission3.di

import android.content.Context
import com.fauzan.submission3.data.UserRepository
import com.fauzan.submission3.data.local.room.UserDatabase
import com.fauzan.submission3.data.remote.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(apiService, dao)
    }
}