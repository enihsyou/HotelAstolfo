package com.enihsyou.astolfo.hotel.domain

import java.io.Serializable

data class User(val id:Long,val email: String, val username: String, val password: String) : Serializable
