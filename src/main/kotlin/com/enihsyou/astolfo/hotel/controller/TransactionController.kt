package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.repository.RoomDirectionRepository
import com.enihsyou.astolfo.hotel.repository.RoomTypeRepository
import com.enihsyou.astolfo.hotel.service.RoomService
import com.enihsyou.astolfo.hotel.service.TransactionService
import com.enihsyou.astolfo.hotel.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime


@RestController("订单接口控制器")
@RequestMapping("/api/transactions")
class TransactionController {

    @Autowired lateinit var userService: UserService

    @Autowired lateinit var transactionService: TransactionService
    @Autowired lateinit var roomService: RoomService
    @Autowired lateinit var roomTypeRepository: RoomTypeRepository
    @Autowired lateinit var roomDirectionRepository: RoomDirectionRepository
    @GetMapping("/list")
    fun listByDate(
        @RequestParam("userPhone", required = false) userPhone: String? = null,
        @RequestParam("createFrom", required = false) createFrom: LocalDateTime? = null,
        @RequestParam("createTo", required = false) createTo: LocalDateTime? = null,
        @RequestParam("validFrom", required = false) validFrom: LocalDateTime? = null,
        @RequestParam("validTo", required = false) validTo: LocalDateTime? = null,
        @RequestParam("type", required = false) type: String? = null,
        @RequestParam("direction", required = false) direction: String? = null,
        @RequestParam("priceFrom", required = false) priceFrom: Int? = null,
        @RequestParam("priceTo", required = false) priceTo: Int? = null,
        @RequestParam("floor", required = false) floor: Int? = null,
        @RequestParam("number", required = false) number: Int? = null): List<Transaction> {
        return transactionService.listByParameter(userPhone, createFrom, createTo, validFrom, validTo, type, direction, priceFrom, priceTo, floor, number)
    }

    class BookBody(
        var userPhone: String,
        var guests: List<String>,
        var room: Room.RoomNumber,
        var from: LocalDateTime,
        var to: LocalDateTime
    )

    @PostMapping("/make")
    fun singleBook(@RequestBody body: BookBody) {
        println(body)
    }

    @GetMapping("/dummy")
    fun dummy() {
        val user = userService.findByPhone("12345678888")!!
        val room = roomService.listRoomByParameter(priceFrom = 99).first()
        transactionService.addTransactions(Transaction(user = user, room = room, dateFrom = LocalDateTime.now(), dateTo = LocalDateTime.now().plusDays(1)))
    }
}

