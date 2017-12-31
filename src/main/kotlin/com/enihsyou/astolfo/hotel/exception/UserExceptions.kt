package com.enihsyou.astolfo.hotel.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.time.LocalDateTime

@ResponseStatus(value = HttpStatus.CONFLICT)
class 注册时用户已存在(val id: String) :
    RuntimeException("手机号<$id>，注册时用户已存在")

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class 用户不存在(val id: String) :
    RuntimeException("手机号<$id>，用户不存在")

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class 房号不存在(floor: Int, number: Int) :
    RuntimeException("房号<$floor-$number>不存在")

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class 房间类型不存在(type: String) :
    RuntimeException("房间类型<$type>不存在")

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class 房间朝向不存在(type: String) :
    RuntimeException("房间朝向<$type>不存在")

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class 用户未绑定身份证(phone: String, identification: String) :
    RuntimeException("用户<$phone>未绑定身份证<$identification>")

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class 身份证不存在(identification: String) :
    RuntimeException("绑定身份证<$identification>不存在")

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
class 用户名和密码不匹配 :
    RuntimeException("用户名和密码不匹配")

@ResponseStatus(value = HttpStatus.CONFLICT)
class 相同身份证已存在(val id: String) :
    RuntimeException("相同身份证<$id>已存在")

@ResponseStatus(value = HttpStatus.CONFLICT)
class 订单时间冲突(from: LocalDateTime, to: LocalDateTime) :
    RuntimeException("时间<$from, $to>冲突")

@ResponseStatus(HttpStatus.NOT_FOUND)
class 订单不存在(bookId: Int) :
    RuntimeException("订单号<$bookId>不存在呀")

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
class 没权限 :
    RuntimeException("没权限")

@ResponseStatus(value = HttpStatus.LOCKED)
class 房间已损坏(floor: Int, number: Int) :
    RuntimeException("房间<$floor-$number>已损坏")

@ResponseStatus(value = HttpStatus.LOCKED)
class 房间已被占用(floor: Int, number: Int) :
    RuntimeException("房间<$floor-$number>已被占用")

@ResponseStatus(value = HttpStatus.LOCKED)
class 评论已存在不可修改(transaction: Int) :
    RuntimeException("订单<$transaction>的评论已存在, 不可修改")

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class 评论不存在(id: Int) :
    RuntimeException("评论号<$id>不存在")

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
class 已有订单的房间不能删除(floor: Int, number: Int) :
    RuntimeException("房间<$floor-$number>已被使用，不能删除")
