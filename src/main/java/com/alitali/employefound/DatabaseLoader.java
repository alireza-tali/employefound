package com.alitali.employefound;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DatabaseLoader {
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {
        return args->{

        };
    }
}
