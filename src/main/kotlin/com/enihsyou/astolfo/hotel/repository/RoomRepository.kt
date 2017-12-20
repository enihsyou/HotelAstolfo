package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Room
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime


@Repository("用户")
@RepositoryRestResource
interface RoomRepository : PagingAndSortingRepository<Room, String>{
    fun findRoomByTimeBetween(from:LocalDateTime, to:LocalDateTime)

    fun findRoomByPriceBetween(from:Int, to:Int)
}

