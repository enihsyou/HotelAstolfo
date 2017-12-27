package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Guest
import com.enihsyou.astolfo.hotel.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.stereotype.Repository

@Repository("旅客仓库")
@RepositoryRestResource(path = "guests")
interface GuestRepository : PagingAndSortingRepository<Guest, Int> {
    fun findByIdentification(identificationNumber: String):Guest?
    fun findByName(Name: String):Guest?
    fun findByUserId(id: Int,
                   pageable: Pageable): Page<Guest>
}
