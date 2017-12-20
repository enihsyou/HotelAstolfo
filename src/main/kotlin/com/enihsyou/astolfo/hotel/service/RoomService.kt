package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Room
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

interface RoomService {
    fun listRoomByParameter(
        from: LocalDateTime,
        to: LocalDateTime,
        type: Room.RoomType,
        direction: Room.RoomDirection,
        priceFrom: Int,
        priceTo: Int,
        pageable: Pageable
    ):List<Room>

    fun listRoomByDateBetween(
        from: LocalDateTime,
        to: LocalDateTime,
        pageable: Pageable
    ): List<Room>

    fun listRoomByPriceBetween(
        priceFrom: Int,
        priceTo: Int,
        pageable: Pageable
    ): List<Room>

    fun listRoomByDirection(
        direction: Room.RoomDirection,
        pageable: Pageable
    ): List<Room>

    fun listRoomByType(
        type: Room.RoomType,
        pageable: Pageable
    ): List<Room>
}
//
//@Service
//class RoomServiceImpl : RoomService {
//
//}
