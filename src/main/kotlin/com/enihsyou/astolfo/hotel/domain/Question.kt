package com.enihsyou.astolfo.hotel.domain

import java.io.Serializable

data class Question(val question_id :Int,val content:String,val answer : String ) : Serializable
