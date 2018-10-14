package com.kite.playground.vkgallery.dao;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.kite.playground.vkgallery.app.DBUnitTestConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DBUnitTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(value={"classpath:test-application.properties"})
@ContextConfiguration(classes = DBUnitTestConfiguration.class)
@DataJpaTest
public class AbstractDaoTest {
    @Autowired
    protected TestEntityManager testEntityManager;
}
