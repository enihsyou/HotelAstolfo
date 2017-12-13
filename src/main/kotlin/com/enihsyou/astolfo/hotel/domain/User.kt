package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.sql.Timestamp
import javax.validation.Payload


enum class UserRole {
  管理员, 前台, 注册用户
}


/*用户类*/
data class User(
    val phone_number: Long,
    var nickname: String?,
    val password: String,
    val register_date: Timestamp,

    /*标记用户类型*/
    val user_role: String
) : Serializable {
  fun user_role(): UserRole = UserRole.valueOf(user_role)
}
