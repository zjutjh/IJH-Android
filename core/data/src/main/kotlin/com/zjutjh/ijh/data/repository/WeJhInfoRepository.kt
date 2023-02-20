package com.zjutjh.ijh.data.repository

import com.zjutjh.ijh.model.Term
import com.zjutjh.ijh.model.WeJhInfo
import kotlinx.coroutines.flow.Flow

interface WeJhInfoRepository {
    val infoStream: Flow<WeJhInfo?>

    suspend fun sync(): Pair<Int, Term>
}