package com.itsx.alexis.roundrobin.controller;

import com.itsx.alexis.roundrobin.model.CustomProcess;
import com.itsx.alexis.roundrobin.model.Quantum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Controller
public class MainController {

    private ExecutorService executorService;
    private StringBuffer builder = new StringBuffer();
    private List<CustomProcess>listProcess = Collections.synchronizedList(new LinkedList<>());
    private int contadorIndice = 1;
    private int unidadesTiempo = 0;
    private int cambioContexto = 0;
    private Quantum quantum;

    @Autowired
    public MainController (ExecutorService executorService) {
        this.executorService = executorService;
    }

    @GetMapping("/index")
    public String getIndex(Model model) {

        model.addAttribute("process", new CustomProcess());
        model.addAttribute("id",contadorIndice);
        model.addAttribute("listProcess",listProcess);
        model.addAttribute("quantum",new Quantum());
        model.addAttribute("unidadesTiempo",unidadesTiempo);
        model.addAttribute("cambioContexto",cambioContexto);

        return "index";
    }

    @PostMapping("/index/process")
    public String addProcess(CustomProcess process,Model model) {

        if (!redundantId(process.getIdProcess())) {
            contadorIndice++;
            listProcess.add(process);
        } else {
            model.addAttribute("toast");
        }

        model.addAttribute("listProcess",listProcess);
        model.addAttribute("process",new CustomProcess());
        model.addAttribute("newProcess",process);
        model.addAttribute("id",contadorIndice);
        model.addAttribute("quantum",new Quantum());
        model.addAttribute("unidadesTiempo",unidadesTiempo);
        model.addAttribute("cambioContexto",cambioContexto);
        return "index";
    }

    @GetMapping("/remove/process/{id}")
    public String removeProcess(@PathVariable int id, Model model) {
        remove(id);
        return "redirect:/index";
    }

    @PostMapping("/execute/algorithm")
    public String executeAlgorithm(Quantum quantum,Model model) {
        model.addAttribute("cambioContexto",cambioContexto);

        this.quantum = quantum;
        this.unidadesTiempo = getUnidadesTiempo(listProcess);
        CustomThread customThread = new CustomThread(listProcess,quantum);
        customThread.run();
        this.cambioContexto = customThread.getCambioContexto();

        return "redirect:/index";
    }

    private int getUnidadesTiempo(List<CustomProcess>listProcess) {
        int acomulador = 0;
        for (CustomProcess process : listProcess) {
            acomulador += process.getRafagaCPU();
        }
        return acomulador;
    }

    private boolean redundantId(int id) {
        boolean redundant = false;
        for (CustomProcess process : listProcess) {
            if (process.getIdProcess() == id) {
                redundant = true;
            }
        }
        return redundant;
    }

    private void remove(int id) {
        int index=0;
        for (CustomProcess process : listProcess) {
            if (id == process.getIdProcess()) {
                listProcess.remove(index);
                break;
            }
            index++;
        }
    }

    private class CustomThread implements Runnable{
        private List<CustomProcess>listProcess;
        private Quantum quantum;
        private Thread thread;
        private int cambioContexto = 0;

        public CustomThread(List<CustomProcess>listProcess,Quantum quantum) {
            this.listProcess = listProcess;
            this.quantum = quantum;
            thread = new Thread("hilo -");
            thread.start();
        }

        @Override
        public void run() {
            int index=0;
            int preRafaga=0;
            boolean band = true;

            synchronized (listProcess) {
                while (band) {
                    for (CustomProcess process: listProcess) {
                        if ( process.getRafagaCPU() <= 0) {

                        } else if ( process.getRafagaCPU() <= quantum.getNumberQuantum() ) {
                            System.out.println(listProcess.get(index).getNameProcess()+"->"+listProcess.get(index).getRafagaCPU());
                            preRafaga=process.getRafagaCPU();
                            process.setRafagaCPU(0);
                            listProcess.set(index,process);

                            try {
                                Thread.sleep(preRafaga*1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            cambioContexto++;
                            System.out.println(listProcess.get(index).getNameProcess()+"->"+listProcess.get(index).getRafagaCPU());

                        } else {
                            System.out.println(listProcess.get(index).getNameProcess()+"->"+listProcess.get(index).getRafagaCPU());
                            process.setRafagaCPU(process.getRafagaCPU()-quantum.getNumberQuantum());
                            listProcess.set(index,process);

                            try {
                                Thread.sleep(quantum.getNumberQuantum()*1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            cambioContexto++;
                            System.out.println(listProcess.get(index).getNameProcess()+"->"+listProcess.get(index).getRafagaCPU());

                        }
                        index++;
                    }

                    if (isAllZero(listProcess)) {
                        System.out.println(cambioContexto);
                        band=false;
                    }
                    index = 0;
                }
            }
        }

        public int getCambioContexto() {
            return this.cambioContexto;
        }

        private boolean isAllZero(List<CustomProcess>listProcess) {
            boolean ban = true;
            for (CustomProcess process : listProcess) {
                if (process.getRafagaCPU()!=0) {
                    ban = false;
                    break;
                }
            }
            return ban;
        }
    }
}
