package com.zjutjh.ijh.network.model

data class NetworkWeJhUser(
    val id: Long = 0,
    val username: String = String(),
    val studentID: String = String(),
    val createTime: String = String(),
    val phoneNum: String = String(),
    val userType: Int = 0,
    val bind: Bind = Bind(),
) {
    data class Bind(
        val zf: Boolean = false,
        val lib: Boolean = false,
        val yxy: Boolean = false,
    )
}
