package com.focuslearning.example.kafka.database;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfig {

   /* @Bean
    public DataSourceInitializer dataSourceInitializer(@Qualifier("datasource")DataSource dataSource){

    }*/
}
