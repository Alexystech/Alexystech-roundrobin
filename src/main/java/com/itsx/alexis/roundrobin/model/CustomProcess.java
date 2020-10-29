package com.itsx.alexis.roundrobin.model;

public class CustomProcess {

    private int idProcess;
    private String nameProcess;
    private int rafagaCPU;

    public CustomProcess() {}

    public CustomProcess(int idProcess, String nameProcess, int rafagaCPU) {
        super();
        this.idProcess = idProcess;
        this.nameProcess = nameProcess;
        this.rafagaCPU = rafagaCPU;
    }

    public int getIdProcess() {
        return idProcess;
    }

    public void setIdProcess(int idProcess) {
        this.idProcess = idProcess;
    }

    public String getNameProcess() {
        return nameProcess;
    }

    public void setNameProcess(String nameProcess) {
        this.nameProcess = nameProcess;
    }

    public int getRafagaCPU() {
        return rafagaCPU;
    }

    public void setRafagaCPU(int rafagaCPU) {
        this.rafagaCPU = rafagaCPU;
    }
}
