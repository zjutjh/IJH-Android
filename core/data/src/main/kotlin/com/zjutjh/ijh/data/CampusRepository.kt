package com.zjutjh.ijh.data

import com.zjutjh.ijh.model.CampusInfo
import com.zjutjh.ijh.model.Term
import kotlinx.coroutines.flow.Flow

interface CampusRepository {
    val infoStream: Flow<CampusInfo?>

    /**
     * Sync campus info from network to local
     * @return Pair(year, term)
     */
    suspend fun sync(): Pair<Int, Term>
}