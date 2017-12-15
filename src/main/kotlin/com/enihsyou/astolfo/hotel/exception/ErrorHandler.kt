package com.enihsyou.astolfo.hotel.exception

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

class ApiError private constructor() {
    /*HTTP 状态码*/
    lateinit var status: HttpStatus
    /*发生时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now()
    /*有效的信息*/
    var message: String = "什么地方出错了orz"
    /*调用栈信息*/
    var debugMessage: String? = null

    constructor(status: HttpStatus) : this() {
        this.status = status
    }

    constructor(status: HttpStatus, ex: Throwable) : this() {
        this.status = status
        this.debugMessage = ex.localizedMessage
    }

    constructor(status: HttpStatus, message: String, ex: Throwable) : this() {
        this.status = status
        this.message = message
        this.debugMessage = ex.localizedMessage
    }
}

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestException2Handler : ResponseEntityExceptionHandler() {
    /*构建返回相应的方法*/
    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<Any> {
        return ResponseEntity(apiError, apiError.status)
    }

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val error = "Malformed JSON request"
        return buildResponseEntity(ApiError(HttpStatus.BAD_REQUEST, error, ex))
    }


    @ExceptionHandler(注册时用户已存在::class)
    fun userAlreadyExist(exception: 注册时用户已存在): ResponseEntity<Any> {
        return buildResponseEntity(ApiError(HttpStatus.CONFLICT, "该用户已注册", exception))
    }

    @ExceptionHandler(用户不存在::class)
    fun userNotExist(exception: 用户不存在): ResponseEntity<Any> {
        return buildResponseEntity(ApiError(HttpStatus.NOT_FOUND, "当前用户名未注册", exception))
    }

    @ExceptionHandler(用户名和密码不匹配::class)
    fun wrongPassword(exception: 用户名和密码不匹配): ResponseEntity<Any> {
        return buildResponseEntity(ApiError(HttpStatus.BAD_REQUEST, "用户名和密码不匹配", exception))
    }
}
