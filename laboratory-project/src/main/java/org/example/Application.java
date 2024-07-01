package org.example;

import org.example.constant.GlobalConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = GlobalConst.BASE_PACKAGE)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}