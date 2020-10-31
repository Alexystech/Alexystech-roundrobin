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

/**
 * Esta clase {@code MainController} es el controlador de la aplicacion
 * desde el cual se trabajara la interoperabilidad de la aplicacion y esta
 * misma estara fuertemente relacionada con el archivo index.html.
 */
@Controller
public class MainController {

    private ExecutorService executorService;
    private StringBuffer builder = new StringBuffer();
    private List<CustomProcess>listProcess = Collections.synchronizedList(new LinkedList<>());
    private int contadorIndice = 1;
    private int unidadesTiempo = 0;
    private int cambioContexto = 0;
    private Quantum quantum;

    /**
     * Es te controlador recive el {@link ExecutorService} por medio de
     * inyeccion de dependencias con los cores disponibles
     * del equipo para utilizar.
     * @param executorService
     */
    @Autowired
    public MainController (ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Este metodo de peticion de tipo Get, es el primero que actuara cuando
     * se inicie la web app.
     *
     * Se le manda a la vista los siguientes atributos:
     * {@code process}, {@code id}, {@code listProcess}, {@code quantum},
     * {@code unidadesTiempo} y {@code cambioContexto}.
     *
     * Retorna el index que se encuentra en /resources/templates.
     * @param model
     * @return
     */
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

    /**
     * Este metodo de peticion de tipo Post, es el que trabajara cuando
     * se quiera registrar un nuevo proceso a la lista {@code listProces},
     * el cual recive por parametro el metodo que se lleno en el formulario
     * de parte de la vista.
     *
     * Se hace una validacion con el metodo {@code redundantId} el cual
     * me permite saber si el id ingresado por el usuario ya se encuentra
     * registrado.
     *
     * Manda a a la vista los siguientes atributos:
     * {@code listProcess},{@code process},{@code newProcess},{@code id},
     * {@code quantum}, {@code unidadesTiempo},{@code cambioContexto}.
     *
     * Retorna el index que se encuentra en /resources/templates.
     *
     * @param process
     * @param model
     * @return
     */
    @PostMapping("/index/process")
    public String addProcess(CustomProcess process,Model model) {

        if (!redundantId(process.getIdProcess())) {
            contadorIndice++;
            listProcess.add(process);
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

    /**
     * Este metodo de peticion de tipo Get, trabajara cuando el cliente
     * quiera eliminar un proceso de la lista {@code listProcess}.
     *
     * Recive por parametro un {@link PathVariable} id de tipo entero
     * el cual me servira para identificar el proceso que se quiere eliminar,
     * para eso uso el metodo {@code remove}.
     *
     * Al final me redirecciona al index donde trabajara de nuevo el {@code GetMapping}
     * de index.
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/remove/process/{id}")
    public String removeProcess(@PathVariable int id, Model model) {
        remove(id);
        return "redirect:/index";
    }

    /**
     * Este metodo de peticion de tipo Post, trabajara cuando el cliente quiera ejecutar
     * el algoritmo Round Robin y precione el boton que debe aparecer por parte de la vista
     * que debe llamarse "Ejecutar Algoritmo".
     *
     * Recive por parametro el {@link Quantum} que se usara para hacer las operaciones
     * necesarias para el algoritmo.
     *
     * Se almacenan las unidades de tiempo totales con el metodo {@code getUnidadesTiempo}
     * y se le pasa por parametro el {@code listProcess}.
     *
     * Se crea una instancia de la clase anonima {@link CustomThread} y se le pasa por
     * parametro {@code listProcess} y el {@code quantum}.
     *
     * Se almacenan los cambios de contexto calculados despues de ejecutar el algoritmo
     * con el metodo {@code getCambioContexto}.
     *
     * Al final redirecciona al index donde trabajara de nuevo el {@code GetMapping}
     * de index.
     * @param quantum
     * @param model
     * @return
     */
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

    /**
     * Este metodo sirve para obtener el total de unidades de tiempo.
     * @param listProcess
     * @return
     */
    private int getUnidadesTiempo(List<CustomProcess>listProcess) {
        int acomulador = 0;
        for (CustomProcess process : listProcess) {
            acomulador += process.getRafagaCPU();
        }
        return acomulador;
    }

    /**
     * Este metodo sirve para saber si existe redundancia de {@code id} entre los
     * procesos registrados y retorna un boolean.
     * @param id
     * @return
     */
    private boolean redundantId(int id) {
        boolean redundant = false;
        for (CustomProcess process : listProcess) {
            if (process.getIdProcess() == id) {
                redundant = true;
            }
        }
        return redundant;
    }

    /**
     * Este metodo sirve para remover un proceso en especifico gracias al
     * {@code id} que se pasa por parametro.
     * @param id
     */
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

    /**
     * Esta es la clase anonima la cual nos sirve para trabajar con threads.
     * Esta implementa {@link Runnable}.
     */
    private class CustomThread implements Runnable{
        private List<CustomProcess>listProcess;
        private Quantum quantum;
        private Thread thread;
        private int cambioContexto = 0;

        /**
         * Constructor.
         * @param listProcess
         * @param quantum
         */
        public CustomThread(List<CustomProcess>listProcess,Quantum quantum) {
            this.listProcess = listProcess;
            this.quantum = quantum;
            thread = new Thread("hilo -");
            thread.start();
        }

        /**
         * Este metodo es el que hace todo el proceso del algoritmo Round Robin.
         */
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

        /**
         * Este metodo me sirve para obtener los cambios de contexto calculados.
         * @return
         */
        public int getCambioContexto() {
            return this.cambioContexto;
        }

        /**
         * Este metodo sirve para saber si todos los procesos han sido terminados de ejecutar,
         * esto es posible gracias a las rafagas de CPU de cada proceso las cuales seran las
         * que se validaran.
         * @param listProcess
         * @return
         */
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
