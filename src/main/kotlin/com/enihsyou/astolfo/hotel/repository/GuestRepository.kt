package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Guest
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository("旅客仓库")
@RepositoryRestResource(path = "guests")
interface GuestRepository : PagingAndSortingRepository<Guest, Int> {
    fun findByIdentification(identificationNumber: String):Guest?
    fun findByName(Name: String):Guest?
    fun findByUserId(id: Int): List<Guest>
}
