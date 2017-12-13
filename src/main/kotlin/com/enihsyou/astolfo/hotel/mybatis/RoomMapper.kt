package com.enihsyou.astolfo.hotel.mybatis

import java.math.BigInteger

interface RoomMapper {

  fun getRoomById(room_id: BigInteger)
}
