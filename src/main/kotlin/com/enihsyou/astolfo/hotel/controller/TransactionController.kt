package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.repository.RoomDirectionRepository
import com.enihsyou.astolfo.hotel.repository.RoomTypeRepository
import com.enihsyou.astolfo.hotel.service.RoomService
import com.enihsyou.astolfo.hotel.service.TransactionService
import com.enihsyou.astolfo.hotel.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
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
        @RequestParam("phone", required = false) user_phone: String? = null,
        @RequestParam("createFrom", required = false) createFrom: LocalDateTime? = null,
        @RequestParam("createTo", required = false) createTo: LocalDateTime? = null,
        @RequestParam("validFrom", required = false) validFrom: LocalDateTime? = null,
        @RequestParam("validTo", required = false) validTo: LocalDateTime? = null,
        @RequestParam("type", required = false) type: String? = null,
        @RequestParam("direction", required = false) direction: String? = null,
        @RequestParam("priceFrom", required = false) priceFrom: Int? = null,
        @RequestParam("priceTo", required = false) priceTo: Int? = null,
        @RequestParam("floor", required = false) floor: Int? = null,
        @RequestParam("number", required = false) number: Int? = null
    ): List<Transaction>
        = transactionService.listTransactions(user_phone, createFrom, createTo, validFrom, validTo, type, direction, priceFrom, priceTo, floor, number)

    class BookBody(
        var phone: String = "",
        var guests: List<String> = emptyList(),
        var room: Room.RoomNumber = Room.RoomNumber(),
        var dateFrom: LocalDateTime = LocalDateTime.now(),
        var dateTo: LocalDateTime = LocalDateTime.now()
    )

    @PostMapping
    fun singleBook(@RequestBody body: BookBody)
        = transactionService.singleBook(body)


    @PatchMapping
    fun modifyBook(@RequestParam bookId: Int, @RequestBody payload: Map<String, String>): Transaction
        = transactionService.modifyRoom(bookId, payload)

    @PostMapping("/checkout")
    fun checkOut(@RequestParam bookId: Int)
        = transactionService.checkOut(bookId)
}

