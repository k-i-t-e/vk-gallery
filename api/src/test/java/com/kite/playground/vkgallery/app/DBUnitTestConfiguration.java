package com.kite.playground.vkgallery.app;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@ComponentScan(basePackages = "com.kite.playground.vkgallery.dao")
@EnableJpaRepositories(basePackages = "com.kite.playground.vkgallery.dao")
@EntityScan(basePackages = "com.kite.playground.vkgallery.entity")
public class DBUnitTestConfiguration {
}
