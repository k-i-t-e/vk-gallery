package com.kite.playground.vkgallery.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {
        "com.kite.playground.vkgallery.manager",
        "com.kite.playground.vkgallery.dao"
})
@Import({SecurityConfig.class, MvcConfig.class, AppConfig.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
