package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters
import java.time.LocalDateTime
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "TRANSACTION")
data class Transaction(
    @Id @GeneratedValue
    var id: Int = 0,

    @CreatedDate
    @CreationTimestamp
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
    var createDate: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @CreatedBy
    @JsonIgnore
    /*这个用户创建的订单*/
    var user: User = User(),

    @ManyToOne
    /*预定的这个房间*/  //现在只支持单个房间
    var room: Room = Room(broken = false),

    @OneToMany
    /*入住这些旅客*/
    var guests: List<Guest> = emptyList(),

    /*预定开始日期*/
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
    var dateFrom: LocalDateTime = LocalDateTime.now(),

    /*预定结束日期*/
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter::class)
    var dateTo: LocalDateTime = LocalDateTime.MAX,

    /*订单还在有效期*/
    var activated: Boolean = true,

    /*旅客是否来入住了*/
    var used: Boolean = false
)
