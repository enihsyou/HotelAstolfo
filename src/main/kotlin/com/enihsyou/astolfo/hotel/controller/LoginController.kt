package com.enihsyou.astolfo.hotel.controller

//import com.enihsyou.astolfo.hotel.repository.UserRepository
import com.enihsyou.astolfo.hotel.mybatis.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/login")
class LoginController {

//  @Autowired
//  lateinit var userRepository: UserRepository

  @PostMapping
  fun doLogin(@RequestParam("username") username: String, @RequestParam("password") password: String): String {
    println("username = ${username}")
    println("password = ${password}")
    return "$username + $password"
  }

}

@RestController
@RequestMapping("/api/signup")
class SignupController {
  @Autowired
  private val userMapper: UserMapper? = null


  @PostMapping
  fun doSignup(@RequestParam("username") username: String, @RequestParam("password") password: String) {
    println("username = ${username}")
    println("password = ${password}")
    userMapper?.signUp(username, password)

  }

}
