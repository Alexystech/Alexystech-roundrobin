package com.itsx.alexis.roundrobin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Programa desarrollado en spring boot framework bajo el modelo de
 * arquitectura de software MVC.
 *
 * @since JDK8
 * @author Jose Alexis Vazquez Morales
 */
@SpringBootApplication
public class RoundrobinApplication {

    /**
     * Clase {@code main} que es tomada como principal para nuestra
     * webapp.
     *
     * Se usa el metodo {@code run} de la clase {@link SpringApplication}
     * para arrancar la aplicacion.
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(RoundrobinApplication.class, args);
    }

}
