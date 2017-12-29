package com.enihsyou.astolfo.hotel.domain

import com.enihsyou.astolfo.hotel.repository.TransactionRepository
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.beans.factory.annotation.Autowired
import java.io.Serializable
import java.time.LocalDateTime
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

    var broken: Boolean = false
) {

    val occupied
        get() = transactions.any {
            val now = LocalDateTime.now()
            it.dateFrom < now && it.dateTo > now
        }

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
