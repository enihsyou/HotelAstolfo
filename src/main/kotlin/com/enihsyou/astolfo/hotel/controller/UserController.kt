package com.enihsyou.astolfo.hotel.controller

import com.enihsyou.astolfo.hotel.domain.Users
import com.enihsyou.astolfo.hotel.error.注册时用户已存在
import com.enihsyou.astolfo.hotel.error.用户不存在
import com.enihsyou.astolfo.hotel.repository.UserRepository
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Payload

data class LoginPayload(
    @JsonProperty("phone_number") val phone_number: Long = 0,
    @JsonProperty("password") val password: String = ""
) : Payload


@RestController
@RequestMapping("/api/users")
class UserController {
  @Autowired
  lateinit var userRepository: UserRepository

  @GetMapping("/list")
  fun listUsers(): MutableIterable<Users>? {
    return userRepository.findAll()
  }

  @GetMapping("/{phone}")
  fun getUser(@PathVariable phone: Long): Users? =
      userRepository.findOne(phone)

  @PostMapping("/signup")
  fun signUp(@RequestBody payload: LoginPayload): Unit {
    val (phone_number, password) = payload
    val user = userRepository.findOne(phone_number)
    /*如果用户已经存在*/
    if (user == null) {
      /*注册并返回*/
      userRepository.save(Users(
              phone_number = phone_number,
              password = password
      ))
    } else throw 注册时用户已存在(phone_number.toString())
  }

  @PostMapping("/login")
  fun login(@RequestBody payload: LoginPayload): Unit {
    val (phone_number, password) = payload
    val user = userRepository.findOne(phone_number)?: throw 用户不存在(phone_number.toString())
    return
  }
  @GetMapping("/{userID}/book")
  fun getUserCustomers(@PathVariable userID: Long?) {
    // ...
  }

  @PutMapping("/{userID}")
  fun updateUser() {
    //todo 更新用户信息
  }

  @DeleteMapping("/{userID}")
  fun deleteUser(@PathVariable userID: Long?) {
    //todo 删除用户
  }

}
