package com.example.springtestdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class SpringTestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTestDemoApplication.class, args);
    }

}
