package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
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
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    var user: MutableList<User> = mutableListOf(),

    /*身份证号码*/
    var identification: String = "",

    /*姓名*/
    var name: String = "",

    @JsonIgnore
    @ManyToMany(mappedBy = "guests")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    var transactions: MutableList<Transaction> = mutableListOf()
)
