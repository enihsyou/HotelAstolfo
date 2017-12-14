package com.enihsyou.astolfo.hotel.domain

import javax.persistence.*


enum class UserRole {
  管理员, 前台, 注册用户, 未注册
}


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
    var password: String = "",
    @OneToOne
    @JoinColumn(name = "role_id")
    @Enumerated(EnumType.STRING)
    @Convert(converter = UserRoleConverter::class)
    var role: UserRoleTable? = null
//    var role:UserRole=UserRole.注册用户


)

class UserRoleConverter : AttributeConverter<UserRole, String> {
  override fun convertToEntityAttribute(dbData: String?): UserRole {
    return dbData?.let { UserRole.valueOf(dbData) } ?: UserRole.未注册
  }

  override fun convertToDatabaseColumn(attribute: UserRole?): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}

@Entity
@Table(name = "user_role")
data class UserRoleTable(
    @Id
    var id: Long,

    var type: String = "未注册"
)
