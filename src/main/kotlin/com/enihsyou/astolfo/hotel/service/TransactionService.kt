package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.controller.TransactionController
import com.enihsyou.astolfo.hotel.domain.Guest
import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.exception.房间已损坏
import com.enihsyou.astolfo.hotel.exception.房间已被占用
import com.enihsyou.astolfo.hotel.exception.订单不存在
import com.enihsyou.astolfo.hotel.exception.订单时间冲突
import com.enihsyou.astolfo.hotel.repository.GuestRepository
import com.enihsyou.astolfo.hotel.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

interface TransactionService {
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
    fun modifyRoom(
        bookId: Int,
        payload: Map<String, String>
    ): Transaction

    fun checkOut(bookId: Int): Long
}

@Service(value = "订单层逻辑")
class TransactionServiceImpl : TransactionService {

    override fun checkOut(bookId: Int): Long {
        if (!transactionRepository.exists(bookId)) throw 订单不存在(bookId)
        val transaction = transactionRepository.findOne(bookId)
        val days = ChronoUnit.DAYS.between(transaction.dateFrom, transaction.dateTo)
        val prices = days * transaction.room.price
        transaction.activated = false
        transactionRepository.save(transaction)
        return prices
    }

    override fun modifyRoom(
        bookId: Int,
        payload: Map<String, String>
    ): Transaction {
        if (!transactionRepository.exists(bookId)) throw 订单不存在(bookId)
        val transaction = transactionRepository.findOne(bookId)
        for ((key, value) in payload)
            when (key) {
                "dateFrom"  ->
                    transaction.dateFrom = LocalDateTime.parse(value)

                "dateTo"    ->
                    transaction.dateFrom = LocalDateTime.parse(value)

                "room"      ->
                    transaction.room = value.also { println(it) }.let { roomService.getRoom(1, 1) }

                "activated" ->
                    transaction.activated = value.toBoolean()

                "used"      ->
                    transaction.used = value.toBoolean()

                "guests"    ->
                    transaction.guests = value.also { println(it) }.split(",").mapNotNull { guestRepository.findByIdentification(it) }.toMutableList()
            }

        transactionRepository.save(transaction)
        return transaction
    }

    override fun singleBook(body: TransactionController.BookBody): ResponseEntity<Unit> {
        val user = userService.getUser(body.phone)
        val room = roomService.getRoom(body.room.floor, body.room.number)
        val guests = mutableListOf<Guest>()
        body.guests.forEach {
            val guest = guestRepository.findByIdentification(it)
            if (guest == null) { // 如果仓库里没找到，也就是没有添加绑定过
                val g = Guest(identification = it, name = "代理用户")
                guestRepository.save(g)
                guests.add(g)
            } else {
                guests.add(guest)
            }
        }
        if (transactionRepository.findByUser(user)
            .filter { it.activated }
            .any { it.room.roomNumber == room.roomNumber })
            throw 订单时间冲突(body.dateFrom, body.dateTo)
        if (room.occupied(body.dateFrom, body.dateTo))
            throw 房间已被占用(room.roomNumber.floor, room.roomNumber.number)
        if (room.broken)
            throw 房间已损坏(room.roomNumber.floor, room.roomNumber.number)
//        val tranList = transactionRepository.findByUser(user)
//        if (tranList.any { body.dateFrom <= it.dateFrom && it.dateTo <= body.dateTo })
//            throw 订单时间冲突(body.dateFrom, body.dateTo)
        val transaction = Transaction(dateFrom = body.dateFrom, dateTo = body.dateTo, user = user, room = room, guests = guests, activated = true)
        room.transactions.add(transaction)
        transactionRepository.save(transaction)
        guests.forEach { it.transactions.add(transaction) }
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
            result = result.filter { it in transactionRepository.findByCreatedDateBetween(createFrom, createTo) }
        }
        if (validFrom != null && validTo != null) {
            result = result.filter { it in transactionRepository.findByValidBetween(validFrom, validTo) }
        }

        return result.toList()
    }

    @Autowired lateinit var userService: UserService
    @Autowired lateinit var guestRepository: GuestRepository
    @Autowired lateinit var roomService: RoomService
    @Autowired lateinit var transactionRepository: TransactionRepository
}
