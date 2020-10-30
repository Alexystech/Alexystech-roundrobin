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

        return "index";
    }

    @PostMapping("/index/process")
    public String addProcess(CustomProcess process,Model model) {
        contadorIndice++;
        listProcess.add(process);
        model.addAttribute("listProcess",listProcess);
        model.addAttribute("process",new CustomProcess());
        model.addAttribute("newProcess",process);
        model.addAttribute("id",contadorIndice);
        model.addAttribute("quantum",new Quantum());
        return "index";
    }

    @GetMapping("/remove/process/{indice}")
    public String removeProcess(@PathVariable int id, Model model) {
        return "redirect:/index";
    }

    @PostMapping("/execute/algorithm")
    public String executeAlgorithm(Quantum quantum,Model model) {
        this.quantum = quantum;

        CustomThread customThread = new CustomThread(listProcess,quantum);
        customThread.run();

        return "redirect:/index";
    }

    private int getUnidadesProceso(List<CustomProcess>listProcess) {
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

    private void dropProcess(int id) {
        
    }

    private class CustomThread implements Runnable{
        List<CustomProcess>listProcess;
        Quantum quantum;
        Thread thread;

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
            List<CustomProcess>copy = listProcess;

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
                            System.out.println(listProcess.get(index).getNameProcess()+"->"+listProcess.get(index).getRafagaCPU());

                        }
                        index++;
                    }

                    if (isAllZero(listProcess)) {
                        band=false;
                    }
                    index = 0;
                }
            }
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
