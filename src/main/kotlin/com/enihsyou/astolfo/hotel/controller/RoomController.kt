package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.service.RoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime


@RestController("用户接口控制器")
@RequestMapping("/api/rooms")
class RoomController {

    @Autowired lateinit var roomService: RoomService

    @GetMapping("/list")
    fun listRoomByDate(@RequestParam("from", required = false) from: LocalDateTime,
                       @RequestParam("to", required = false) to: LocalDateTime) {
    }
}

