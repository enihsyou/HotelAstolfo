package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.service.RoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime


@RestController("房间接口控制器")
@RequestMapping("/api/rooms")
class RoomController {

    @Autowired lateinit var roomService: RoomService

    @GetMapping("/list")
    fun listRoom(
        @RequestParam("from", required = false) from: LocalDateTime = LocalDateTime.now(),
        @RequestParam("to", required = false) to: LocalDateTime = LocalDateTime.MAX,
        @RequestParam("type", required = false) type:String="",
        @RequestParam("direction", required = false) direction: String="",
        @RequestParam("priceFrom", required = false) priceFrom: Int = Int.MIN_VALUE,
        @RequestParam("priceTo", required = false) priceTo: Int = Int.MAX_VALUE,
        pageable: Pageable) =
        roomService.listRoomByParameter(from, to, type, direction, priceFrom, priceTo, pageable)
}

