package com.enihsyou.astolfo.hotel;

import com.enihsyou.astolfo.hotel.mybatis.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HotelAstolfoApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(HotelAstolfoApplication.class, args);
  }

  final private UserMapper userMapper;

  public HotelAstolfoApplication(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println(this.userMapper.selectByUsername("dfd"));
  }
}
