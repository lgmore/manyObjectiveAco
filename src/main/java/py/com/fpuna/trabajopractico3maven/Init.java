/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.fpuna.trabajopractico3maven;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author lg_more
 */
public class Init {

    //public static final String CIUDAD_PRUEBA = "burma14.dat";
    public static final String CIUDAD_PRUEBA = "berlin52.dat";
    public static final int CANTIDAD_INDIVIDUOS = 50;
    public static final int CANTIDAD_ITERACIONES = 200;
    public static final int CANTIDAD_CIUDADES = 14;

    public static Double alpha = 10.0;

    public static Double beta = 0.005;

    public static Double evaporacion = 0.5;
    // new trail deposit coefficient;
    public static Double Q = 500.0;
    public static Hormiga mejorGlobal = null;
    public static ArrayList<Integer> ciudadesIniciales = new ArrayList<>();
    static final Logger log = LogManager.getLogger(Init.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        log.info("+*****comienza proceso*****+");
        for (int i = 0; i < CANTIDAD_CIUDADES; i++) {

            ciudadesIniciales.add(i);

        }

        Path archivo = FileSystems.getDefault().getPath("ciudades", CIUDAD_PRUEBA);
        ACO.matrizCiudades = ACO.create2DIntMatrixFromFile(archivo);
        ACO.inicializarVisibilidades(CANTIDAD_CIUDADES);
        ACO.inicializarFeromonas(CANTIDAD_CIUDADES);

        ArrayList<Hormiga> colonia = new ArrayList<>();

//        for (int i = 0; i < CANTIDAD_INDIVIDUOS; i++) {
//
//            Particula particula = new Particula(ciudadesIniciales);
//            particulas.add(particula);
//
//        }//ya tengo mis particulas, tengo que evaluar soluciones
        //boolean primeravez = true;
        for (int j = 0; j < CANTIDAD_ITERACIONES; j++) {
            log.info("----------------------");
            log.info("comienza iteracion: " + (j + 1));

            List<Future<Hormiga>> listaFutures = new ArrayList<Future<Hormiga>>();
            ExecutorService executor = Executors.newFixedThreadPool(CANTIDAD_INDIVIDUOS);
            MonitorHilos monitor = new MonitorHilos(executor, 10);
            Thread monitorThread = new Thread(monitor);
            monitorThread.start();
            for (int i = 0; i < CANTIDAD_INDIVIDUOS; i++) {

                Callable<Hormiga> worker;
                if (j == 0) { // estamos en la primera iteracion
                    worker = new Hormiga();
                } else {
                    worker = colonia.get(i);
                }
                Future<Hormiga> submit = executor.submit(worker);
                listaFutures.add(submit);

            }
            // This will make the executor accept no new threads
            // and finish all existing threads in the queue
            executor.shutdown();
            monitor.shutdown();
            // Wait until all threads are finish
            while (!executor.isTerminated()) {
            }

            colonia.clear();
            for (Future<Hormiga> future : listaFutures) {
                try {
                    colonia.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            //una vez que se recuperaron todas las hormigas, se debe actualizar  
            //la tabla de feromonas, y a partir de ahi volver a ejecutar
            ACO.actualizarFeromonas(colonia);
            //printMatrizFeromonas();

            log.info("***soluciones provisorias***");
            for (Hormiga elemento : colonia) {

                if (mejorGlobal == null) {
                    //si todavia no tenemos solucion global, 
                    //asignamos el primer elemento que haya

                    mejorGlobal = elemento;
                } else {//comparacion para obtener el mejor global
                    if (mejorGlobal.getFitnessSolucionActual().compareTo(elemento.getFitnessSolucionActual()) > 0) {
                        mejorGlobal = elemento;
                    }
                }

//                log.info("Sol hormiga: " + Arrays.toString(elemento.getSolucionActual().toArray()));
//                log.info("Fitness solucion " + elemento.getFitnessSolucionActual());

            }
            log.info("----------------------");

        }
        log.info("***mejor global encontrado***");
        log.info(Arrays.toString(mejorGlobal.getSolucionActual().toArray()));
        log.info("fitness: " + (mejorGlobal.getFitnessSolucionActual()));

    }

    private static void printMatrizFeromonas() {

        log.info("matriz feromonas: ");
        for (int fila = 0; fila < CANTIDAD_CIUDADES; fila++) {
            StringBuilder strb = new StringBuilder();
            for (int columna = 0; columna < CANTIDAD_CIUDADES; columna++) {

                strb.append(ACO.matrizFeromonas[fila][columna]).append(" ");

            }
            log.info(strb.toString());

        }

    }
}
