package com.enihsyou.astolfo.hotel.controller

import io.restassured.RestAssured.given
import org.hamcrest.Matchers
import org.junit.Test


class UserControllerTest {

//    @Test
//    fun lotto_resource_returns_200_with_expected_id_and_winners() {
//
//        `when`().get("http://localhost:10080/api/rooms/")
//            .then().statusCode(200)
//            .body("lotto.lottoId", equalTo(5),
//            "lotto.winners.winnerId",equals(23))
//    }

    @Test
    fun test() {
        val bodyString = "{\n  \"phoneNumber\": \"18800000001\",\n  \"password\": \"2333\",\n  \"nickname\": \"昵称\"\n}"
        given().contentType("application/json").body(bodyString).
                `when`().post("https://enihsyou.synology.me:8899/api/users/make")
                .then()
                .also { it.log().all() }
                .assertThat()
                .body("id", Matchers.equalTo(32))
                .body("phoneNumber", Matchers.equalTo("18800000001"))
                .body("role", Matchers.equalTo("注册用户"))
                .statusCode(201)
    }


}
