package com.enihsyou.astolfo.hotel.domain

import java.io.Serializable
import java.sql.Timestamp

data class User(val phoneNumber: Number, var username: String?, val password: String, val register_date: Timestamp) : Serializable
