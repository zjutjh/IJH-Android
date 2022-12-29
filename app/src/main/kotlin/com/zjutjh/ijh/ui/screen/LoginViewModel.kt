package com.zjutjh.ijh.ui.screen

import androidx.lifecycle.ViewModel
import com.zjutjh.ijh.data.WeJHUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: WeJHUserRepository) :
    ViewModel()