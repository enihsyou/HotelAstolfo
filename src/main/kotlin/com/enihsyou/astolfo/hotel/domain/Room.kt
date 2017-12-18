package com.enihsyou.astolfo.hotel.domain

import org.hibernate.annotations.NaturalId
import javax.persistence.AttributeConverter
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Converter
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
    @Column(name = "room_number", unique = true, nullable = false)
    var roomNumber: RoomNumber,

    @Enumerated
    @Convert(converter = RoomTypeConverter::class)
    @Column(name = "room_type")
    var type: RoomType = RoomType.单人间,

    @Enumerated
    var direction: String = "",

    var specialty: String = "",

    @OneToMany
    @JoinColumn(name = "room_book")
    var book_transactions: MutableList<BookTransaction>? = null
) {

    enum class RoomType {
        大床房, 单人间, 双人间, 总统套房
    }

    @Embeddable
    data class RoomNumber(
        var floor: Int,
        var number: Int
    )

    @Converter
    class RoomTypeConverter : AttributeConverter<RoomType, String> {

        override fun convertToEntityAttribute(dbData: String?): RoomType? {
            return dbData?.let { RoomType.valueOf(dbData) } ?: RoomType.单人间
        }

        override fun convertToDatabaseColumn(attribute: RoomType?): String? {
            return attribute.toString()
        }
    }

    enum class RoomDirection {
        东, 西, 南, 北
    }
}

@Entity
data class RoomTypeTable(
    @Id
    var type_id: Int = 1,

    var type: String = "单人间"
)
