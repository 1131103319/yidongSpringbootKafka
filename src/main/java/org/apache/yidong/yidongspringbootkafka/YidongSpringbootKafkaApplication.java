package org.apache.yidong.yidongspringbootkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YidongSpringbootKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YidongSpringbootKafkaApplication.class, args);
    }
}
