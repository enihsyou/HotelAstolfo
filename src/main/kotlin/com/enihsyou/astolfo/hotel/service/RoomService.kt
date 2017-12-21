package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.repository.GuestRepository
import com.enihsyou.astolfo.hotel.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface RoomService {
    fun listRoomByParameter(
        from: LocalDateTime,
        to: LocalDateTime,
        type: Room.RoomType,
        direction: Room.RoomDirection,
        priceFrom: Int,
        priceTo: Int,
        pageable: Pageable
    ): List<Room>

    fun listRoomByDateBetween(
        from: LocalDateTime,
        to: LocalDateTime,
        pageable: Pageable
    ): List<Room>

    fun listRoomByPriceBetween(
        priceFrom: Int,
        priceTo: Int,
        pageable: Pageable
    ): List<Room>

    fun listRoomByDirection(
        direction: Room.RoomDirection,
        pageable: Pageable
    ): List<Room>

    fun listRoomByType(
        type: Room.RoomType,
        pageable: Pageable
    ): List<Room>
}

@Service(value = "房间层逻辑")
class RoomServiceImpl : RoomService {

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var roomRepository: RoomService
    @Autowired lateinit var guestRepository: GuestRepository
    override fun listRoomByParameter(from: LocalDateTime,
                                     to: LocalDateTime,
                                     type: Room.RoomType,
                                     direction: Room.RoomDirection,
                                     priceFrom: Int,
                                     priceTo: Int,
                                     pageable: Pageable): List<Room> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listRoomByDateBetween(from: LocalDateTime,
                                       to: LocalDateTime,
                                       pageable: Pageable): List<Room> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listRoomByPriceBetween(priceFrom: Int,
                                        priceTo: Int,
                                        pageable: Pageable): List<Room> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listRoomByDirection(direction: Room.RoomDirection,
                                     pageable: Pageable): List<Room> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun listRoomByType(type: Room.RoomType,
                                pageable: Pageable): List<Room> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
