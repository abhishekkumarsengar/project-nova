package com.project.nova.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.properties")
public class AppContext {

    @Value("${spring.datasource.username}")
    private String postgresUserName;

    @Value("${spring.datasource.password}")
    private String postgresPassword;

    @Value("${spring.datasource.platform}")
    private String postgresDatabase;
}
