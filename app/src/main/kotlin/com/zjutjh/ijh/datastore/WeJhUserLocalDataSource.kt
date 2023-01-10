package com.zjutjh.ijh.datastore

import androidx.datastore.core.DataStore
import com.zjutjh.ijh.datastore.converter.asInternalModel
import com.zjutjh.ijh.datastore.model.LocalWeJhUser
import com.zjutjh.ijh.model.WeJhUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeJhUserLocalDataSource @Inject constructor(private val dataStore: DataStore<LocalWeJhUser>) {
    val data: Flow<LocalWeJhUser> = dataStore.data

    suspend fun set(user: WeJhUser) {
        dataStore.updateData {
            user.asInternalModel()
        }
    }
}