package com.zjutjh.ijh.datastore

import androidx.datastore.core.DataStore
import com.zjutjh.ijh.datastore.model.LocalWeJhUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeJhUserLocalDataSource @Inject constructor(dataStore: DataStore<LocalWeJhUser>) {
    val data: Flow<LocalWeJhUser> = dataStore.data

}