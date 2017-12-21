package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.domain.User
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository("订单仓库")
@RepositoryRestResource(path = "transactions")
interface TransactionRepository: PagingAndSortingRepository<Transaction, Int>{
    fun findByUser(user: User,
                   pageable: Pageable):List<Transaction>
}
