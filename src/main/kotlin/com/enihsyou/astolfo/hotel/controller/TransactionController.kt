package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.repository.RoomDirectionRepository
import com.enihsyou.astolfo.hotel.repository.RoomTypeRepository
import com.enihsyou.astolfo.hotel.service.RoomService
import com.enihsyou.astolfo.hotel.service.TransactionService
import com.enihsyou.astolfo.hotel.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@CrossOrigin
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
        @RequestParam("phone", required = false) userPhone: String? = null,
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
        var phone: String = "",
        var guests: List<String> = emptyList(),
        var room: Room.RoomNumber = Room.RoomNumber(),
        var from: LocalDateTime = LocalDateTime.now(),
        var to: LocalDateTime = LocalDateTime.now()
    )

    @PostMapping("/make")
    fun singleBook(@RequestBody body: BookBody) {
        transactionService.singleBook(body)
    }

    @PutMapping
    fun updateInformation(@RequestParam phone: String, @RequestBody user: User): User
        =User()// transactionService.modifyRoom(phone, user)
}

