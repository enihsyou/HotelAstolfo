package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.BookTransaction
import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.service.UserService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Payload

data class UserPayload(
    @JsonProperty("phone_number") val phone_number: String = "",
    @JsonProperty("password", access = JsonProperty.Access.WRITE_ONLY) val password: String = "",
    @JsonProperty("nickname", required = false) val nickname: String = "",
    @JsonProperty("new_password", required = false) val new_password: String? = ""
) : Payload


@RestController("用户接口控制器")
@RequestMapping("/api/users")
class UserController {

    @Autowired lateinit var userService: UserService

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody payload: UserPayload): User {
        payload.run {
            userService.signUp(phone_number, password, nickname)
        }
        return getUser(payload.phone_number)
    }

    @PostMapping("/login")
    fun login(@RequestBody payload: UserPayload) =
        payload.run { userService.login(phone_number, password) }

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization") header: String): String =
        "忘掉密码不就退出了么w"

    @GetMapping("/list")
    fun listUsers(pageable: Pageable): List<User> =
        userService.listUsers(pageable)

    @GetMapping("/{phone}")
    fun getUser(@PathVariable phone: String): User =
        userService.findUserByPhone(phone)

    @PutMapping("/{phone}")
    fun updateUser(@PathVariable phone: String, @RequestBody payload: UserPayload): User =
        payload.run { userService.updateInformation(phone, password, new_password, nickname) }

    @DeleteMapping("/{phone}")
    fun deleteUser(@PathVariable phone: String) =
        userService.deleteUser(phone)

    @GetMapping("/{phone}/book")
    fun getUserBooks(@PathVariable phone: Long, @RequestHeader("Authorization") header: String): List<BookTransaction> =
        userService.books(phone)
}
