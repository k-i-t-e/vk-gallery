package com.kite.playground.vkgallery.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.kite.playground.vkgallery.manager"
})
@EnableJpaRepositories(basePackages = "com.kite.playground.vkgallery.dao")
@EntityScan(basePackages = "com.kite.playground.vkgallery.entity")
@Import({SecurityConfig.class, MvcConfig.class, AppConfig.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
