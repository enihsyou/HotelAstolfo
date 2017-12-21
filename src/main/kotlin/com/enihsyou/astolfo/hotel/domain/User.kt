package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSetter
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import java.time.LocalDateTime
import javax.persistence.AttributeConverter
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table


@Entity
@Table(name = "USER")
data class User(
    @Id
    @GeneratedValue
    var id: Int = 0,

    @NaturalId
    var phone_number: String = "",

    var nickname: String = "",

    var password: String = "",

    @CreationTimestamp
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
    val register_date: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.ORDINAL)
    @ManyToOne(targetEntity = UserRoleTable::class)
    @Convert(converter = UserRoleConverter::class)
    var role: UserRole = UserRole.未注册,

    @OneToMany
    var guests: List<Guest> = emptyList()
) {

    enum class UserRole {
        管理员, 前台, 注册用户, 未注册
    }

    class UserRoleConverter : AttributeConverter<UserRole, String> {
        override fun convertToEntityAttribute(dbData: String?): UserRole {
            return dbData?.let { UserRole.valueOf(dbData) } ?: UserRole.未注册
        }

        override fun convertToDatabaseColumn(attribute: UserRole?): String {
            return attribute.toString()
        }
    }
}


@Entity
@Table(name = "USER_ROLE")
data class UserRoleTable(
    @Id
    @GeneratedValue
    var id: Int = 3,

    var type: String = "未注册",

    @OneToMany
    var users:List<User>

    //todo add user role permissions
)
