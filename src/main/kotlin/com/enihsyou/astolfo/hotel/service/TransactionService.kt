package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.repository.GuestRepository
import com.enihsyou.astolfo.hotel.repository.RoomRepository
import com.enihsyou.astolfo.hotel.repository.TransactionRepository
import com.enihsyou.astolfo.hotel.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface TransactionService {
    fun addTransactions(
        transaction: Transaction
    )

    fun listByParameter(user_phone: String?,
                        createFrom: LocalDateTime?,
                        createTo: LocalDateTime?,
                        validFrom: LocalDateTime?,
                        validTo: LocalDateTime?,
                        type: String?,
                        direction: String?,
                        priceFrom: Int?,
                        priceTo: Int?,
                        floor: Int?,
                        number: Int?): List<Transaction>
}

@Service(value = "订单层逻辑")
class TransactionServiceImpl : TransactionService {

    override fun listByParameter(
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
        number: Int?): List<Transaction> {

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

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var guestRepository: GuestRepository
    @Autowired lateinit var roomRepository: RoomRepository
    @Autowired lateinit var transactionRepository: TransactionRepository
}
