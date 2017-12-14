package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.User
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository("用户")
@RepositoryRestResource
interface UserRepository : PagingAndSortingRepository<User, Long> {

//  fun findByPhone_number(phoneNumber: String): List<Users>
}


