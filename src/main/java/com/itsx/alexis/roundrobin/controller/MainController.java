package com.itsx.alexis.roundrobin.controller;

import com.itsx.alexis.roundrobin.model.CustomProcess;
import com.itsx.alexis.roundrobin.model.Quantum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {

    private ExecutorService executorService;
    private StringBuffer builder = new StringBuffer();
    private List<CustomProcess>listProcess = new LinkedList<>();
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

        /*List<CompletableFuture<String>> allFutureString =
                IntStream.range(0,10)
                .mapToObj(String::valueOf)
                .sequential()
                .map(this::produceFutureString)
                .map((future) -> future.whenComplete((value,throwable)-> builder.append(value)))
                .collect(Collectors.toList());
        allFutureString.forEach((future) -> System.out.println(future.join()));
        model.addAttribute("greetings",builder.toString());*/
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

        if (redundantId(id)) {

        } else {

        }
        return "redirect:/index";
    }

    @PostMapping("/execute/algorithm")
    public String executeAlgorithm(Quantum quantum,Model model) {
        System.out.println(quantum.getNumberQuantum());
        this.quantum = quantum;
        return "redirect:/index";
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

    /*private CompletableFuture<String> produceFutureString(String value) {
        return CompletableFuture.supplyAsync(() -> String.format("%s - %s\n", value,"hello"));
    }*/
}
