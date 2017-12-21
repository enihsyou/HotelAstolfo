package com.enihsyou.astolfo.hotel.service

import com.enihsyou.astolfo.hotel.domain.Guest
import com.enihsyou.astolfo.hotel.domain.Transaction
import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.exception.注册时用户已存在
import com.enihsyou.astolfo.hotel.exception.用户不存在
import com.enihsyou.astolfo.hotel.exception.用户名和密码不匹配
import com.enihsyou.astolfo.hotel.exception.相同身份证已存在
import com.enihsyou.astolfo.hotel.repository.GuestRepository
import com.enihsyou.astolfo.hotel.repository.UserRepository
import com.google.common.hash.Hashing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets


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
    fun listGuests(phone: String): List<Guest>
    fun addGuest(phone: String,
                 guest: Guest)
}

@Service(value = "用户层逻辑")
class UserServiceImpl : UserService {

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var guestRepository: GuestRepository
    override fun addGuest(phone: String,
                          guest: Guest) {
        val id = guest.identification
        existUser(phone).let {
            val user = it
            guestRepository.findByIdentification(id)?.let {
                guestRepository.save(Guest(identification = id, name = guest.name, user = user))

            } ?: throw 相同身份证已存在(id)
        }
    }

    override fun listGuests(phone: String): List<Guest> {
        existUser(phone).let {
            return it.guests
        }
    }

    private fun existUser(phone: String): User =
        if (userRepository.exists(phone))
            userRepository.findOne(phone)
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


    override fun findUserByPhone(phone: String): User {
        return userRepository.findOne(phone) ?: throw 用户不存在(phone)
    }

    override fun listUsers(pageable: Pageable): List<User> {
        return userRepository.findAll(pageable).toList()
    }

    override fun signUp(phoneNumber: String,
                        password: String,
                        nickname: String) {
        /*如果用户已经存在*/
        userRepository.findByPhone_number(phoneNumber)?.let {
            /*注册并返回*/
            userRepository.save(User(
                phone_number = phoneNumber,
                nickname = nickname,
                /*如果密码不是经过前端哈希的，这里进行哈希*/
                password = getCheckedPassword(password)
            ))
        } ?: throw 注册时用户已存在(phoneNumber)
    }

    override fun login(phoneNumber: String,
                       password: String) {
        val user = userRepository.findOne(phoneNumber) ?: throw 用户不存在(phoneNumber)
        return
    }
}
