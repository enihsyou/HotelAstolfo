package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.BookTransaction
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.json.GsonJsonParser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import javax.validation.Payload

@RestController("订单接口控制器")
@RequestMapping("/api/books")
class BookController {

    @Autowired lateinit var roomService: RoomService

    @GetMapping("/list")
    fun listRoomByDate(@RequestParam("from", required = false) from: LocalDateTime,
                       @RequestParam("to", required = false) to: LocalDateTime,
                       pageable: Pageable): Page<BookTransaction> {
    }

    @PostMapping("/makeBook")
    fun makeBook() {
    }
}

@Service
interface RoomService {

}
