package com.enihsyou.astolfo.hotel.mybatis

import com.enihsyou.astolfo.hotel.domain.User
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
  @Insert("INSERT INTO registered_user (phone_number, password) VALUES (#{phoneNumber}, SHA2(#{password}, 256))")
  fun signUp(@Param("phoneNumber") phoneNumber: String, @Param("password") password: String)

  @Select("SELECT * FROM registered_user WHERE phone_number = #{phoneNumber}")
  fun findByPhone(phoneNumber: String): User
}
