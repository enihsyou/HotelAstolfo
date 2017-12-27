package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.RoomDirection
import com.enihsyou.astolfo.hotel.domain.RoomType
import com.enihsyou.astolfo.hotel.service.RoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController("房间接口控制器")
@RequestMapping("/api/rooms")
class RoomController {

    @Autowired lateinit var roomService: RoomService

    /*房间*/
    @GetMapping
    fun listRooms(
        @RequestParam("from", required = false) from: String? = null,
        @RequestParam("to", required = false) to: String? = null,
        @RequestParam("type", required = false) type: String? = null,
        @RequestParam("direction", required = false) direction: String? = null,
        @RequestParam("priceFrom", required = false) priceFrom: Int? = null,
        @RequestParam("priceTo", required = false) priceTo: Int? = null,
        @RequestParam("floor", required = false) floor: Int? = null,
        @RequestParam("number", required = false) number: Int? = null
    )
        = roomService.listRooms(from, to, type, direction, priceFrom, priceTo, floor, number)

    @PostMapping
    fun addRoom(@RequestBody room: Room)
        = roomService.addRoom(room)

    @PatchMapping
    fun modifyRoom(@RequestParam floor: Int, @RequestParam number: Int, @RequestBody payload: Map<String, String>)
        = roomService.modifyRoom(floor, number, payload)

    @DeleteMapping
    @ResponseStatus(HttpStatus.GONE)
    fun deleteRoom(@RequestParam floor: Int, @RequestParam number: Int)
        = roomService.deleteRoom(floor, number)


    /*房间类型*/
    @GetMapping("/types")
    fun listTypes()
        = roomService.listTypes()

    @PostMapping("/types")
    fun addType(@RequestBody type: RoomType)
        = roomService.addType(type)

    @PatchMapping("/types")
    fun modifyType(@RequestParam type: String, @RequestBody payload: Map<String, String>)
        = roomService.modifyType(type, payload)

    @DeleteMapping("/types")
    @ResponseStatus(HttpStatus.GONE)
    fun deleteType(@RequestParam type: String)
        = roomService.deleteType(type)


    /*房间朝向*/
    @GetMapping("/directions")
    fun listDirections()
        = roomService.listDirections()

    @PostMapping("/directions")
    fun addDirection(@RequestBody direction: RoomDirection)
        = roomService.addDirection(direction)

    @PatchMapping("/directions")
    fun modifyDirection(@RequestParam direction: String, @RequestBody payload: Map<String, String>)
        = roomService.modifyDirection(direction,payload)

    @DeleteMapping("/directions")
    @ResponseStatus(HttpStatus.GONE)
    fun deleteDirection(@RequestBody direction: String)
        = roomService.deleteDirection(direction)


    /*楼层信息*/
    @GetMapping("/floors")
    fun listFloors()
        = roomService.listFloors()


    /*初始化信息获取*/
    @GetMapping("/load")
    fun load(): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result.put("types", listTypes().map { it.type })
        result.put("directions", listDirections().map { it.type })
        result.put("rooms", listRooms().groupBy { it.roomNumber.floor }.mapValues { it.value.map { it.roomNumber.number } })
        return result
    }

    @GetMapping("/load2")
    fun load2(): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result.put("types", listTypes())
        result.put("directions", listDirections())
        result.put("rooms", listRooms())
        return result
    }
}

