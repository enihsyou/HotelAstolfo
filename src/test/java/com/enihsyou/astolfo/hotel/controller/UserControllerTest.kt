package com.enihsyou.astolfo.hotel.controller

import io.restassured.RestAssured.`when`
import io.restassured.RestAssured.given
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.Test


class UserControllerTest internal constructor(){

//    @Test
//    fun lotto_resource_returns_200_with_expected_id_and_winners() {
//
//        `when`().get("http://localhost:10080/api/rooms/")
//            .then().statusCode(200)
//            .body("lotto.lottoId", equalTo(5),
//            "lotto.winners.winnerId",equals(23))
//    }
//    var path="https://enihsyou.synology.me:8899/api/users/make";

    internal var phoneNumber: String

    init {
        this.phoneNumber = randomPhoneNumber
    }

    val randomPhoneNumber: String
        get() {
            var phoneNumber = "1"
            for (i in 0..9) {
                phoneNumber += Math.random().toInt() * 10
            }
            return phoneNumber
        }

    @Test
    fun login_returns_409_with_registed_phoneNumber (){
        val bodyString = "{\n  \"phoneNumber\": \"18800000001\",\n  \"password\": \"2333\",\n  \"nickname\": \"昵称\"\n}"
        given().contentType("application/json").body(bodyString).
                `when`().post("https://enihsyou.synology.me:8899/api/users/make")
                .then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(409)
    }



    @Test
    fun login_returns_201_wieh_expected_phoneNumber_and_nickname(){

        val bodyString = "{\n  \""+phoneNumber+"\": \"18800000011\",\n  \"password\": \"2333\",\n  \"nickname\": \"昵称\"\n}"
        given().contentType("application/json").body(bodyString).
                `when`().post("https://enihsyou.synology.me:8899/api/users/make")
                .then()
                .also { it.log().all() }
                .assertThat()
                .body("phoneNumber", Matchers.equalTo(phoneNumber))
                .body("role", Matchers.equalTo("注册用户"))
                .statusCode(201)
    }

    @Test
    fun delete_user(){
        val getString="";
        given().delete("https://enihsyou.synology.me:8899/api/users?phone="+phoneNumber).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }


//    @Test


}
