package com.enihsyou.astolfo.hotel

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class HotelAstolfoApplication

fun main(args: Array<String>) {
    SpringApplication.run(HotelAstolfoApplication::class.java, *args)
}
