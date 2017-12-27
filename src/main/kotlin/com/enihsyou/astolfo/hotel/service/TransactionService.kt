package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.controller.TransactionController
import com.enihsyou.astolfo.hotel.domain.Guest
import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.repository.GuestRepository
import com.enihsyou.astolfo.hotel.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface TransactionService {
    fun addTransactions(
        transaction: Transaction
    )

    fun listTransactions(
        user_phone: String? = null,
        createFrom: LocalDateTime? = null,
        createTo: LocalDateTime? = null,
        validFrom: LocalDateTime? = null,
        validTo: LocalDateTime? = null,
        type: String? = null,
        direction: String? = null,
        priceFrom: Int? = null,
        priceTo: Int? = null,
        floor: Int? = null,
        number: Int?
    ): List<Transaction>

    fun singleBook(body: TransactionController.BookBody): ResponseEntity<Unit>
}

@Service(value = "订单层逻辑")
class TransactionServiceImpl : TransactionService {

    override fun singleBook(body: TransactionController.BookBody): ResponseEntity<Unit> {
        val user = userService.getUser(body.phone)
        val room = roomService.listRooms(floor = body.room.floor, number = body.room.number).first()
        val guests = mutableListOf<Guest>()
        body.guests.forEach { guestRepository.findByIdentification(it)?.let { guests.add(it) } }
        val transaction = Transaction(dateFrom = body.from, dateTo = body.to, user = user, room = room, guests = guests)
        transactionRepository.save(transaction)
        return ResponseEntity(HttpStatus.CREATED)
    }

    override fun listTransactions(
        user_phone: String?,
        createFrom: LocalDateTime?,
        createTo: LocalDateTime?,
        validFrom: LocalDateTime?,
        validTo: LocalDateTime?,
        type: String?,
        direction: String?,
        priceFrom: Int?,
        priceTo: Int?,
        floor: Int?,
        number: Int?
    ): List<Transaction> {
        val lists = listOf(listOf(1, 2, 3, 4, 5), listOf(6, 7, 8, 9, 10))
        for (list in lists) {
            for (i in list) {
                println(i)
            }
        }
        var result = transactionRepository.findAll()
        if (user_phone != null) {
            result = result.filter { it.user.phoneNumber == user_phone }
        }
        if (type != null) {
            result = result.filter { it.room.type.type == type }
        }
        if (direction != null) {
            result = result.filter { it.room.direction.type == direction }
        }
        if (floor != null) {
            result = result.filter { it.room.roomNumber.floor == floor }
        }
        if (number != null) {
            result = result.filter { it.room.roomNumber.number == number }
        }
        if (priceFrom != null) {
            result = result.filter { it.room.price >= priceFrom }
        }
        if (priceTo != null) {
            result = result.filter { it.room.price <= priceTo }
        }
        if (createFrom != null && createTo != null) {
            result = result.filter { it in transactionRepository.findByCreateDateBetween(createFrom, createTo) }
        }
        if (validFrom != null && validTo != null) {
            result = result.filter { it in transactionRepository.findByValidBetween(validFrom, validTo) }
        }

        return result.toList()
    }

    override fun addTransactions(transaction: Transaction) {
        transactionRepository.save(transaction)
    }

    @Autowired lateinit var userService: UserService
    @Autowired lateinit var guestRepository: GuestRepository
    @Autowired lateinit var roomService: RoomService
    @Autowired lateinit var transactionRepository: TransactionRepository
}
