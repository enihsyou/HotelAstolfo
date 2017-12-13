package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.sql.Timestamp
import javax.validation.Payload


data class User(val phoneNumber: Number, var username: String?, val password: String, val register_date: Timestamp) : Serializable

data class LoginPayload(
    @JsonProperty("phone_number") val phoneNumber: String = "",
    @JsonProperty("password") val password: String = ""
) : Payload
