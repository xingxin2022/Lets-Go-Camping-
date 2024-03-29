package edu.usc.csci310.project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpringBootAPITest {

    @Autowired
    private ApplicationContext context;

    @Mock
    private SpringBootAPI api;

    @Test
    void contextLoads() {
        api.main(new String[]{});
        assertThat(context).isNotNull();
    }

    @Test
    void redirect() {
        api = new SpringBootAPI();
        assertThat(api.redirect()).isEqualTo("forward:/");
    }
}
