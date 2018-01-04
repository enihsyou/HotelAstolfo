package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.RoomDirection
import com.enihsyou.astolfo.hotel.domain.RoomType
import com.enihsyou.astolfo.hotel.exception.已有订单的房间不能删除
import com.enihsyou.astolfo.hotel.exception.房号不存在
import com.enihsyou.astolfo.hotel.exception.房间朝向不存在
import com.enihsyou.astolfo.hotel.exception.房间类型不存在
import com.enihsyou.astolfo.hotel.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface RoomService {
    fun addRoom(room: Room): ResponseEntity<Room>

    fun listRooms(
        from: String? = null,
        to: String? = null,
        type: String? = null,
        direction: String? = null,
        priceFrom: Int? = null,
        priceTo: Int? = null,
        floor: Int? = null,
        number: Int? = null,
        broken: Boolean? = null,
        available: Boolean? = null
    ): List<Room>

    fun getRoom(floor: Int, number: Int): Room
    fun modifyRoom(floor: Int, number: Int, payload: Map<String, String>): Room
    fun deleteRoom(floor: Int, number: Int)

    fun listTypes(): List<RoomType>
    fun addType(type: RoomType): ResponseEntity<RoomType>
    fun modifyType(type: String, payload: Map<String, String>): RoomType
    fun deleteType(type: String)

    fun listDirections(): List<RoomDirection>
    fun addDirection(direction: RoomDirection): ResponseEntity<RoomDirection>
    fun modifyDirection(direction: String, payload: Map<String, String>): RoomDirection
    fun deleteDirection(direction: String)

    fun listFloors(): List<Room.RoomNumber>
}

@Service(value = "房间层逻辑")
class RoomServiceImpl : RoomService {


    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var commentRepository: CommentRepository
    @Autowired lateinit var transactionRepository: TransactionRepository
    @Autowired lateinit var roomRepository: RoomRepository
    @Autowired lateinit var roomTypeRepository: RoomTypeRepository
    @Autowired lateinit var roomDirectionRepository: RoomDirectionRepository
    @Autowired lateinit var guestRepository: GuestRepository

    override fun listRooms(
        from: String?,
        to: String?,
        type: String?,
        direction: String?,
        priceFrom: Int?,
        priceTo: Int?,
        floor: Int?,
        number: Int?,
        broken: Boolean?,
        available: Boolean?
    ): List<Room> {
        var result = roomRepository.findAll()
        if (type != null) {
            result = result
                .filter { it.type.type == type }
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

//            if (from != null) {
//                from2 = LocalDateTime.parse(from)
//                result.filterNot { it in transactionRepository.findByJoinDate(from2).map { it.room } }
//            }
//            if (to != null) {
//                to2 = LocalDateTime.parse(to)
//                result = result.filterNot { it in transactionRepository.findByLeaveDate(to2).map { it.room } }
//            }
        }
        if (broken != null) {
            result = result.filter { it.broken == broken }
        }
//        if (available!=null){
//            result = result.filter { transactionRepository.findByRoomNumber(it.roomNumber.floor, it.roomNumber.number).fi }
//        }

        return result.toList()
    }

    override fun addRoom(room: Room): ResponseEntity<Room> {
        val room_test = roomRepository.findByRoomNumber(room.roomNumber.floor, room.roomNumber.number)
        return if (room_test == null) {
            val type = getType(room.type.type)
            val direction = getDirection(room.direction.type)

            room.type = type
            room.direction = direction

            val new_room = roomRepository.save(room)
            ResponseEntity(new_room, HttpStatus.CREATED)
        } else
            ResponseEntity(room_test, HttpStatus.CONFLICT)
    }

    override fun modifyRoom(floor: Int, number: Int, payload: Map<String, String>): Room {
        val room = getRoom(floor, number)

        for ((key, value) in payload)
            when (key) {
                "broken"    ->
                    room.broken = value.toBoolean()

                "type"      ->
                    room.type = getType(value)

                "direction" ->
                    room.direction = getDirection(value)

                "price"     ->
                    room.price = value.toInt()

                "specialty" ->
                    room.specialty = value
            }

        roomRepository.save(room)
        return room
    }

    override fun deleteRoom(floor: Int, number: Int) {
        val room = getRoom(floor, number)
        if (room.transactions.isNotEmpty())
            throw 已有订单的房间不能删除(floor, number)

        roomRepository.delete(room)
    }


    override fun listTypes(): List<RoomType>
        = roomTypeRepository.findAll().toList()

    override fun addType(type: RoomType): ResponseEntity<RoomType> {
        val type_test = roomTypeRepository.findByType(type.type)
        return if (type_test == null) {
            roomTypeRepository.save(type)
            ResponseEntity(type, HttpStatus.CREATED)
        } else
            ResponseEntity(type_test, HttpStatus.CONFLICT)
    }

    override fun modifyType(type: String, payload: Map<String, String>): RoomType {
        val type_test = getType(type)

        payload["description"]?.let { type_test.description = it }

        roomTypeRepository.save(type_test)
        return type_test
    }

    override fun deleteType(type: String)
        = roomTypeRepository.delete(getType(type))


    override fun listDirections(): List<RoomDirection>
        = roomDirectionRepository.findAll().toList()


    override fun addDirection(direction: RoomDirection): ResponseEntity<RoomDirection> {
        val type_test = roomDirectionRepository.findByType(direction.type)
        return if (type_test == null) {
            roomDirectionRepository.save(direction)
            ResponseEntity(direction, HttpStatus.CREATED)
        } else
            ResponseEntity(type_test, HttpStatus.CONFLICT)
    }

    override fun modifyDirection(direction: String, payload: Map<String, String>): RoomDirection {
        val direction_test = getDirection(direction)

        payload["description"]?.let { direction_test.description = it }

        roomDirectionRepository.save(direction_test)
        return direction_test
    }

    override fun deleteDirection(direction: String)
        = roomDirectionRepository.delete(getDirection(direction))

    override fun listFloors(): List<Room.RoomNumber> {
        return roomRepository.listFloor()
    }

    override fun getRoom(floor: Int, number: Int): Room
        = roomRepository.findByRoomNumber(floor, number) ?:
        throw 房号不存在(floor, number)

    fun getType(type: String): RoomType
        = roomTypeRepository.findByType(type) ?:
        throw 房间类型不存在(type)

    fun getDirection(type: String): RoomDirection
        = roomDirectionRepository.findByType(type) ?:
        throw 房间朝向不存在(type)
}
