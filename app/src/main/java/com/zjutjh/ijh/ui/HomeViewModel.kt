package com.zjutjh.ijh.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zjutjh.ijh.data.Course
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class HomeViewModel : ViewModel() {
    val courses: ImmutableList<Course> by mutableStateOf(persistentListOf())
}

