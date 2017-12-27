package com.enihsyou.astolfo.hotel.configuration

import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.exception.没权限
import com.enihsyou.astolfo.hotel.exception.用户不存在
import com.enihsyou.astolfo.hotel.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
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

        val request = req as HttpServletRequest
        val response = res as HttpServletResponse
        val authHeader = request.getHeader("Authorization")

        if ("OPTIONS" == request.method) {
            response.status = HttpServletResponse.SC_OK

            chain.doFilter(req, res)
        } else {
            val s = request.servletPath
            if (authHeader == null || !authHeader.startsWith("Basic ")) {
                if (s == "/api/users/make") return chain.doFilter(req, res)
                if (s == "/api/users/make/admin" || s == "/api/users/make/employee")
                    throw 没权限()
                if (s.startsWith("/api/rooms/load")) return chain.doFilter(req, res)
                else throw 没权限()
            }


            val input = authHeader.substring(6)
            val (phone, _) = Base64.getDecoder().decode(input).toString(Charset.defaultCharset()).split(":")


            val user = userRepository.findByPhoneNumber(phone) ?: throw 用户不存在(phone)
            when {
                s.startsWith("/api/users")      -> {
                    if (s == "/api/users/make") return chain.doFilter(req, res)
                    if (s == "/api/users/make/admin" || s == "/api/users/make/employee")
                        if (user.role == User.UserRole.管理员)
                            return chain.doFilter(req, res)
                        else throw 没权限()
                }

                s.startsWith("/api/rooms/load") -> return chain.doFilter(req, res)
                else                            -> if (user.role == User.UserRole.注册用户 || user.role == User.UserRole.未注册) throw 没权限()
            }
            chain.doFilter(req, res)
        }
    }
}
