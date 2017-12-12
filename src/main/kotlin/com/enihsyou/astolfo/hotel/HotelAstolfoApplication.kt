package com.enihsyou.astolfo.hotel

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class HotelAstolfoApplication// {
//
//
//  @Bean
//  fun init(repository: UserRepository) = CommandLineRunner {
//    repository.save(User("Jack", "Bauer"))
//    repository.save(User("Chloe", "O'Brian"))
//    repository.save(User("Kim", "Bauer"))
//    repository.save(User("David", "Palmer"))
//    repository.save(User("Michelle", "Dessler"))
//  }
//}

fun main(args: Array<String>) {
  SpringApplication.run(HotelAstolfoApplication::class.java, *args)
}
