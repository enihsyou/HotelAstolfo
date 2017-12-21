package com.enihsyou.astolfo.hotel.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedBy
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "GUEST")
data class Guest(
    @Id
    @GeneratedValue
    var id: Int = 0,

    @ManyToOne
    @CreatedBy
    @JsonIgnore
    var user: User? = null,

    /*身份证号码*/
    var identification: String = "",

    /*姓名*/
    var name: String = ""
)
