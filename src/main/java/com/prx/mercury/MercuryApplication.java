package com.prx.mercury;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
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
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {
        "com.prx.mercury",
        "com.prx.commons.properties",
        "com.prx.security"
})
public class MercuryApplication {
    public static void main(String[] args) {
        SpringApplication.run(MercuryApplication.class, args);
    }
}
