package com.enihsyou.astolfo.hotel.domain

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
data class Hotel(
        @Id
        val id: Long,
        val hotel_name: String
)

@Entity
class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: Long = 0

    var firstName: String? = null
    var lastName: String? = null
}

//@RepositoryRestResource(collectionResourceRel = "people", path = "people")
//interface PersonRepository : PagingAndSortingRepository<Hotel, Long> {
//
//  fun findByName(@Param("name") name: String): List<Hotel>
//}
