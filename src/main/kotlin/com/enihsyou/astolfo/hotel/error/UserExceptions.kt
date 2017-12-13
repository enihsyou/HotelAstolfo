package com.enihsyou.astolfo.hotel.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.NOT_FOUND, reason = "项目不存在")
class EntityNotFoundException(val id: String) : RuntimeException(id)


class 注册时用户已存在(val id: String) : RuntimeException("手机号$id，注册时用户已存在")
