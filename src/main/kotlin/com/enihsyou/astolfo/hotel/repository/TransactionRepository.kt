package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.BookTransaction
import org.springframework.data.repository.PagingAndSortingRepository

interface TransactionRepository: PagingAndSortingRepository<BookTransaction, Int>
