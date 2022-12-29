package com.zjutjh.ijh.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class WeJHUser(
    @Id var id: Long = 0,
    var userID: Long = 0,
    var studentID: String = String(),
    var createTime: String = String(),
    var phoneNum: String = String(),
    var username: String = String(),
    var userType: Int = 0,
    var bindLib: Boolean = false,
    var bindYxy: Boolean = false,
    var bindZf: Boolean = false,
)