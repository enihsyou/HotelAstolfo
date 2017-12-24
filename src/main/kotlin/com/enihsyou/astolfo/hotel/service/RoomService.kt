package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.RoomDirection
import com.enihsyou.astolfo.hotel.domain.RoomType
import com.enihsyou.astolfo.hotel.repository.GuestRepository
import com.enihsyou.astolfo.hotel.repository.RoomDirectionRepository
import com.enihsyou.astolfo.hotel.repository.RoomRepository
import com.enihsyou.astolfo.hotel.repository.RoomTypeRepository
import com.enihsyou.astolfo.hotel.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime


interface RoomService {
    fun addRoom(room: Room): ResponseEntity.BodyBuilder?

    fun listRoomByParameter(from: LocalDateTime?=null,
                            to: LocalDateTime?=null,
                            type: String?=null,
                            direction: String?=null,
                            priceFrom: Int?=null,
                            priceTo: Int?=null,
                            floor: Int?=null,
                            number: Int?=null): List<Room>

    fun addRoomDirection(direction: RoomDirection): ResponseEntity.BodyBuilder
    fun addType(type: RoomType): ResponseEntity.BodyBuilder
}

@Service(value = "房间层逻辑")
class RoomServiceImpl : RoomService {

    override fun addRoomDirection(direction: RoomDirection): ResponseEntity.BodyBuilder {
        return if (roomDirectionRepository.findByType(direction.type) == null) {
            roomDirectionRepository.save(direction)
            ResponseEntity.ok()
        } else
            ResponseEntity.status(HttpStatus.CONFLICT)
    }

    override fun addType(type: RoomType): ResponseEntity.BodyBuilder {
        return if (roomTypeRepository.findByType(type.type) == null) {
            roomTypeRepository.save(type)
            ResponseEntity.ok()
        } else
            ResponseEntity.status(HttpStatus.CONFLICT)
    }

    override fun addRoom(room: Room): ResponseEntity.BodyBuilder {
        return if (roomRepository.findByRoomNumber(room.roomNumber) == null) {
            roomRepository.save(room)
            ResponseEntity.ok()
        } else
            ResponseEntity.status(HttpStatus.CONFLICT)

    }

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var roomRepository: RoomRepository
    @Autowired lateinit var roomTypeRepository: RoomTypeRepository
    @Autowired lateinit var roomDirectionRepository: RoomDirectionRepository
    @Autowired lateinit var guestRepository: GuestRepository
    override fun listRoomByParameter(from: LocalDateTime?,
                                     to: LocalDateTime?,
                                     type: String?,
                                     direction: String?,
                                     priceFrom: Int?,
                                     priceTo: Int?,
                                     floor: Int?,
                                     number: Int?): List<Room> {
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
            result = result.filter{ it.price >= priceFrom }
        }
        if (priceTo != null) {
            result = result.filter { it.price <= priceTo }
        }
        if (from != null && to != null) {
            result = result.filter { it in roomRepository.findByDateBetween(from, to) }
        }

        return result.toList()
    }
}
