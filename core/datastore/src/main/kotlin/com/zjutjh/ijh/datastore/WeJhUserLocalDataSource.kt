package com.zjutjh.ijh.datastore

import androidx.datastore.core.DataStore
import com.zjutjh.ijh.datastore.converter.asLocalModel
import com.zjutjh.ijh.datastore.model.LocalWeJhUser
import com.zjutjh.ijh.model.WeJhUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeJhUserLocalDataSource @Inject constructor(private val dataStore: DataStore<LocalWeJhUser>) {
    val user: Flow<LocalWeJhUser> = dataStore.data

    suspend fun set(user: WeJhUser) {
        dataStore.updateData {
            user.asLocalModel()
        }
    }

    suspend fun set(user: LocalWeJhUser) {
        dataStore.updateData {
            user
        }
    }

    suspend fun update(transform: suspend (LocalWeJhUser) -> LocalWeJhUser) =
        dataStore.updateData(transform)


    suspend fun delete() {
        dataStore.updateData {
            it.defaultInstanceForType
        }
    }
}