package com.enihsyou.astolfo.hotel.mybatis

import com.enihsyou.astolfo.hotel.domain.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
  @Select("SELECT * FROM student WHERE student_name = #{username}")
  fun selectByUsername(username: String): User
}
