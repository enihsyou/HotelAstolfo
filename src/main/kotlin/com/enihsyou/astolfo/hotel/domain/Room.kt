package com.enihsyou.astolfo.hotel.domain

import org.hibernate.annotations.NaturalId
import javax.persistence.AttributeConverter
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "ROOM")
data class Room(
    @Id @GeneratedValue
    var id: Int = 0,

    @NaturalId
    @Embedded
    @Column(name = "number", unique = true, nullable = false)
    var roomNumber: RoomNumber,

    @Enumerated
    @Convert(converter = RoomTypeConverter::class)
    @ManyToOne(targetEntity = RoomTypeTable::class)
    var type: RoomType = RoomType.Undefined,

    @Enumerated
    @Convert(converter = RoomDirectionConverter::class)
    @ManyToOne(targetEntity = RoomDirectionTable::class)
    var direction: RoomDirection = RoomDirection.Undefined,

    /*简介信息*/
    var specialty: String = "",

    /*单独房间的价格*/
    var price: Int = 0,

    @OneToMany
    var transactions: MutableList<Transaction>? = null
) {

    enum class RoomType {
        大床房, 单人间, 双人间, 总统套房, Undefined, Any
    }

    enum class RoomDirection {
        东, 西, 南, 北, Undefined, Any
    }

    @Embeddable
    data class RoomNumber(
        /*楼层*/
        var floor: Int,
        /*当前楼层的房号*/
        var number: Int
    )

    class RoomTypeConverter : AttributeConverter<RoomType, String> {

        override fun convertToEntityAttribute(dbData: String?): RoomType? {
            return dbData?.let { RoomType.valueOf(dbData) } ?: RoomType.Undefined
        }

        override fun convertToDatabaseColumn(attribute: RoomType?): String? {
            return attribute.toString()
        }
    }

    class RoomDirectionConverter : AttributeConverter<RoomDirection, String> {

        override fun convertToEntityAttribute(dbData: String?): RoomDirection? {
            return dbData?.let { RoomDirection.valueOf(dbData) } ?: RoomDirection.Undefined
        }

        override fun convertToDatabaseColumn(attribute: RoomDirection?): String? {
            return attribute.toString()
        }
    }
}

@Entity
@Table(name = "ROOM_TYPE")
data class RoomTypeTable(
    @Id @GeneratedValue
    var id: Int = 1,

    /*类型名字*/
    var type: String = "Undefined",

    /*房型简介*/
    var description:String = "",

    @OneToMany
    var room:List<Room>
)

@Entity
@Table(name = "ROOM_DIRECTION")
data class RoomDirectionTable(
    @Id @GeneratedValue
    var id: Int = 1,

    /*方向名字*/
    var type: String = "Undefined",

    /*方向简介*/
    var description: String = "",

    @OneToMany
    var room: List<Room>
)
