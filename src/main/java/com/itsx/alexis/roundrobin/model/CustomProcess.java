package com.itsx.alexis.roundrobin.model;

/**
 * Esta calse {@code CustomProcess} es el modelo para nuestros
 * procesos. Cabe destacar que cuenta con tres atributos,
 * {@code idProcess}, {@code nameProcess} y {@code rafagaCPU}.
 */
public class CustomProcess {

    private int idProcess;
    private String nameProcess;
    private int rafagaCPU;

    /**
     * Constructor.
     */
    public CustomProcess() {}

    /**
     * Contructor.
     * @param idProcess
     * @param nameProcess
     * @param rafagaCPU
     */
    public CustomProcess(int idProcess, String nameProcess, int rafagaCPU) {
        super();
        this.idProcess = idProcess;
        this.nameProcess = nameProcess;
        this.rafagaCPU = rafagaCPU;
    }

    /**
     * Getter.
     * @return
     */
    public int getIdProcess() {
        return idProcess;
    }

    /**
     * Setter.
     * @param idProcess
     */
    public void setIdProcess(int idProcess) {
        this.idProcess = idProcess;
    }

    /**
     * Getter.
     * @return
     */
    public String getNameProcess() {
        return nameProcess;
    }

    /**
     * Setter.
     * @param nameProcess
     */
    public void setNameProcess(String nameProcess) {
        this.nameProcess = nameProcess;
    }

    /**
     * Getter.
     * @return
     */
    public int getRafagaCPU() {
        return rafagaCPU;
    }

    /**
     * Setter.
     * @param rafagaCPU
     */
    public void setRafagaCPU(int rafagaCPU) {
        this.rafagaCPU = rafagaCPU;
    }
}
