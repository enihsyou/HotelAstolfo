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


@RestController("用户接口控制器")
@RequestMapping("/api/rooms")
class RoomController(@Autowired val roomService: RoomService) {

    @GetMapping("/list")
    fun listRoom(
        @RequestParam("from", required = false) from: LocalDateTime = LocalDateTime.now(),
        @RequestParam("to", required = false) to: LocalDateTime = LocalDateTime.MAX,
        @RequestParam("type", required = false) type: Room.RoomType = Room.RoomType.Any,
        @RequestParam("direction", required = false) direction: Room.RoomDirection = Room.RoomDirection.Any,
        @RequestParam("priceFrom", required = false) priceFrom: Int = 0,
        @RequestParam("priceTo", required = false) priceTo: Int = Int.MAX_VALUE,
        pageable: Pageable) =
        roomService.listRoomByParameter(from, to, type, direction, priceFrom, priceTo, pageable)
}

