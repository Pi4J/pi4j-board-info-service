package com.pi4j.raspberrypiinfoservice.configuration;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfiguration {

    @Configuration
    @ConditionalOnProperty(prefix = "os", name = "arch", havingValue = "armv6l")
    static class Armv6l {

        @Bean
        Context context() {

            return Pi4J.newAutoContext();
        }

    }

    @Configuration
    @ConditionalOnProperty(prefix = "os", name = "arch", havingValue = "armv7l")
    static class Armv7l {

        @Bean
        Context context() {

            return Pi4J.newAutoContext();
        }

    }

    @Configuration
    @ConditionalOnProperty(prefix = "os", name = "arch", havingValue = "aarch64")
    static class Aarch64 {

        @Bean
        Context context() {

            return Pi4J.newAutoContext();
        }

    }

}
