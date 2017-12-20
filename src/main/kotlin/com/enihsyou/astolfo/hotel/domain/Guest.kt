package com.enihsyou.astolfo.hotel.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Guest(
    @Id
    var id: Int,

    var identification_number: String,

    var name: String
)
