package com.enihsyou.astolfo.hotel.domain

import org.hibernate.validator.constraints.NotBlank
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "TRANSACTION")
data class Transaction(
    @Id @GeneratedValue
    var id: Int,

    @ManyToOne
    /*这个用户创建的订单*/
    var user: User,

    @ManyToOne
    /*预定的这个房间*/  //现在只支持单个房间
    var room: Room,

    @OneToMany
    /*入住这些旅客*/
    var guests: List<Guest>,

    /*预定开始日期*/
    var date_from: LocalDateTime,

    /*预定结束日期*/
    var date_to: LocalDateTime,

    /*订单还在有效期*/
    var activated: Boolean,

    /*旅客是否来入住了*/
    var used: Boolean
)
