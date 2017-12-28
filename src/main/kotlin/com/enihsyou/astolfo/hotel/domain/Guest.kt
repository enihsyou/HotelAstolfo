package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "GUEST")
data class Guest(
    @Id
    @GeneratedValue
    var id: Int = 0,

    @JsonIgnore
    @ManyToMany(mappedBy = "guests")
    var user: MutableList<User> = mutableListOf(),

    /*身份证号码*/
    var identification: String = "",

    /*姓名*/
    var name: String = "",

    @JsonIgnore
    @ManyToMany(mappedBy = "guests")
    var transactions: MutableList<Transaction> = mutableListOf()
)
