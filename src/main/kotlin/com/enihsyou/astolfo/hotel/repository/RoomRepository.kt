package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Room
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


@Repository("房间仓库")
@RepositoryRestResource(path = "rooms")
interface RoomRepository : PagingAndSortingRepository<Room, String> {

    @Query(value = "SELECT R FROM Room R LEFT JOIN R.transactions T with T.activated=false or (T.date_to <?1 OR T.date_from >?2)")
    fun findByTimeBetweenQuery(@Param("from") from: LocalDateTime,
                               @Param("to") to: LocalDateTime): List<Room>

    fun findByPriceBetween(@Param("from") from: Int,
                           @Param("to") to: Int): List<Room>

    fun findByRoomNumber(@Param("floor") floor: Int,
                         @Param("index") index: Int): List<Room>

    fun findByRoomNumber_Floor(@Param("floor") floor: Int): List<Room>

    fun findByRoomNumber_Number(@Param("index") index: Int): List<Room>

    fun findByDirection(@Param("direction") direction: Room.RoomDirection): List<Room>

    fun findByType(@Param("type") type: Room.RoomType): List<Room>
}

