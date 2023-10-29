package com.zjutjh.ijh.data

import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.model.WeJhInfo
import kotlinx.coroutines.flow.Flow

interface WeJhInfoRepository {
    val infoStream: Flow<WeJhInfo?>

    /**
     * Sync WeJhInfo from network to local
     * @return Pair(year, term)
     */
    suspend fun sync(): Pair<Int, Term>
}