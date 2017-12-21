package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.RoomDirectionTable
import com.enihsyou.astolfo.hotel.domain.RoomTypeTable
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


@Repository("房间仓库")
@RepositoryRestResource(path = "rooms")
interface RoomRepository : PagingAndSortingRepository<Room, Room.RoomNumber> {

    @Query(value = "SELECT R FROM Room R LEFT JOIN R.transactions T with T.activated=false or (T.date_to <?1 OR T.date_from >?2)")
    fun findByTimeBetweenQuery(@Param("from") from: LocalDateTime,
                               @Param("to") to: LocalDateTime,
                               pageable: Pageable): List<Room>

    fun findByPriceBetween(@Param("from") from: Int,
                           @Param("to") to: Int,
                           pageable: Pageable): List<Room>

    fun findByRoomNumber(@Param("floor") floor: Int,
                         @Param("index") index: Int,
                         pageable: Pageable): List<Room>

    fun findByRoomNumber_Floor(@Param("floor") floor: Int,
                               pageable: Pageable): List<Room>

    fun findByRoomNumber_Number(@Param("index") index: Int,
                                pageable: Pageable): List<Room>

    fun findByDirection(@Param("direction") direction: String,
                        pageable: Pageable): List<Room>

    fun findByType(@Param("type") type: String,
                   pageable: Pageable): List<Room>

    @Query(value = "SELECT R FROM Room R LEFT JOIN R.transactions T with T.activated=?1")
    fun findByAvailability(@Param("availability") availability: Boolean): List<Room>
}

@Repository("房间类型仓库")
@RepositoryRestResource(path = "roomTypes")
interface RoomTypeRepository : PagingAndSortingRepository<RoomTypeTable, Int>
@Repository("房间朝向仓库")
@RepositoryRestResource(path = "roomDirections")
interface RoomDirectionRepository : PagingAndSortingRepository<RoomDirectionTable, Int>
