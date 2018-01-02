package com.enihsyou.astolfo.hotel.controller

import io.restassured.RestAssured.given
import org.junit.Test


class EmployeeControllerTest internal constructor() {



    @Test
    fun signin_with_employee_returns_201_with_expected_PhoneNumber_and_nickname() {
        val bodyString = "{\n  \"phoneNumber\": \"18019002573\",\n  \"password\": \"2333\",\n  \"nickname\": \"我是前台\"\n}"
        given().contentType("application/json").body(bodyString).
                `when`().post("https://astolfo.20001.me:443/api/users/make")
                .then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(201)
    }

    @Test
    fun delete_employee_returns_200_if_success() {
        val postString = "https://astolfo.20001.me:443/api/users?phone=18019002573"
        given().delete(postString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }
}
