package com.fauzan.submission3.ui.themePreference

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ThemeModelFactory constructor(private val pref: SettingPreference) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}