package com.data.migration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

//    @Bean(name = "sourceDB")
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource.source")
//    public DataSource dataSource1() {
//        return DataSourceBuilder.create().build();
//    }
//
//
//
//
//    @Bean(name = "destinationDB")
//    @ConfigurationProperties(prefix = "spring.datasource.destination") // application.properteis中对应属性的前缀
//    public DataSource dataSource2() {
//        return DataSourceBuilder.create().build();
//    }

}
