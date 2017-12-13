package com.enihsyou.astolfo.hotel.controller

//import com.enihsyou.astolfo.hotel.repository.UserRepository
import com.enihsyou.astolfo.hotel.domain.LoginPayload
import com.enihsyou.astolfo.hotel.mybatis.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/login")
class LoginController {

//  @Autowired
//  lateinit var userRepository: UserRepository

  @PostMapping
  fun doLogin(@RequestBody payload: LoginPayload): String {
    return payload.toString()
  }

}

@RestController
@RequestMapping("/api/signup")
class SignupController {
  @Autowired
  lateinit var userMapper: UserMapper

  @PostMapping
  fun doSignup(@RequestBody payload: LoginPayload) {
    println("phone_number = ${payload.phoneNumber}")
    println("password = ${payload.password}")
    userMapper.signUp(payload.phoneNumber, payload.password)

  }

}
