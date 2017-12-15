package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.service.UserService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import javax.validation.Payload

data class LoginPayload(
        @JsonProperty("phone_number") val phone_number: String = "",
        @JsonProperty("password") val password: String = "",
        @JsonProperty("nickname") val nickname: String = ""
) : Payload


@RestController("用户接口控制器")
@RequestMapping("/api/users")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/signup")
    fun signUp(@RequestBody payload: LoginPayload) {
        val (phone_number, password, nickname) = payload
        userService.signUp(phone_number, password, nickname)
    }

    @PostMapping("/login")
    fun login(@RequestBody payload: LoginPayload) {
        val (phone_number, password) = payload
        userService.login(phone_number, password)
    }

    @PostMapping("/logout")
    fun logout(header: RequestHeader) {
//        val (phone_number, password) = payload
//        userService.login(phone_number, password)
    }

    @GetMapping("/list")
    fun listUsers(pageable: Pageable, header: RequestHeader): List<User> =
            userService.listUsers(pageable)

    @GetMapping("/{userID}/book")
    fun getUserCustomers(@PathVariable userID: Long, header: RequestHeader) {
        // ...
    }

    @GetMapping("/{phone}")
    fun getUser(@PathVariable phone: String, header: RequestHeader): User? =
            userService.findUserByPhone(phone)

    @PutMapping("/{phone}")
    fun updateUser(@PathVariable phone: String, header: RequestHeader) {
        val user = getUser(phone, header)
        user?.let { userService.updateInformation(it) }
    }


    @DeleteMapping("/{phone}")
    fun deleteUser(@PathVariable phone: String, header: RequestHeader) =
            userService.deleteUser(phone)


}
