package com.wintam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAsync // <-- 2. Añadir esta anotación aquí
public class WintamApplication {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("admin1234"));

        SpringApplication.run(WintamApplication.class, args);
    }
}