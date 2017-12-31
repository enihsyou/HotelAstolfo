package com.enihsyou.astolfo.hotel.controller

import io.restassured.RestAssured.given
import org.junit.Test


class RoomControllerTestControllerTest internal constructor() {




    @Test
    fun get_room_init_info_returns_200_if_success() {
        val getString = "https://enihsyou.synology.me:8899/api/rooms/load2"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }

    @Test
    fun get_room_init_info_small_returns_200_if_success(){
        val getString = "https://enihsyou.synology.me:8899/api/rooms/load"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }

    @Test
    fun get_room_type_returns_200_if_success_(){
        val getString = "https://enihsyou.synology.me:8899/api/rooms/types"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }

    @Test
    fun insert_room_type_returns_201_if_success(){
        val postString="https://enihsyou.synology.me:8899/api/rooms/types"
        val bodyString= "{\n  \"type\": \"测试房\",\n  \"description\": \"测试用的房间\"\n}"
        given().contentType("application/json").body(bodyString).
                `when`().post(postString)
                .then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(201)
    }

    @Test
    fun modify_room_description_returns_200_if_success(){
        val patchString="https://enihsyou.synology.me:8899/api/rooms/types?type=测试房"
        val bodyString = "{\n  \"description\": \"修改后的测试房间\"\n}"
        given().contentType("application/json").body(bodyString).
                `when`().patch(patchString).then()
                .also{it.log().all()}
                .assertThat()
                .statusCode(200)
    }




    @Test
    fun get_room_directions_returns_200_if_success(){
        val getString="https://enihsyou.synology.me:8899/api/rooms/directions"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)

    }


    @Test
    fun modify_room_directions_returns_200_if_success() {
        val patchString = "https://enihsyou.synology.me:8899/api/rooms/directions"
        val bodyString = "{\n" +
                "  \"type\": \"东\",\n" +
                "  \"description\": \"冬暖夏朗\"\n" +
                "}"
        given().contentType("application/json").body(bodyString).
                `when`().patch(patchString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(201)
    }

    @Test
    fun delete_room_type_returns_200_if_success() {
        val deleteString = "https://enihsyou.synology.me:8899/api/rooms/types?type=测试房"
        given().delete(deleteString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)

    }
    @Test
    fun get_room_status_returns_200_if_success() {
        val getString = "https://enihsyou.synology.me:8899/api/rooms/"
        given().get(getString).then()
                .also { it.log().all() }
                .assertThat()
                .statusCode(200)
    }




}
