package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.service.UserService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Payload

data class LoginPayload(
    @JsonProperty("phone_number") val phone_number: Long = 0,
    @JsonProperty("password") val password: String = "",
    @JsonProperty("nickname") val nickname: String = ""
) : Payload


@RestController
@RequestMapping("/api/users")
class UserController {
  @Autowired
  lateinit var userService: UserService

  @GetMapping("/list")
  fun listUsers(): MutableIterable<User>? =
      userService.listUsers()


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

  @GetMapping("/{userID}/book")
  fun getUserCustomers(@PathVariable userID: Long?) {
    // ...
  }

  @GetMapping("/{phone}")
  fun getUser(@PathVariable phone: Long): User? =
      userService.findUserByPhone(phone)

  @PutMapping("/{phone}")
  fun updateUser(@PathVariable phone: Long) =
      userService.updateInformation(phone)


  @DeleteMapping("/{phone}")
  fun deleteUser(@PathVariable phone: Long) =
      userService.deleteUser(phone)


}
