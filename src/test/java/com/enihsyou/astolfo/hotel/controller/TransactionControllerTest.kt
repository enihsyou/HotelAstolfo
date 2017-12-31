package com.enihsyou.astolfo.hotel.controller

import io.restassured.RestAssured.given
import org.junit.Test


class TransactionControllerTest internal constructor() {


    @Test
    fun update_user_information_returns_200_if_success() {
        val bodyString = "{\n  \"type\": \"大床房\",\n  \"direction\": \"东\",\n  \"specialty\": \"一个介绍\",\n  \"price\": \"143\"\n}"
        given().contentType("application/json").body(bodyString).
                `when`().post("https://enihsyou.synology.me:8899/api/rooms?floor=1&number=1")
                .then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }

    @Test
    fun delete_room_returns_200_if_success() {
        val postString = "https://enihsyou.synology.me:8899/api/rooms?floor=1&number=1"
        given().delete(postString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }


    @Test
    fun get_floor_info_returns_200_if_success(){
        val getString = "https://enihsyou.synology.me:8899/api/rooms?floor=1&number=1"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }


    @Test

    fun get_reservation_info_returns_200_if_success(){
        val getString = "https://enihsyou.synology.me:8899/api/transactions/list"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }

    @Test

    fun get_commit_returns_200_if_success(){
        val getString = "https://enihsyou.synology.me:8899/api/comments/room?roomId=8"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }

    @Test
    fun comment_on_transaction_if_success() {
        val bodyString = "{\n  \"body\": \"comment\"\n}"
        given().contentType("application/json").body(bodyString).
                `when`().post("https://enihsyou.synology.me:8899/api/rooms?floor=1&number=1")
                .then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }

}
