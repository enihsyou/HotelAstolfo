package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table


@Entity
@Table(name = "ROOM")
data class Room(
    @EmbeddedId
    var roomNumber: RoomNumber,

    @ManyToOne(targetEntity = RoomTypeTable::class)
    @Embedded
    var type: String = "",

    @ManyToOne(targetEntity = RoomDirectionTable::class)
    @Embedded
    var direction: String = "",

    /*简介信息*/
    var specialty: String = "",

    /*单独房间的价格*/
    var price: Int = 0,

    @OneToMany
    var transactions: MutableList<Transaction>? = null
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
data class RoomTypeTable(
    @Id @GeneratedValue
    var id: Int = 1,

    /*类型名字*/
    var type: String = "Undefined",

    /*房型简介*/
    var description: String = "",

    @OneToMany
    @JsonIgnore
    var room: List<Room> = emptyList()
)

@Entity
@Table(name = "ROOM_DIRECTION")
@Embeddable
data class RoomDirectionTable(
    @Id @GeneratedValue
    var id: Int = 1,

    /*方向名字*/
    var type: String = "Undefined",

    /*方向简介*/
    var description: String = "",

    @OneToMany
    var room: List<Room> = emptyList()
)
