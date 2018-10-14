package com.kite.playground.vkgallery.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@SpringBootConfiguration
@ComponentScan(basePackages = "com.kite.playground.vkgallery.dao")
@EnableJpaRepositories(basePackages = "com.kite.playground.vkgallery.dao")
@EntityScan(basePackages = "com.kite.playground.vkgallery.entity")
public class DBUnitTestConfiguration {
    @Autowired
    private DataSource dataSource;

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
