package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.Guest
import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@CrossOrigin
@RestController("用户接口控制器")
@RequestMapping("/api/users")
class UserController {

    @Autowired lateinit var userService: UserService

    @GetMapping
    fun information(@RequestParam phone: String): User?
        = userService.findByPhone(phone)

    @PutMapping
    fun updateInformation(@RequestParam phone: String, @RequestBody user:User): User
        = userService.updateInformation(phone, user)


    @PostMapping("/make")
    @ResponseStatus(HttpStatus.CREATED)
    fun make(@RequestBody user: User): User
        = user.run {
        userService.make(phoneNumber, password, nickname, role = User.UserRole.注册用户)
    }


    @PostMapping("/make/admin")
    @ResponseStatus(HttpStatus.CREATED)
    fun makeAdmin(@RequestBody user: User): User
        = user.run {
        userService.make(phoneNumber, password, nickname, role = User.UserRole.管理员)
    }


    @PostMapping("/make/employee")
    @ResponseStatus(HttpStatus.CREATED)
    fun makeEmployee(@RequestBody user: User): User
        = user.run {
        userService.make(phoneNumber, password, nickname, role = User.UserRole.前台)
    }


    @PostMapping("/login")
    fun login(@RequestBody user: User): User
        = user.run {
        userService.login(phoneNumber, password)
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    fun logout(@RequestHeader("Authorization") header: String): String
        = "忘掉密码不就退出了么w"

    @GetMapping("/transactions")
    fun transactions(@RequestParam("phone") phone: String, pageable: Pageable): List<Transaction>
        = userService.findTransactions(phone, pageable)

    @GetMapping("/guests")
    fun guests(@RequestParam("phone") phone: String, pageable: Pageable): List<Guest>
        = userService.findGuests(phone, pageable)

    @PostMapping("/guests")
    @ResponseStatus(HttpStatus.CREATED)
    fun addGuest(@RequestParam("phone") phone: String, @RequestBody guest: Guest)
        = userService.addGuest(phone, guest)

    @DeleteMapping("/guests")
    @ResponseStatus(HttpStatus.GONE)
    fun deleteGuest(@RequestParam("phone") phone: String, @RequestBody guest: Guest)
        = userService.deleteGuest(phone, guest)
}
