package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Room
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

interface RoomService {
    fun listRoomByDate(@RequestParam("from", required = false) from: LocalDateTime,
                       @RequestParam("to", required = false) to: LocalDateTime,
                       @RequestParam("type", required = false) type: Room.RoomType,
                       @RequestParam("direction", required = false) direction: Room.RoomDirection,
                       @RequestParam("priceFrom", required = false) priceFrom: Int,
                       @RequestParam("priceTo", required = false) priceTo: Int,
                       pageable: Pageable)
}

@Service
class RoomServiceImpl : RoomService {

    override fun listRoomByDate(@RequestParam(required = false, value = "from") from: LocalDateTime, @RequestParam(required = false, value = "to") to: LocalDateTime, @RequestParam(required = false, value = "type") type: Room.RoomType, @RequestParam(required = false, value = "direction") direction: Room.RoomDirection, @RequestParam(required = false, value = "priceFrom") priceFrom: Int, @RequestParam(required = false, value = "priceTo") priceTo: Int, pageable: Pageable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
