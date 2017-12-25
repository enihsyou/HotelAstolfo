package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.Room
import com.enihsyou.astolfo.hotel.domain.RoomDirection
import com.enihsyou.astolfo.hotel.domain.RoomType
import com.enihsyou.astolfo.hotel.repository.RoomDirectionRepository
import com.enihsyou.astolfo.hotel.repository.RoomTypeRepository
import com.enihsyou.astolfo.hotel.service.RoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController("房间接口控制器")
@RequestMapping("/api/rooms")
class RoomController {

    @Autowired lateinit var roomService: RoomService
    @Autowired lateinit var roomTypeRepository: RoomTypeRepository
    @Autowired lateinit var roomDirectionRepository: RoomDirectionRepository


    /*根据条件搜索房间*/
    @GetMapping("/list")
    fun listRoom(
        @RequestParam("from", required = false) from: String? = null,
        @RequestParam("to", required = false) to: String? = null,
        @RequestParam("type", required = false) type: String? = null,
        @RequestParam("direction", required = false) direction: String? = null,
        @RequestParam("priceFrom", required = false) priceFrom: Int? = null,
        @RequestParam("priceTo", required = false) priceTo: Int? = null,
        @RequestParam("floor", required = false) floor: Int? = null,
        @RequestParam("number", required = false) number: Int? = null
    )
        = roomService.listRoomByParameter(from, to, type, direction, priceFrom, priceTo, floor, number)

    /*管理员添加一个房间*/
    @PostMapping("/add")
    fun addRoom(@RequestBody room: Room)
        = roomService.addRoom(room)

    /*列出房间类型*/
    @GetMapping("/types")
    fun listTypes()
        = roomService.listTypes()

    /*添加房间类型定义*/
    @PostMapping("/types")
    fun addType(@RequestBody type: RoomType)
        = roomService.addType(type)

    /*添加房间朝向*/
    @GetMapping("/directions")
    fun listDirections()
        = roomService.listDirections()

    /*添加房间朝向定义*/
    @PostMapping("/directions")
    fun addDirection(@RequestBody direction: RoomDirection)
        = roomService.addRoomDirection(direction)

    @GetMapping("/floors")
    fun listFloors()
        = roomService.listFloors()
    @PostMapping("/control")
    fun controlRoom(
        @RequestParam("floor") floor: Int,
        @RequestParam("number") number: Int,
        @RequestBody payload: Map<String, String>)
        = roomService.controlRoom(floor, number, payload)

    @GetMapping("/load")
    fun load(): MutableMap<String, Any> {
        val result = mutableMapOf<String, Any>()
        result.put("types", listTypes())
        result.put("directions", listDirections())
        result.put("rooms", listRoom())
        return result
    }
}

