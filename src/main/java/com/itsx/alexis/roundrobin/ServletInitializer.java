package com.itsx.alexis.roundrobin;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Esta clase {@code ServletInitializer} que extiende de {@link SpringBootServletInitializer}
 * es usada como principal cuando se empaqueta el .war y no
 * hayan conflictos con el tomcat embebido
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RoundrobinApplication.class);
    }

}
