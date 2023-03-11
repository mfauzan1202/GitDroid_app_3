package com.fauzan.submission3.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.fauzan.submission3.data.local.room.UserDao
import com.fauzan.submission3.data.local.entity.UserEntity
import com.fauzan.submission3.data.remote.api.ApiService

class UserRepository(private val apiService: ApiService, private val userDao: UserDao) : ViewModel(){

    fun setUser(query: String): LiveData<Result<List<UserEntity>>> = liveData{
        emit(Result.Loading)
        try {
            userDao.deleteAll()
            val response = apiService.getUser(query)
            val users = response.items
            val userList = users.map { user ->
                val isLoved = userDao.isUserLoved(user.login)
                UserEntity(
                    user.login,
                    user.avatarUrl,
                    isLoved
                )
            }
            userDao.insertUser(userList)
        } catch (e: Exception) {
            Log.d("UserRepository", "getHeadlineNews: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UserEntity>>> =
            userDao.getUser(query).map { Result.Success(it) }
        emitSource(localData)
    }


    fun setFavoriteUser(): LiveData<Result<List<UserEntity>>> = liveData{
        val localData: LiveData<Result<List<UserEntity>>> =
            userDao.getLovedUser().map { Result.Success(it) }
        emitSource(localData)
    }

    fun searchFavoriteUser(query: String): LiveData<Result<List<UserEntity>>> = liveData{
        val localData: LiveData<Result<List<UserEntity>>> =
            userDao.searchLovedUser(query).map { Result.Success(it) }
        emitSource(localData)
    }

    suspend fun setLovedUser(user: UserEntity, bookmarkState: Boolean) {
        user.isLoved = bookmarkState
        userDao.updateUser(user)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: UserDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, newsDao)
            }.also { instance = it }
    }
}