package com.enihsyou.astolfo.hotel.domain

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import java.time.LocalDateTime
import javax.persistence.*


enum class UserRole {
    管理员, 前台, 注册用户, 未注册
}


@Entity
@Table(name = "REGISTERED_USER")
data class User(
        @Id
        @Column(name = "phone_number")
        var phone_number: String = "",

        @Column(name = "nickname")
        var nickname: String = "",

        @Column(name = "password")
        var password: String = "",

        @Column(name = "register_date")
        @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
        val register_data: LocalDateTime = LocalDateTime.MIN,

        @OneToOne(targetEntity = UserRoleTable::class)
        @JoinColumn(name = "role")
        @Enumerated(EnumType.STRING)
        @Convert(converter = UserRoleConverter::class)
        var role: UserRoleTable? = null
) {

    class UserRoleConverter : AttributeConverter<UserRole, String> {
        override fun convertToEntityAttribute(dbData: String?): UserRole {
            return dbData?.let { UserRole.valueOf(dbData) } ?: UserRole.未注册
        }

        override fun convertToDatabaseColumn(attribute: UserRole?): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}


@Entity
@Table(name = "USER_ROLE")
data class UserRoleTable(
        @Id
        var id: Long = 0,

        var type: String = "未注册"
)
