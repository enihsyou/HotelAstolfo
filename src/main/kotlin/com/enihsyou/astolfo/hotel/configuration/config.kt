package com.enihsyou.astolfo.hotel.configuration

import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.exception.没权限
import com.enihsyou.astolfo.hotel.exception.用户不存在
import com.enihsyou.astolfo.hotel.exception.用户名和密码不匹配
import com.enihsyou.astolfo.hotel.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
@WebFilter("/api/**")
class MyFilter : GenericFilterBean() {

    @Autowired lateinit var userRepository: UserRepository

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
//        return chain.doFilter(req, res)

        val request = req as HttpServletRequest
        val response = res as HttpServletResponse
        val authHeader = request.getHeader("Authorization")

        if ("OPTIONS" == request.method) {
            response.status = HttpServletResponse.SC_OK

            chain.doFilter(req, res)
        } else {
            val s = request.servletPath

            if (!s.startsWith("/api")) {
                println("访问$s , 跳过")
                return chain.doFilter(req, res)
            }
            println("访问$s")
            when {
                s in listOf(
                    "/api/users/login",
                    "/api/users/logout",
                    "/api/users/make",
                    "/api/rooms", // 假设这里只有GET方法
                    "/api/rooms/load",
                    "/api/rooms/load2") -> return chain.doFilter(req, res)

                s.startsWith("/api/") -> {
                    if (authHeader == null || !authHeader.startsWith("Basic "))
                        return response.sendError(HttpStatus.UNAUTHORIZED.value(), "访问$s ${没权限()}")

                    val (phone, password) = checkAuthorization(authHeader)

                    val user = userRepository.findByPhoneNumber(phone) ?:
                        return response.sendError(HttpStatus.UNAUTHORIZED.value(), "访问$s ${用户不存在(phone)}")
                    if (user.password != password)
                        return response.sendError(HttpStatus.UNAUTHORIZED.value(), "访问$s ${用户名和密码不匹配()}")
                    if (s == "/api/users/make/admin" || s == "/api/users/make/employee")
                        if (user.role != User.UserRole.经理)
                            return response.sendError(HttpStatus.UNAUTHORIZED.value(), "访问$s ${没权限()}")

                }
            }
            chain.doFilter(req, res)
        }
    }
}

fun checkAuthorization(authHeader: String): Pair<String, String> {
    val input = authHeader.substring(6)
    val (phone, password) = Base64.getDecoder().decode(input).toString(Charset.defaultCharset()).split(":")
    return Pair(phone, password)
}
