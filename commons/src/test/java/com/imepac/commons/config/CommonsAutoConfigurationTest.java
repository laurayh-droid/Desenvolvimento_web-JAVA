package com.imepac.commons.config;

import com.imepac.commons.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class CommonsAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner().withUserConfiguration(CommonsAutoConfiguration.class);

    @Test
    void shouldProvideGlobalExceptionHandlerBean() {
        contextRunner.run(ctx -> assertThat(ctx).hasSingleBean(GlobalExceptionHandler.class));
    }
}
