package com.itsx.alexis.roundrobin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class BeanProvider {

    @Bean
    @Scope("singleton")
    public ExecutorService executorService() {
        return Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors());
    }
}
