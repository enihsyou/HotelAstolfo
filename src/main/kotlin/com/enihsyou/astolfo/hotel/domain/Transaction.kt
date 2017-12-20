package com.enihsyou.astolfo.hotel.domain

import org.hibernate.validator.constraints.NotBlank
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "transaction")
data class Transaction(
    @Id
    var id: Int,

    @ManyToOne
    @JoinColumn(name = "user_transaction")
    var user: User,

    @ManyToOne
    @JoinColumn(name = "room_transaction")
    var room: Room,

    @OneToMany
    @JoinColumn(name = "transaction_guest")
    var guests: List<Guest>,

    /*预定开始日期*/
    var date_from: LocalDateTime,

    /*预定结束日期*/
    var date_to: LocalDateTime,

    /*订单没取消*/
    var activated: Boolean,

    /*旅客是否来入住了*/
    var used: Boolean
)
