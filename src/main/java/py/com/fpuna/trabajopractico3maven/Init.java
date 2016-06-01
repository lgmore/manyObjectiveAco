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
    public static final int CANTIDAD_INDIVIDUOS = 100;
    //4 objetivos public static final int CANTIDAD_ITERACIONES = 700;
    public static final int CANTIDAD_ITERACIONES = 2000;
    //public static final Double RADIO_FITNESS_SHARING = 10.0;
    public static final Double RADIO_FITNESS_SHARING = 0.05;//2.0;

    public static Individuo mejorGlobal = null;

    static final Logger log = LogManager.getLogger(Init.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        log.info("+*****comienza proceso*****+");

        ArrayList<Individuo> individuos = GA.inicializarIndividuos(CANTIDAD_INDIVIDUOS);

//        for (int i = 0; i < CANTIDAD_INDIVIDUOS; i++) {
//
//            Particula particula = new Particula(ciudadesIniciales);
//            particulas.add(particula);
//
//        }//ya tengo mis particulas, tengo que evaluar soluciones
        //boolean primeravez = true;
        ArrayList<Individuo> poblacionPareto = new ArrayList<>();
        for (int j = 0; j < CANTIDAD_ITERACIONES; j++) {
            log.info("----------------------");
            log.info("comienza iteracion: " + (j + 1));

            List<Future<Individuo>> listaFutures = new ArrayList<>();
            ExecutorService executor = Executors.newFixedThreadPool(CANTIDAD_INDIVIDUOS);
            MonitorHilos monitor = new MonitorHilos(executor, 10);
            Thread monitorThread = new Thread(monitor);
            monitorThread.start();
            for (int i = 0; i < CANTIDAD_INDIVIDUOS; i++) {

                Callable<Individuo> worker = individuos.get(i);

                Future<Individuo> submit = executor.submit(worker);
                listaFutures.add(submit);

            }
            // This will make the executor accept no new threads
            // and finish all existing threads in the queue
            executor.shutdown();
            monitor.shutdown();
            // Wait until all threads are finish
            while (!executor.isTerminated()) {
            }

            individuos.clear();
            for (Future<Individuo> future : listaFutures) {
                try {
                    individuos.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            if (j + 1 == CANTIDAD_ITERACIONES) {
                break;
            }

            //individuos.addAll(poblacionPareto);
            ArrayList<Individuo> individuosBackup = new ArrayList<>();
            //individuosBackup.addAll(individuos);
            log.info("***cant individuos de la iteracion " + individuos.size() + "***");
            poblacionPareto.addAll(GA.getIndividuosNoDominados(individuos));
            poblacionPareto = GA.getIndividuosNoDominados(poblacionPareto);
            //verificar que no se empiecen a dominar entre ellos

            for (Individuo elemento : poblacionPareto) {

                log.debug("cromosomas: " + Arrays.toString(elemento.cromosomas.toArray()));
                log.debug("resultado: " + Arrays.toString(elemento.resultadoSolucionActual.toArray()));

            }
            //individuos.addAll(individuosBackup);

            ArrayList<Individuo> poblacionIntermedia = new ArrayList<>();

            poblacionPareto = GA.calcularFitnessSharing(poblacionPareto, RADIO_FITNESS_SHARING);

            poblacionPareto = GA.calcularFDA(poblacionPareto);

            //una vez que se recuperaron todas las hormigas, se debe actualizar  
            //la tabla de feromonas, y a partir de ahi volver a ejecutar
            //GA.actualizarFeromonas(colonia);
            //printMatrizFeromonas();
            //GA.calcularFDA(individuos);
            //se generan nuevos individuos a partir de la poblacion vieja
            //ArrayList<Individuo> nuevaPoblacion = GA.generarNuevosIndividuos(individuos);
            poblacionIntermedia = new ArrayList<>();
            poblacionIntermedia.addAll(GA.realizarCrossover(poblacionPareto));

//            for (Individuo i : poblacionIntermedia) {
//
//                if (i.resultadoSolucionActual == null) {
//
//                    log.error("ndi");
//
//                }
//
//            }
            poblacionIntermedia = GA.realizarMutacion(poblacionIntermedia);

            if (j % 500 == 0) {
                for (Individuo elemento : poblacionPareto) {
                    log.info("cromosomas: " + Arrays.toString(elemento.cromosomas.toArray()));
                    log.info("resultado: " + Arrays.toString(elemento.resultadoSolucionActual.toArray()));
                }
                log.info("continuamos");
            }

            individuos = new ArrayList<>();
            individuos.addAll(poblacionIntermedia);

        }
        log.info("***frente pareto encontrado***");

        for (Individuo elemento : poblacionPareto) {

            log.info("cromosomas: " + Arrays.toString(elemento.cromosomas.toArray()));
            log.info("resultado: " + Arrays.toString(elemento.resultadoSolucionActual.toArray()));

        }

//        log.info(Arrays.toString(mejorGlobal.getSolucionActual().toArray()));
//        log.info("fitness: " + (mejorGlobal.getFitnessSolucionActual()));
    }

//    private static void printMatrizFeromonas() {
//
//        log.info("matriz feromonas: ");
//        for (int fila = 0; fila < CANTIDAD_CIUDADES; fila++) {
//            StringBuilder strb = new StringBuilder();
//            for (int columna = 0; columna < CANTIDAD_CIUDADES; columna++) {
//
//                strb.append(GA.matrizFeromonas[fila][columna]).append(" ");
//
//            }
//            log.info(strb.toString());
//
//        }
//
//    }
}
