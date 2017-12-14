package com.enihsyou.astolfo.hotel.domain

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import java.time.LocalDateTime
import javax.persistence.*


enum class UserRole {
    管理员, 前台, 注册用户, 未注册
}


@Entity
@Table(name = "registered_user")
data class User(
        @Id
        @Column(name = "phone_number")
        var phone_number: Long = 0L,
        @Column(name = "nickname")
        var nickname: String = "",
        @Column(name = "password")
        var password: String = "",
        @Column(name = "register_date")
        @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
        val register_data: LocalDateTime = LocalDateTime.MIN,
//        @OneToOne
//        @JoinColumn(name = "role_id")
//        @Enumerated(EnumType.STRING)
//        @Convert(converter = UserRoleConverter::class)
//        var role: UserRoleTable? = null
        @Column(name="role")
        var role: Int = UserRole.未注册.ordinal


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
