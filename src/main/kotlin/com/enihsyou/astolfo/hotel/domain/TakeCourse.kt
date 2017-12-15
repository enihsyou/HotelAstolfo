package com.enihsyou.astolfo.hotel.domain

import java.io.Serializable


data class TakeCourse (val student_uid :Int,val teacher_uid:Int,val usual_behave_grade : Double,val master_test_grade:Double,val final_term_grade:Double) :Serializable




