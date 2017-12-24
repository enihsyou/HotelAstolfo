package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.domain.User
import org.intellij.lang.annotations.Language
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository("订单仓库")
@RepositoryRestResource(path = "transactions")
interface TransactionRepository : PagingAndSortingRepository<Transaction, Int> {

    fun findByUser(user: User,
                   pageable: Pageable): List<Transaction>

    fun findByRoom(room: Room): List<Transaction>

    @Query(value = "SELECT T FROM Transaction T where not (T.dateTo <?1 OR T.dateFrom >?2)")
    fun findByCreateDateBetween(from: LocalDateTime,
                                to: LocalDateTime): List<Transaction>
//
//
//    @Language("HQL")
//    @Query(value = "SELECT T FROM Transaction T where T.activated and not (T.dateTo <?1 OR T.dateFrom >?2)")
//    fun findByValidBetween(from: LocalDateTime,
//                           to: LocalDateTime): List<Transaction>
}
