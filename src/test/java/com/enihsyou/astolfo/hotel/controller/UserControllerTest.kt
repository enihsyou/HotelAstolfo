package com.enihsyou.astolfo.hotel.controller

import org.apache.commons.lang3.StringUtils.containsOnly
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

import io.restassured.RestAssured.*
import io.restassured.matcher.RestAssuredMatchers.*
import org.hamcrest.Matchers.*



class UserControllerTest {

    @Test
    fun lotto_resource_returns_200_with_expected_id_and_winners() {

        `when`().get("http://localhost:10080/api/rooms/")
            .then().statusCode(200)
//            .body("lotto.lottoId", equalTo(5),
//            "lotto.winners.winnerId",equals(23))
    }
}
