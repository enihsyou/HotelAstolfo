package com.enihsyou.astolfo.hotel.repository

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.RoomDirection
import com.enihsyou.astolfo.hotel.domain.RoomType
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.criteria.Predicate


@Repository("房间仓库")
@RepositoryRestResource(path = "rooms")
interface RoomRepository : PagingAndSortingRepository<Room, Int> {

    @Query(value = "SELECT R FROM Room R LEFT JOIN R.transactions T with T.activated=false or (T.dateTo <?1 OR T.dateFrom >?2)")
    fun findByDateBetween(@Param("from") from: LocalDateTime,
                          @Param("to") to: LocalDateTime): List<Room>

    @Query(value = "SELECT R FROM Room R LEFT JOIN R.transactions T with T.activated=false or (T.dateTo <?1 OR T.dateFrom >?2)")
    fun findByTransactionsDateBetween(@Param("from") from: LocalDateTime,
                          @Param("to") to: LocalDateTime,
                          pageable: Pageable): List<Room>

    fun findByPriceBetween(@Param("from") from: Int,
                           @Param("to") to: Int,
                           pageable: Pageable): List<Room>

    fun findByRoomNumber(roomNumber: Room.RoomNumber): Room?
//
//    fun findByFloor(@Param("floor") floor: Int,
//                               pageable: Pageable): List<Room>
//
//    fun findByNumber(@Param("index") index: Int,
//                                pageable: Pageable): List<Room>

    fun findByDirection_Type(@Param("direction") direction: String,
                        pageable: Pageable): List<Room>

    fun findByType_Type(@Param("type") type: String,
                   pageable: Pageable): List<Room>

    @Query(value = "SELECT R FROM Room R LEFT JOIN R.transactions T with T.activated=?1")
    fun findByAvailability(@Param("availability") availability: Boolean): List<Room>
}

@Repository("房间类型仓库")
@RepositoryRestResource(path = "roomTypes")
interface RoomTypeRepository : PagingAndSortingRepository<RoomType, Int>{

    fun findByType(type: String):RoomType?
}

@Repository("房间朝向仓库")
@RepositoryRestResource(path = "roomDirections")
interface RoomDirectionRepository : PagingAndSortingRepository<RoomDirection, Int>{

    fun findByType(type: String):RoomDirection?
}
