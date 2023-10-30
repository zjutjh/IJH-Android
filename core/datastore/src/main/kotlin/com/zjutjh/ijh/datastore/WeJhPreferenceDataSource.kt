package com.zjutjh.ijh.datastore

import androidx.datastore.core.DataStore
import com.zjutjh.ijh.datastore.converter.asLocalModel
import com.zjutjh.ijh.datastore.model.LocalSession
import com.zjutjh.ijh.datastore.model.WeJhPreference
import com.zjutjh.ijh.datastore.model.WeJhPreferenceKt
import com.zjutjh.ijh.datastore.model.copy
import com.zjutjh.ijh.model.CampusInfo
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

    private suspend fun setInfo(info: WeJhPreference.Info) =
        dataStore.updateData {
            it.copy {
                this.info = info
            }
        }

    suspend fun setInfo(info: CampusInfo) =
        setInfo(info.asLocalModel())

    suspend fun deleteInfo() =
        dataStore.updateData {
            it.copy {
                clearInfo()
            }
        }

    suspend fun setSession(session: LocalSession) =
        dataStore.updateData {
            it.copy {
                this.session = session
            }
        }

    suspend fun deleteSession() =
        dataStore.updateData {
            it.copy {
                clearSession()
            }
        }

    private suspend fun setCard(card: WeJhPreference.Card) =
        dataStore.updateData {
            it.copy {
                this.card = card
            }
        }

    suspend fun setCard(balance: String, syncTime: ZonedDateTime) =
        setCard(WeJhPreferenceKt.card {
            this.balance = balance
            lastSyncTime = syncTime.toEpochSecond()
        })

    suspend fun deleteCard() =
        dataStore.updateData {
            it.copy {
                clearCard()
            }
        }

}