package com.enihsyou.astolfo.hotel.mybatis

import com.enihsyou.astolfo.hotel.domain.User
import com.enihsyou.astolfo.hotel.domain.UserRole
import org.apache.ibatis.annotations.*

@Mapper
interface UserMapper {
  @Insert("INSERT INTO registered_user (phone_number, password) VALUES (#{phone_number}, #{password})")
  @Options(useGeneratedKeys = true)
  fun signUp(@Param("phone_number") phone_number: String, @Param("password") password: String)

  @Insert("INSERT INTO registered_user (phone_number, password) VALUES (#{phone_number}, SHA2(#{password}, 256))")
  @Options(useGeneratedKeys = true)
  fun signUpHash(@Param("phone_number") phone_number: String, @Param("password") password: String)


  @Results(
      Result(property = "phone_number", column = "phone_number", javaType = Long::class),
      Result(property = "user_role", column = "user_role", javaType = UserRole::class)
  )
  @Select("SELECT phone_number, nickname, password, register_date, role FROM registered_user JOIN user_role ON registered_user.user_role = user_role.id WHERE phone_number = #{phone_number}")
  fun findByPhone(phone_number: String): User?
}
