package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.exception.注册时用户已存在
import com.enihsyou.astolfo.hotel.exception.用户不存在
import com.enihsyou.astolfo.hotel.exception.用户名和密码不匹配
import com.enihsyou.astolfo.hotel.repository.UserRepository
import com.google.common.hash.Hashing
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import javax.annotation.Resource

interface UserService {
    fun signUp(phoneNumber: String,
               password: String,
               nickname: String = "")

    fun login(phoneNumber: String,
              password: String)

    fun findUserByPhone(phone: String): User
    fun listUsers(pageable: Pageable): List<User>
    fun updateInformation(phone: String,
                          old_password: String,
                          new_password: String?,
                          nickname: String?): User

    fun deleteUser(phone: String)
    fun transactions(phone: Long): List<Transaction>
}

@Service
class UserServiceImpl : UserService {

    private fun existUser(phone: String): User =
        if (repository.exists(phone))
            repository.findOne(phone)
        else throw throw 用户不存在(phone)

    private fun getCheckedPassword(password: String): String {
        return if (password.length != 64)
            Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString()
        else
            password
    }

    override fun transactions(phone: Long): List<Transaction> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateInformation(phone: String,
                                   old_password: String,
                                   new_password: String?,
                                   nickname: String?): User {
        existUser(phone).let {
            if (it.password == getCheckedPassword(old_password))
                new_password?.let { p -> it.password = getCheckedPassword(p) }
            else throw 用户名和密码不匹配()
            if (!nickname.isNullOrBlank())
                nickname?.let { n -> it.nickname = nickname }
            return it
        }
    }

    override fun deleteUser(phone: String) {
    }

    @Resource
    lateinit var repository: UserRepository

    override fun findUserByPhone(phone: String): User {
        return repository.findOne(phone) ?: throw 用户不存在(phone)
    }

    override fun listUsers(pageable: Pageable): List<User> {
        return repository.findAll(pageable).toList()
    }

    override fun signUp(phoneNumber: String,
                        password: String,
                        nickname: String) {
        /*如果用户已经存在*/
        if (!repository.exists(phoneNumber)) {
            /*注册并返回*/
            repository.save(User(
                phone_number = phoneNumber,
                nickname = nickname,
                /*如果密码不是经过前端哈希的，这里进行哈希*/
                password = getCheckedPassword(password)
            ))
        } else throw 注册时用户已存在(phoneNumber)
    }

    override fun login(phoneNumber: String,
                       password: String) {
        val user = repository.findOne(phoneNumber) ?: throw 用户不存在(phoneNumber)
        return
    }
}
