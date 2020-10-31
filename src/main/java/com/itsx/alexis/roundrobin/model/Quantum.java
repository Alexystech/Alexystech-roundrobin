package com.itsx.alexis.roundrobin.model;

/**
 * Esta clase {@code Quantum} es el modelo del quantum el cual
 * sera utilizado en el {@link com.itsx.alexis.roundrobin.controller.MainController}
 * para hacer las operaciones correspondientes al algoritmo
 * round robin.
 */
public class Quantum {
    private int numberQuantum;

    public Quantum() {}

    /**
     * Constructor.
     * @param numberQuantum
     */
    public Quantum(int numberQuantum) {
        super();
        this.numberQuantum = numberQuantum;
    }

    /**
     * Getter.
     * @return
     */
    public int getNumberQuantum() {
        return numberQuantum;
    }

    /**
     * Setter.
     * @param numberQuantum
     */
    public void setNumberQuantum(int numberQuantum) {
        this.numberQuantum = numberQuantum;
    }
}
