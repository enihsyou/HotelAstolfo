package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.configuration.checkAuthorization
import com.enihsyou.astolfo.hotel.domain.Guest
import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.exception.用户名和密码不匹配
import com.enihsyou.astolfo.hotel.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController("用户接口控制器")
@RequestMapping("/api/users")
class UserController {

    @Autowired lateinit var userService: UserService

    @PostMapping("/make")
    @ResponseStatus(HttpStatus.CREATED)
    fun make(@RequestBody user: User)
        = user.run {
        userService.createUser(phoneNumber, password, nickname, role = User.UserRole.注册用户)
    }

    @PostMapping("/make/admin")
    @ResponseStatus(HttpStatus.CREATED)
    fun makeAdmin(@RequestBody user: User)
        = user.run {
        userService.createUser(phoneNumber, password, nickname, role = User.UserRole.经理)
    }

    @PostMapping("/make/employee")
    @ResponseStatus(HttpStatus.CREATED)
    fun makeEmployee(@RequestBody user: User)
        = user.run {
        userService.createUser(phoneNumber, password, nickname, role = User.UserRole.前台)
    }

    @GetMapping
    fun listUsers()
        = userService.listUsers()

    @GetMapping("/get")
    fun getUser(@RequestParam phone: String)
        = userService.getUser(phone)

    @PatchMapping
    fun modifyUser(@RequestParam phone: String, @RequestBody payload: Map<String, String>, @RequestHeader("Authorization") header: String) {
        val (user_phone, password) = checkAuthorization(header)
        val up_user = getUser(user_phone) // 是这个人操作的
        if (up_user.password != password)
            throw 用户名和密码不匹配()
        userService.modifyUser(phone, payload, up_user)
    }

    @DeleteMapping
    fun deleteUser(@RequestParam phone: String)
        = userService.deleteUser(phone)

    @PostMapping("/login")
    fun login(@RequestBody user: Map<String, String>): User {
        val number: String = user["phoneNumber"] ?: throw 用户名和密码不匹配()
        val password: String = user["password"] ?: throw 用户名和密码不匹配()
        return userService.login(number, password)
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    fun logout(@RequestHeader("Authorization") header: String)
        = "忘掉密码不就退出了么w"


    /*旅客信息*/
    @GetMapping("/guests")
    fun listGuests(@RequestParam("phone") phone: String)
        = userService.listGuests(phone)

    @PostMapping("/guests")
    fun addGuest(@RequestParam("phone") phone: String, @RequestBody guest: Guest)
        = userService.addGuest(phone, guest)

    @PatchMapping("/guests")
    fun modifyGuest(@RequestParam("guest") identification: String, @RequestBody payload: Map<String, String>)
        = userService.modifyGuest(identification, payload)

    @DeleteMapping("/guests")
    fun deleteGuest(@RequestParam("identification") identification: String)
        = userService.deleteGuest(identification)


    @GetMapping("/transactions")
    fun listTransactions(@RequestParam("phone") phone: String)
        = userService.listTransactions(phone)
}
