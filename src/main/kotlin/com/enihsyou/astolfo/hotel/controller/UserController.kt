package com.enihsyou.astolfo.hotel.controller

import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/users", name = "处理用户组")
class UserController {
  @GetMapping("/")
  fun listUsers(@PathVariable user: Long?) {
    // ...
  }

  @GetMapping("/{userID}")
  fun getUser(@PathVariable userID: Long?) {
    // ...
  }

  @GetMapping("/{userID}/customers")
  fun getUserCustomers(@PathVariable userID: Long?) {
    // ...
  }

  @PutMapping("/{userID}/")
  fun deleteUser() {
    //todo 删除用户
  }

  @DeleteMapping("/{userID}")
  fun deleteUser(@PathVariable userID: Long?) {
    // ...
  }

}
