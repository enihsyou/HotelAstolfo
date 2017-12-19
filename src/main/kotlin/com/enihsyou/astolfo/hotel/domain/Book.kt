package com.enihsyou.astolfo.hotel.domain

import org.hibernate.validator.constraints.NotBlank
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

@Entity
data class BookTransaction(
    @Id
    var id: Int,

    @ManyToOne
    @JoinColumn(name = "user_book")
    var user: User,

    @ManyToOne
    @JoinColumn(name = "room_book")
    var room: Room,

    var date_from: LocalDateTime,

    var date_to: LocalDateTime,

    var activated: Boolean
)
