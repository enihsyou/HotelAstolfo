package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.domain.User
import org.intellij.lang.annotations.Language
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository("订单仓库")
@RepositoryRestResource(path = "transactions")
interface TransactionRepository : PagingAndSortingRepository<Transaction, Int> {

    fun findByUser(user: User): List<Transaction>


    @Query(value = "SELECT T FROM Transaction T where T.activated=true and T.room.roomNumber.floor=?1 and T.room.roomNumber.number=?2")
    fun findByRoomNumber(@Param("floor") floor: Int, @Param("number") number: Int): List<Transaction>

    @Query(value = "SELECT T FROM Transaction T where not (T.dateTo <?1 OR T.dateFrom >?2)")
    fun findByCreatedDateBetween(from: LocalDateTime,
                                 to: LocalDateTime): List<Transaction>

    @Query(value = "SELECT T FROM Transaction T where T.dateFrom > ?1")
    fun findByJoinDate(from: LocalDateTime): List<Transaction>
    @Query(value = "SELECT T FROM Transaction T where T.dateTo < ?1")
    fun findByLeaveDate(to: LocalDateTime): List<Transaction>


    @Language("HQL")
    @Query(value = "SELECT T FROM Transaction T where T.activated=true and not (dateTo <?1 OR dateFrom >?2)")
    fun findByValidBetween(from: LocalDateTime,
                           to: LocalDateTime): List<Transaction>
}
