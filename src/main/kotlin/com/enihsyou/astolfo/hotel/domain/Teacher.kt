package com.enihsyou.astolfo.hotel.domain

import java.io.Serializable

data class Teacher(val teacher_uid : Long, val teacher_email : String, val teacher_name :String,val teacher_passwd:String) : Serializable
