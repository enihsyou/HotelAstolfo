package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Users
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import javax.persistence.Entity
import javax.persistence.NamedQuery
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository


interface UserRepository: PagingAndSortingRepository<Users, Long> {

//  fun findByPhone_number(phoneNumber: String): List<Users>
}


