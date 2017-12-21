package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.Guest
import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController("用户接口控制器")
@RequestMapping("/api/users")
class UserController {

    @Autowired lateinit var userService: UserService

    @PostMapping("/make")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody user: User): User {
        user.run {
            return userService.make(phoneNumber, password, nickname)
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody payload: User): User =
        payload.run { return userService.login(phoneNumber, password) }

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") header: String): String =
        "忘掉密码不就退出了么w"

    @GetMapping("/transactions")
    fun transactions(@RequestParam("phone") phone: String, pageable: Pageable): List<Transaction> = userService.findTransactions(phone, pageable)

    @GetMapping("/guests")
    fun guests(@RequestParam("phone") phone: String, pageable: Pageable): List<Guest>
        = userService.findGuests(phone, pageable)
    @PostMapping("/guests")
    fun addGuest(@RequestParam("phone") phone: String, @RequestBody guest: Guest)
        = userService.addGuest(phone, guest)
}
