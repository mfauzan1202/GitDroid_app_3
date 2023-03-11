package com.fauzan.submission3.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fauzan.submission3.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user where login LIKE '%'||:login||'%' ORDER BY login ASC")
    fun getUser(login: String): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user where loved = 1")
    fun getLovedUser(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user where login LIKE '%'||:login||'%' AND loved = 1")
    fun searchLovedUser(login: String): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM user WHERE loved = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM user WHERE login = :login AND loved = 1)")
    suspend fun isUserLoved(login: String): Boolean
}