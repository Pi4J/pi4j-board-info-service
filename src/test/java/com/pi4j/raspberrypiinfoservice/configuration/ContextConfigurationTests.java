package com.pi4j.raspberrypiinfoservice.configuration;

import com.pi4j.context.Context;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

public class ContextConfigurationTests {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(AutoConfigurations.of(TestConfiguration.class));

    @Test
    void whenOsArchIsNotExpected_verifyContextDoesNotExist() {

        this.contextRunner
                .withPropertyValues("os.arch=other")
                .run(context -> assertThat(context).doesNotHaveBean(Context.class));
    }

    @Test
    void whenOsArchIsArmv6l_verifyContextExists() {

        this.contextRunner
                .withPropertyValues("os.arch=armv6l")
                .run(context -> assertThat(context).hasSingleBean(Context.class));
    }

    @Test
    void whenOsArchIsArmv7l_verifyContextExists() {

        this.contextRunner
                .withPropertyValues("os.arch=armv7l")
                .run(context -> assertThat(context).hasSingleBean(Context.class));
    }

    /*
     * aarch64 will also match Apple M processors
     */
    @Test
    void whenOsArchIsAarch64_verifyContextExists() {

        this.contextRunner
                .withPropertyValues("os.arch=aarch64")
                .run(context -> assertThat(context).hasSingleBean(Context.class));
    }

    @Configuration
    @Import(ContextConfiguration.class)
    static class TestConfiguration {

    }

}
