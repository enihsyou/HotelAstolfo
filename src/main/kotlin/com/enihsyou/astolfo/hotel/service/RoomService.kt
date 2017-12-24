package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.RoomDirection
import com.enihsyou.astolfo.hotel.domain.RoomType
import com.enihsyou.astolfo.hotel.repository.GuestRepository
import com.enihsyou.astolfo.hotel.repository.RoomDirectionRepository
import com.enihsyou.astolfo.hotel.repository.RoomRepository
import com.enihsyou.astolfo.hotel.repository.RoomTypeRepository
import com.enihsyou.astolfo.hotel.repository.TransactionRepository
import com.enihsyou.astolfo.hotel.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime


interface RoomService {
    fun addRoom(room: Room): ResponseEntity<Unit>

    fun listRoomByParameter(
        from: String? = null,
        to: String? = null,
        type: String? = null,
        direction: String? = null,
        priceFrom: Int? = null,
        priceTo: Int? = null,
        floor: Int? = null,
        number: Int? = null
    ): List<Room>

    fun addRoomDirection(direction: RoomDirection): ResponseEntity<Unit>
    fun addType(type: RoomType): ResponseEntity<Unit>
}

@Service(value = "房间层逻辑")
class RoomServiceImpl : RoomService {

    override fun addRoomDirection(direction: RoomDirection): ResponseEntity<Unit> {
        return if (roomDirectionRepository.findByType(direction.type) == null) {
            roomDirectionRepository.save(direction)
            ResponseEntity(HttpStatus.CREATED)
        } else
            ResponseEntity(HttpStatus.CONFLICT)
    }

    override fun addType(type: RoomType): ResponseEntity<Unit> {
        return if (roomTypeRepository.findByType(type.type) == null) {
            roomTypeRepository.save(type)
            ResponseEntity(HttpStatus.CREATED)
        } else
            ResponseEntity(HttpStatus.CONFLICT)
    }

    override fun addRoom(room: Room): ResponseEntity<Unit> {
        return if (roomRepository.findByRoomNumber(room.roomNumber.floor, room.roomNumber.number) == null) {
            val type = roomTypeRepository.findByType(room.type.type)
            val direction = roomDirectionRepository.findByType(room.direction.type)
            if (type != null && direction != null) {
                room.type = type
                room.direction = direction
                roomRepository.save(room)
                ResponseEntity(HttpStatus.CREATED)
            } else
                ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
        } else
            ResponseEntity(HttpStatus.CONFLICT)
    }

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var transactionRepository: TransactionRepository
    @Autowired lateinit var roomRepository: RoomRepository
    @Autowired lateinit var roomTypeRepository: RoomTypeRepository
    @Autowired lateinit var roomDirectionRepository: RoomDirectionRepository
    @Autowired lateinit var guestRepository: GuestRepository
    override fun listRoomByParameter(
        from: String?,
        to: String?,
        type: String?,
        direction: String?,
        priceFrom: Int?,
        priceTo: Int?,
        floor: Int?,
        number: Int?
    ): List<Room> {
        var result = roomRepository.findAll()
        if (type != null) {
            result = result.filter { it.type.type == type }
        }
        if (direction != null) {
            result = result.filter { it.direction.type == direction }
        }
        if (floor != null) {
            result = result.filter { it.roomNumber.floor == floor }
        }
        if (number != null) {
            result = result.filter { it.roomNumber.number == number }
        }
        if (priceFrom != null) {
            result = result.filter { it.price >= priceFrom }
        }
        if (priceTo != null) {
            result = result.filter { it.price <= priceTo }
        }
        if (from != null && to != null) {
            val from2: LocalDateTime = LocalDateTime.parse(from)
            val to2: LocalDateTime = LocalDateTime.parse(to)
            result = result.filter { it in roomRepository.findByDateBetween(from2, to2) }
            //
//            if (from != null) {
//                from2 = LocalDateTime.parse(from)
//                result.filterNot { it in transactionRepository.findByJoinDate(from2).map { it.room } }
//            }
//            if (to != null) {
//                to2 = LocalDateTime.parse(to)
//                result = result.filterNot { it in transactionRepository.findByLeaveDate(to2).map { it.room } }
//            }
        }

        return result.toList()
    }
}
