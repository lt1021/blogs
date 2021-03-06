package cn.lt.blog3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
@MapperScan("cn.lt.blog3.mapper")
public class Blog3Application {

    public static void main(String[] args) {
        SpringApplication.run(Blog3Application.class, args);
    }

}
