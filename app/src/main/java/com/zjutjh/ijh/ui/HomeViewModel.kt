package com.zjutjh.ijh.ui

import androidx.lifecycle.ViewModel
import com.zjutjh.ijh.model.Course
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class HomeViewModel : ViewModel() {
    val courses: ImmutableList<Course> = persistentListOf()
}