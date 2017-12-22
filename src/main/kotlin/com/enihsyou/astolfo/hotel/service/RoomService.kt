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
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface RoomService {
//    fun listRoomByDateBetween(
//        from: LocalDateTime,
//        to: LocalDateTime,
//        pageable: Pageable
//    ): List<Room>
//
//    fun listRoomByPriceBetween(
//        priceFrom: Int,
//        priceTo: Int,
//        pageable: Pageable
//    ): List<Room>
//
//    fun listRoomByDirection(
//        direction: String,
//        pageable: Pageable
//    ): List<Room>
//
//    fun listRoomByType(
//        type: String,
//        pageable: Pageable
//    ): List<Room>
//
//    fun listByRoomNumber(floor: Int,
//                         number: Int,
//                         pageable: Pageable): List<Room>

    fun addRoom(room: Room)

    fun listRoomByParameter(from: LocalDateTime?,
                            to: LocalDateTime?,
                            type: String?,
                            direction: String?,
                            priceFrom: Int?,
                            priceTo: Int?,
                            floor: Int?,
                            number: Int?): List<Room>

    fun addRoomDirection(direction: RoomDirection)
    fun addType(type: RoomType)
}

@Service(value = "房间层逻辑")
class RoomServiceImpl : RoomService {

    override fun addRoomDirection(direction: RoomDirection) {
        roomDirectionRepository.save(direction)
    }

    override fun addType(type: RoomType) {
        roomTypeRepository.save(type)
    }

    override fun addRoom(room: Room) {
        roomRepository.save(room)
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
            result = result.filter { it.roomNumber.floor != floor }
        }
        if (number != null) {
            result = result.filter { it.roomNumber.number != number }
        }
        if (priceFrom != null) {
            result = result.filter { it.price < priceFrom }
        }
        if (priceTo != null) {
            result = result.filter { it.price < priceTo }
        }
        if (from != null && to != null) {
            result = result.filter { it in roomRepository.findByDateBetween(from, to) }
        }

        return result.toList()
    }
//
//
//    override fun listRoomByDateBetween(from: LocalDateTime,
//                                       to: LocalDateTime,
//                                       pageable: Pageable): List<Room>
//        = roomRepository.findByDateBetween(from, to)
//
////    override fun listByRoomNumber(floor: Int,
////                                  number: Int,
////                                  pageable: Pageable): List<Room>
////        = roomRepository.findByRnumber(floor, number, pageable)
//
//    override fun listRoomByPriceBetween(priceFrom: Int,
//                                        priceTo: Int,
//                                        pageable: Pageable): List<Room>
//        = roomRepository.findByPriceBetween(priceFrom, priceTo, pageable)
//
//    override fun listRoomByDirection(direction: String,
//                                     pageable: Pageable): List<Room>
//        = roomRepository.findByDirection(direction, pageable)
//
//    override fun listRoomByType(type: String,
//                                pageable: Pageable): List<Room>
//        = roomRepository.findByType(type, pageable)
}
