package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Transaction
import org.springframework.data.repository.PagingAndSortingRepository

interface TransactionRepository: PagingAndSortingRepository<Transaction, Int>
