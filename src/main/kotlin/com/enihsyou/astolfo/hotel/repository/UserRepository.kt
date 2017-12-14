package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.User
import org.springframework.data.repository.PagingAndSortingRepository


interface UserRepository: PagingAndSortingRepository<User, Long> {

//  fun findByPhone_number(phoneNumber: String): List<Users>
}


