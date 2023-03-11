package com.fauzan.submission3.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fauzan.submission3.data.UserRepository
import com.fauzan.submission3.data.local.entity.UserEntity
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun setUser(query: String) = userRepository.setUser(query)

//    fun setFollowerUser(query: String) = userRepository.setFollowerUser(query)

    fun setFavoriteUser() = userRepository.setFavoriteUser()
    fun searchFavoriteUser(query: String) = userRepository.searchFavoriteUser(query)
//    fun getLovedUser() = userRepository.getLovedUser()

    fun saveUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setLovedUser(user, true)
        }
    }
    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            userRepository.setLovedUser(user, false)
        }
    }
}