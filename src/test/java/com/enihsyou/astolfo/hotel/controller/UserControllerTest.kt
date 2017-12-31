package com.enihsyou.astolfo.hotel.controller

import io.restassured.RestAssured.given
import org.hamcrest.Matchers
import org.junit.Test
import java.util.*


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

    internal val phoneNumber: String

    init {
        this.phoneNumber = "18019002373"
    }

    val randomPhoneNumber: String
        get() {
            var phoneNumber = "1"
            for (i in 0..9) {
                phoneNumber += Random().nextInt(10)
            }
            return phoneNumber
        }

    @Test
    fun login_returns_409_with_registed_phoneNumber (){
        val bodyString = "{\n  \"phoneNumber\": \"18800000011\",\n  \"password\": \"2333\",\n  \"nickname\": \"昵称\"\n}"
        given().contentType("application/json").body(bodyString).
                `when`().post("https://enihsyou.synology.me:8899/api/users/make")
                .then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(409)
    }



    @Test
    fun login_returns_201_with_expected_phoneNumber_and_nickname(){

        val bodyString = "{\n  \"phoneNumber\": \""+phoneNumber+"\",\n  \"password\": \"2333\",\n  \"nickname\": \"昵称\"\n}"
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
    fun delete_user_returns_200_if_success(){
        val postString= "https://enihsyou.synology.me:8899/api/users?phone=" + phoneNumber;
        given().delete(postString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }


    @Test

    fun login_returns_200_with_correct_username_and_password(){
        val bodyString = "{\n  \"phoneNumber\": \"18800000011\",\n  \"password\": \"2333\"\n}"
        given().contentType("application/json").body(bodyString).
                `when`().post("https://enihsyou.synology.me:8899/api/users/login")
                .then()
                .also { it.log().all() }
                .assertThat()
                .body("phoneNumber",Matchers.equalTo("18800000011"))
                .body("role",Matchers.equalTo("注册用户"))
                .statusCode(200)
    }

    @Test
    fun get_all_user_info_return_200_if_success() {
        val getString = "https://enihsyou.synology.me:8899/api/users/"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }

    @Test
    fun get_particular_user_info_return_200_if_success(){
        val getString = "https://enihsyou.synology.me:8899/api/users/get?phone=18800000001"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }

    @Test
    fun update_user_info_returns_405_if_authorization_failed(){

        val bodyString= "{\n  \"password\": \"nihaoya12345\",\n  \"nickname\": \"new_nick\"\n}";
        given().contentType("application/json").body(bodyString).
                `when`().patch("https://enihsyou.synology.me:8899/api/users/login").then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(405)
    }

    @Test
    fun get_user_phone_info_returns_200_if_success() {
        val getString = "https://enihsyou.synology.me:8899/api/users/transactions?phone=18800000011"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }

    @Test
    fun get_user_identifications_returns_200_if_success(){
        val getString = "https://enihsyou.synology.me:8899/api/users/guests?phone=18800000011"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }



}
