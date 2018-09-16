package com.kite.playground.vkgallery.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {
        "com.kite.playground.vkgallery.controller",
        "com.kite.playground.vkgallery.manager"
})
@Import(SecurityConfig.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
