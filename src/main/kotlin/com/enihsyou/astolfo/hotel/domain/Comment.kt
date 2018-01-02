package com.enihsyou.astolfo.hotel.domain

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "COMMENT")
data class Comment(
    @Id @GeneratedValue
    var id: Int = 0,

    var body: String = "",

    @CreatedBy
    @ManyToOne
    var user: User = User(),

    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
    var createdDate: LocalDateTime = LocalDateTime.now()
)
