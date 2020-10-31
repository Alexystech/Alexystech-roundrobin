package com.itsx.alexis.roundrobin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Esta clase {@code BeanProvider} manda un pool de threads a la
 * aplicación.
 */
@Component
public class BeanProvider {

    /**
     * Este metdo {@code executorService} el cual retorna un
     * {@link ExecutorService} sirve para mandar la cantidad de cores
     * disponibles en el equipo gracias al {@link Runtime} podemos
     * obtener los cores disponibles con el metodo {@code avaibleProcessors}
     *
     * cabe decir que trabaja con el patron de diseño Singleton, el cual
     * no permite que existan mas de una instancia.
     * @return
     */
    @Bean
    @Scope("singleton")
    public ExecutorService executorService() {
        return Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors());
    }
}
