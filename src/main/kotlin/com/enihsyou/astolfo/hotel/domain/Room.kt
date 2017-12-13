package com.enihsyou.astolfo.hotel.domain

import java.math.BigInteger

enum class RoomType {
  大床房, 单人间, 双人间, 总统套房
}

enum class RoomDirection {
  东, 西, 南, 北
}

data class Room(
    val id: BigInteger,
    val type: RoomType,
    val floor: Int,
    val direction: RoomDirection,
    val specialty: String,
    var occupied:Boolean,
    var booked:Boolean
)
