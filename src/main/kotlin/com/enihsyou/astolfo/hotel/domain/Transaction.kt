package com.enihsyou.astolfo.hotel.domain

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "TRANSACTION")
data class Transaction(
    @Id @GeneratedValue
    var id: Int = 0,

    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
    var createdDate: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @CreatedBy
    /*这个用户创建的订单*/
    var user: User = User(),

    @ManyToOne
    /*预定的这个房间*/  //现在只支持单个房间
    var room: Room = Room(),

    /*入住这些旅客*/
    @ManyToMany
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    var guests: MutableList<Guest> = mutableListOf(),

    /*预定开始日期*/
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
    var dateFrom: LocalDateTime = LocalDateTime.now(),

    /*预定结束日期*/
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
    var dateTo: LocalDateTime = LocalDateTime.now(),

    /*订单还有效*/
    var activated: Boolean = false,

    /*旅客是否来入住了*/
    var used: Boolean = false,

    @OneToOne
    var comment: Comment? = null
)
