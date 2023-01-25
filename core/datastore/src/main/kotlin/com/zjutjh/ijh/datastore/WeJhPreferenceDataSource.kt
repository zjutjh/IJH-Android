package com.zjutjh.ijh.datastore

import androidx.datastore.core.DataStore
import com.zjutjh.ijh.datastore.converter.asLocalModel
import com.zjutjh.ijh.datastore.model.WeJhPreference
import com.zjutjh.ijh.datastore.model.copy
import com.zjutjh.ijh.model.WeJhUser
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime
import javax.inject.Inject

class WeJhPreferenceDataSource @Inject constructor(private val dataStore: DataStore<WeJhPreference>) {

    val data: Flow<WeJhPreference> = dataStore.data

    suspend fun setUser(user: WeJhUser) {
        dataStore.updateData {
            it.copy {
                this.user = user.asLocalModel()
            }
        }
    }

    suspend fun setUser(user: WeJhPreference.User) {
        dataStore.updateData {
            it.copy { this.user = user }
        }
    }

    suspend fun updateUser(transform: suspend (WeJhPreference.User) -> WeJhPreference.User) =
        dataStore.updateData {
            it.copy { this.user = transform(it.user) }
        }


    suspend fun deleteUser() {
        dataStore.updateData {
            it.copy {
                clearUser()
            }
        }
    }

    suspend fun setCoursesLastSyncTime(time: ZonedDateTime) {
        dataStore.updateData {
            it.copy {
                coursesLastSyncTime = time.toEpochSecond()
            }
        }
    }

    suspend fun deleteCoursesLastSyncTime() =
        dataStore.updateData {
            it.copy {
                clearCoursesLastSyncTime()
            }
        }
}