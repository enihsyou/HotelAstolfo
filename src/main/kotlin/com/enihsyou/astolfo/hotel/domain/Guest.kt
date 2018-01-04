package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
@Table(name = "GUEST")
data class Guest(
    @Id
    @GeneratedValue
    var id: Int = 0,

    @JsonIgnore
    @ManyToMany(mappedBy = "guests")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    var user: MutableList<User> = mutableListOf(),

    /*身份证号码*/
    @NaturalId
    var identification: String = "",

    /*姓名*/
    var name: String = "",

    @JsonIgnore
    @ManyToMany(mappedBy = "guests")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    var transactions: MutableList<Transaction> = mutableListOf()
)
