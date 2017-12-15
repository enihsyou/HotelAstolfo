package com.enihsyou.astolfo.hotel.domain


import com.sun.javafx.beans.IDProperty
import javax.persistence.*


enum class UserRole {
  管理员, 前台, 注册用户
}


/*用户类*/
////@Entity
////@Table(name = "user")
//class User {
//  //  @Id
//  private val id: Long = 0
//
//  //  @NotBlank
//  var phone_number: Long = 0
//
//  var nickname: String? = null
//
//  //  @NotBlank
////  @JsonIgnoreProperties
//  var password: String = ""
//
//  //  @NotBlank
//  var user_role: UserRole = UserRole.注册用户
//
//
//  //  @Column(nullable = false, updatable = false)
////  @Temporal(TemporalType.TIMESTAMP)
////  @CreatedDate
//  private val createdAt: Date? = null
//
//
//  //  @Column(nullable = false)
////  @Temporal(TemporalType.TIMESTAMP)
////  @LastModifiedDate
//  private val updatedAt: Date? = null
//}


@Entity
@Table(name = "user")
data class User(
    @Id
    @PrimaryKeyJoinColumn
    @Column(name = "phone_number")
    var phone_number: Long = 0L,
    @Column(name = "nick_name")
    var nick_name: String = "",
    @Column(name = "password")
    var password: String = ""
)
