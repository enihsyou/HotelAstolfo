package com.enihsyou.astolfo.hotel.controller

//import com.enihsyou.astolfo.hotel.repository.UserRepository
import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.error.注册时用户已存在
import com.enihsyou.astolfo.hotel.mybatis.UserMapper
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Payload


data class LoginPayload(
    @JsonProperty("phone_number") val phone_number: String = "",
    @JsonProperty("password") val password: String = ""
) : Payload

@RestController
@RequestMapping("/api/login")
class LoginController {

  @PostMapping
  fun doLogin(@RequestBody payload: LoginPayload): String {
    return payload.toString()
  }

}

@RestControllerAdvice
@RequestMapping("/api/signup",
    consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE), (MediaType.APPLICATION_JSON_VALUE)],
    produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class SignupController {
  @Autowired
  lateinit var userMapper: UserMapper

  @PostMapping
  fun doSignup(@RequestBody payload: LoginPayload): User? {
    val (phone_number, password) = payload
    val user = userMapper.findByPhone(phone_number)
    /*如果用户已经存在*/
    if (user == null) {
      /*注册并返回*/
      userMapper.signUp(phone_number, password)
      return userMapper.findByPhone(phone_number)!!
    } else throw 注册时用户已存在(phone_number)
  }
}

