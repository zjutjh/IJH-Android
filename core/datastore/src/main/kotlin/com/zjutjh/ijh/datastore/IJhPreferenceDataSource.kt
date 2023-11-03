package com.zjutjh.ijh.datastore

import androidx.datastore.core.DataStore
import com.zjutjh.ijh.datastore.converter.asLocalModel
import com.zjutjh.ijh.datastore.model.IJhPreference
import com.zjutjh.ijh.datastore.model.IJhPreferenceKt
import com.zjutjh.ijh.datastore.model.LocalSession
import com.zjutjh.ijh.datastore.model.LocalWeJhUser
import com.zjutjh.ijh.datastore.model.copy
import com.zjutjh.ijh.model.CampusInfo
import com.zjutjh.ijh.model.WeJhUser
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime
import javax.inject.Inject

class IJhPreferenceDataSource @Inject constructor(private val dataStore: DataStore<IJhPreference>) {

    val data: Flow<IJhPreference> = dataStore.data

    suspend fun setWeJhUser(user: WeJhUser) {
        dataStore.updateData {
            it.copy {
                this.weJhUser = user.asLocalModel()
            }
        }
    }

    suspend fun setWeJhUser(user: LocalWeJhUser) {
        dataStore.updateData {
            it.copy { this.weJhUser = user }
        }
    }

    suspend fun updateWeJhUser(transform: suspend (LocalWeJhUser) -> LocalWeJhUser) =
        dataStore.updateData {
            it.copy { this.weJhUser = transform(it.weJhUser) }
        }


    suspend fun deleteWeJhUser() {
        dataStore.updateData {
            it.copy {
                clearWeJhUser()
            }
        }
    }

    suspend fun setCoursesLastSyncTime(time: ZonedDateTime) {
        dataStore.updateData {
            it.copy {
                coursesSyncTime = time.toEpochSecond()
            }
        }
    }

    suspend fun deleteCoursesLastSyncTime() =
        dataStore.updateData {
            it.copy {
                clearCoursesSyncTime()
            }
        }

    private suspend fun setCampus(info: IJhPreference.Campus) =
        dataStore.updateData {
            it.copy {
                this.campus = info
            }
        }

    suspend fun setCampus(info: CampusInfo) =
        setCampus(info.asLocalModel())

    suspend fun deleteCampus() =
        dataStore.updateData {
            it.copy {
                clearCampus()
            }
        }

    suspend fun setSession(session: LocalSession) =
        dataStore.updateData {
            it.copy {
                this.weJhSession = session
            }
        }

    suspend fun deleteSession() =
        dataStore.updateData {
            it.copy {
                clearWeJhSession()
            }
        }

    private suspend fun setCard(card: IJhPreference.Card) =
        dataStore.updateData {
            it.copy {
                this.card = card
            }
        }

    suspend fun setCard(balance: String, syncTime: ZonedDateTime) =
        setCard(IJhPreferenceKt.card {
            this.balance = balance
            this.syncTime = syncTime.toEpochSecond()
        })

    suspend fun deleteCard() =
        dataStore.updateData {
            it.copy {
                clearCard()
            }
        }

}