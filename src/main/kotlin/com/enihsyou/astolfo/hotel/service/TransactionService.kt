package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.repository.GuestRepository
import com.enihsyou.astolfo.hotel.repository.RoomRepository
import com.enihsyou.astolfo.hotel.repository.TransactionRepository
import com.enihsyou.astolfo.hotel.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface TransactionService {
    fun listTransactionByDateBetween(
        from: LocalDateTime,
        to: LocalDateTime,
        pageable: Pageable
    ): List<Transaction>

    fun listTransactionByType(
        type: String,
        pageable: Pageable
    ): List<Transaction>

    fun listTransactionByDirection(
        direction: String,
        pageable: Pageable
    ): List<Transaction>

    fun listTransactionByPriceBetween(
        priceFrom: Int,
        priceTo: Int,
        pageable: Pageable
    ): List<Transaction>
}

@Service(value = "订单层逻辑")
class TransactionServiceImpl : TransactionService {

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var guestRepository: GuestRepository
    @Autowired lateinit var roomRepository: RoomRepository
    @Autowired lateinit var transactionRepository:TransactionRepository
    override fun listTransactionByDateBetween(from: LocalDateTime,
                                              to: LocalDateTime,
                                              pageable: Pageable): List<Transaction> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listTransactionByType(type: String,
                                       pageable: Pageable): List<Transaction> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listTransactionByDirection(direction: String,
                                            pageable: Pageable): List<Transaction> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listTransactionByPriceBetween(priceFrom: Int,
                                               priceTo: Int,
                                               pageable: Pageable): List<Transaction> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
