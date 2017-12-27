package com.enihsyou.astolfo.hotel.domain

import afu.org.checkerframework.checker.igj.qual.Mutable
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedBy
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
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
