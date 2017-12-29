package com.enihsyou.astolfo.hotel.controller

import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.testng.annotations.Test


class UserControllerTest {

//    @Test
//    fun lotto_resource_returns_200_with_expected_id_and_winners() {
//
//        `when`().get("https://enihsyou.synology.me:8899/api/rooms/")
//            .then().statusCode(200)
//            .body("lotto.lottoId", equalTo(5),
//            "lotto.winners.winnerId",equals(23))
//    }
    @Test
    fun test(){
        val bodyString= "{\"phoneNumber\": \"18800000001\",\"password\": \"2333\",\"nickname\": \"昵称\"}";
        given().contentType("application/json").body(bodyString).
        `when`().post("https://enihsyou.synology.me:8899/api/users/make").then().assertThat()
                .body("id",equalTo(32))
                .body("phoneNumber", equalTo("18800000001"))
                .body("role", equalTo("注册用户"))
                .statusCode(201)
    }


}
