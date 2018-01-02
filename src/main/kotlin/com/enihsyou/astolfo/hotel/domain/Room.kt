package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.annotation.CreatedDate
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "ROOM")
data class Room(
    @Id @GeneratedValue
    var id: Int = 0,

    @Embedded
    var roomNumber: RoomNumber = RoomNumber(),

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    var type: RoomType = RoomType(),

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    var direction: RoomDirection = RoomDirection(),

    /*简介信息*/
    var specialty: String = "",

    /*单独房间的价格*/
    var price: Int = 0,

    @OneToMany
    @JsonIgnore
    var transactions: MutableList<Transaction> = mutableListOf(),

    //todo 添加一个新条目，信息的创建时间和失效时间，每次更改信息 创建新的，删除旧的
    var broken: Boolean = false,

    @OneToMany
    var comments: MutableList<Comment> = mutableListOf(),

    @CreatedDate
    var createdDate: LocalDateTime = LocalDateTime.now()
) {
    fun occupied(from: LocalDateTime, to: LocalDateTime)
        =        transactions.filter { it.dateFrom >= from && it.dateTo <= to }
            .any { it.activated || it.used }


    @Embeddable
    data class RoomNumber(
        /*楼层*/
        var floor: Int = 0,

        /*当前楼层的房号*/
        var number: Int = 0
    ) : Serializable
}


@Entity
@Table(name = "ROOM_TYPE")
@Embeddable
data class RoomType(
    @Id @GeneratedValue
    var id: Int = 0,

    @NaturalId
    /*类型名字*/
    var type: String = "Undefined",

    /*房型简介*/
    var description: String = ""

//    @OneToMany(mappedBy = "roomtype")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    var rooms: List<Room> = emptyList()
) {

    constructor(type: String) : this() {
        this.type = type
    }
}

@Entity
@Table(name = "ROOM_DIRECTION")
@Embeddable
data class RoomDirection(
    @Id @GeneratedValue
    var id: Int = 0,

    /*方向名字*/
    @NaturalId
    var type: String = "Undefined",

    /*方向简介*/
    var description: String = ""
//
//    @OneToMany(mappedBy = "room_direction")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    var rooms: List<Room> = emptyList()
) {

    constructor(type: String) : this() {
        this.type = type
    }
}
