package com.enihsyou.astolfo.hotel.domain

import org.hibernate.annotations.NaturalId
import javax.persistence.AttributeConverter
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

@Entity
data class Room(
    @Id
    var id: Int = 0,

    @NaturalId
    @Embedded
    @Column(name = "number", unique = true, nullable = false)
    var roomNumber: RoomNumber,

    @Enumerated
    @Convert(converter = RoomTypeConverter::class)
    @Column(name = "type")
    var type: RoomType = RoomType.单人间,

    @Enumerated
    @Convert(converter = RoomDirectionConverter::class)
    @Column(name = "direction")
    var direction: RoomDirection = RoomDirection.东,

    var specialty: String = "",

    @OneToMany
    @JoinColumn(name = "room_book")
    var book_transactions: MutableList<BookTransaction>? = null
) {

    enum class RoomType {
        大床房, 单人间, 双人间, 总统套房
    }

    enum class RoomDirection {
        东, 西, 南, 北
    }

    @Embeddable
    data class RoomNumber(
        var floor: Int,
        var number: Int
    )

    class RoomTypeConverter : AttributeConverter<RoomType, String> {

        override fun convertToEntityAttribute(dbData: String?): RoomType? {
            return dbData?.let { RoomType.valueOf(dbData) } ?: RoomType.单人间
        }

        override fun convertToDatabaseColumn(attribute: RoomType?): String? {
            return attribute.toString()
        }
    }

    class RoomDirectionConverter : AttributeConverter<RoomDirection, String> {

        override fun convertToEntityAttribute(dbData: String?): RoomDirection? {
            return dbData?.let { RoomDirection.valueOf(dbData) } ?: RoomDirection.东
        }

        override fun convertToDatabaseColumn(attribute: RoomDirection?): String? {
            return attribute.toString()
        }
    }
}

@Entity
data class RoomTypeTable(
    @Id
    var type_id: Int = 1,

    var type: String = "单人间"
)

