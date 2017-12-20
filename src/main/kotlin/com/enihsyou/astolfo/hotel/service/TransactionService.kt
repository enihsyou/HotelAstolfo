package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.Transaction
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
        type: Room.RoomType,
        pageable: Pageable
    ): List<Transaction>

    fun listTransactionByDirection(
        direction: Room.RoomDirection,
        pageable: Pageable
    ): List<Transaction>

    fun listTransactionByPriceBetween(
        priceFrom: Int,
        priceTo: Int,
        pageable: Pageable
    ): List<Transaction>
}

//@Service
//class TransactionServiceImpl : TransactionService {
//
//}
