package com.zjutjh.ijh.network.service.response


data class UserResult(
    val user: User
) {
    data class User(
        val bind: UserBind,
        val createTime: String,
        val id: Long,
        val phoneNum: String,
        val studentID: String,
        val username: String,
        val userType: Int
    ) {
        data class UserBind(
            val lib: Boolean,
            val yxy: Boolean,
            val zf: Boolean
        )
    }
}

