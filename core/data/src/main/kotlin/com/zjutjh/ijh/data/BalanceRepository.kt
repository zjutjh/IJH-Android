package com.zjutjh.ijh.data

interface BalanceRepository {

    /**
     * Get balance directly from network, because the non-realtime data is useless
     *
     * @return balance (Unit: CNY) in string
     */
    suspend fun getBalance(): String
}