package com.prx.mercury;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * MercuryApplication.
 *
 * @author Luis Antonio Mata
 * @version 1.0.0, 03-05-2022
 * @since 11
 */
@FeignClient
@EnableScheduling
@EnableEurekaClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.prx.mercury", "com.prx.commons.config", "com.prx.commons.properties"})
public class MercuryApplication {
    public static void main(String[] args) {
        SpringApplication.run(MercuryApplication.class, args);
    }
}
