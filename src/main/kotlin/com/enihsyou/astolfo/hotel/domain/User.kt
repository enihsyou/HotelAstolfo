package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import java.time.LocalDateTime
import javax.persistence.AttributeConverter
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Converter
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table


@Entity
@Table(name = "REGISTERED_USER")
data class User(
    @Id
    @Column(nullable = false)
    var phone_number: String = "",

    var nickname: String = "",

    @Column(nullable = false)
    @JsonIgnore
    var password: String = "",

    @CreationTimestamp
    @Column(updatable = false)
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
    val register_date: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "role")
    @Enumerated(EnumType.STRING)
    @Convert(converter = UserRoleConverter::class)
    var role: UserRoleTable = UserRoleTable(3),

    @OneToMany
    @JoinColumn(name = "user_guest")
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
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}


@Entity
@Table(name = "USER_ROLE")
data class UserRoleTable(
    @Id
    var role_id: Int = 3,

    var type: String = "未注册"
)
