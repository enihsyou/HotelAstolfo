package com.enihsyou.astolfo.hotel.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class 注册时用户已存在(val id: String) : RuntimeException("手机号$id，注册时用户已存在")

@ResponseStatus(value= HttpStatus.NOT_FOUND)
class 用户不存在(val id: String) : RuntimeException("手机号$id，用户不存在")

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
class 用户名和密码不匹配 : RuntimeException("用户名和密码不匹配")

@ResponseStatus(value = HttpStatus.CONFLICT)
class 相同身份证已存在(val id: String) : RuntimeException("相同身份证 $id 已存在")
