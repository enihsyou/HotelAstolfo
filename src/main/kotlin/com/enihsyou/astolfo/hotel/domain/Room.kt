package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.NaturalId
import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table


@Entity
@Table(name = "ROOM")
data class Room(
    @Id @GeneratedValue
    var id: Int = 0,

    @Embedded
    var roomNumber: RoomNumber = RoomNumber(),

    @ManyToOne(targetEntity = RoomType::class)
    var type: RoomType = RoomType(),

    @ManyToOne(targetEntity = RoomDirection::class)
    var direction: RoomDirection = RoomDirection(),

    /*简介信息*/
    var specialty: String = "",

    /*单独房间的价格*/
    var price: Int = 0,

    @OneToMany
    @JsonIgnore
    var transactions: MutableList<Transaction>? = null,

    var broken: Boolean = false
) {

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
    var id: Int=0,

    @NaturalId
    /*类型名字*/
    var type: String = "Undefined",

    /*房型简介*/
    var description: String = ""
){
    constructor(type: String) : this(){
        this.type = type
    }
}

@Entity
@Table(name = "ROOM_DIRECTION")
@Embeddable
data class RoomDirection(
    @Id @GeneratedValue
    var id: Int=0,

    /*方向名字*/
    @NaturalId
    var type: String = "Undefined",

    /*方向简介*/
    var description: String = ""
) {
    constructor(type: String) : this() {
        this.type = type
    }
}
