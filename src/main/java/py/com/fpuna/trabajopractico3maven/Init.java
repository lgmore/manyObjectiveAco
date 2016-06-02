/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.fpuna.trabajopractico3maven;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
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
    public static int CANTIDAD_CIUDADES = 52;
    public static Integer CIUDAD_INICIO = 0;
    public static final int CANTIDAD_INDIVIDUOS = 100;
    //4 objetivos public static final int CANTIDAD_ITERACIONES = 700;
    public static final int CANTIDAD_ITERACIONES = 10000;
    //public static final Double RADIO_FITNESS_SHARING = 10.0;

    public static final String NOMBRE_ARCHIVO = "/home/lg_more/MEGAsync/maestria/AE2016CD/tp2/berlin52.dat";
    public static Hormiga mejorGlobal = null;

    public static ArrayList<ArrayList<Double>> ciudades;
    public static ArrayList<ArrayList<Double>> feromonas;
    public static ArrayList<ArrayList<Double>> visibilidades;

    static final Logger log = LogManager.getLogger(Init.class.getName());
    static Double BETA = 5.0;
    static double ALFA = 1.0;
    private static Double Q = 10.0;
    private static final Double COEFICIENTE_VISIBILIDAD = 1.0;
    static ArrayList<Integer> LISTA_CIUDADES;
    private static final Double COEFICIENTE_EVAPORACION = 0.01;
    private static final Double COEFICIENTE_FEROMONA = 1.0;

    public static ArrayList<ArrayList<Double>> leerArchivo() {
        Scanner in;
        ArrayList<ArrayList<Double>> resultado = new ArrayList<>();

        try {
            Scanner s = new Scanner(new File(NOMBRE_ARCHIVO));
            int elementos = 0;
            int row = 0;
            //int matrix[][] = new int[rows][columns];
            resultado.add(new ArrayList<>());
            while (s.hasNextDouble()) {

                resultado.get(row).add(s.nextDouble());
                elementos++;
                if (elementos >= CANTIDAD_CIUDADES) {

                    elementos = 0;
                    row++;
                    resultado.add(new ArrayList<>());

                }
            }
            s.close();
            // At this point all dead cells are 0 and all live are 1
        } catch (IOException i) {
            System.out.println("Problems..");

        }
        resultado.remove(resultado.size() - 1);
        return resultado;
    }

    public static ArrayList<ArrayList<Double>> inicializarFeromonas() {
        Scanner in;
        ArrayList<ArrayList<Double>> resultado = new ArrayList<>();

        try {
            Scanner s = new Scanner(new File(NOMBRE_ARCHIVO));
            int elementos = 0;
            int row = 0;
            //int matrix[][] = new int[rows][columns];
            resultado.add(new ArrayList<>());
            while (s.hasNextDouble()) {
                s.nextDouble();
                resultado.get(row).add(Init.COEFICIENTE_FEROMONA);
                elementos++;
                if (elementos >= CANTIDAD_CIUDADES) {

                    elementos = 0;
                    row++;
                    resultado.add(new ArrayList<>());

                }
            }
            s.close();
            // At this point all dead cells are 0 and all live are 1
        } catch (IOException i) {
            System.out.println("Problems..");

        }
        resultado.remove(resultado.size() - 1);
        return resultado;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        log.info("+*****comienza proceso*****+");
        initListaCiudades();

        ArrayList<Hormiga> hormigas = new ArrayList<>();

        ciudades = leerArchivo();
        feromonas = inicializarFeromonas();
        visibilidades = inicializarVisibilidades(ciudades);

//        ArrayList<Individuo> poblacionPareto = new ArrayList<>();
        for (int j = 0; j < CANTIDAD_ITERACIONES; j++) {
//            log.info("----------------------");
            log.info("comienza iteracion: " + (j + 1));
//
            List<Future<Hormiga>> listaFutures = new ArrayList<>();
            ExecutorService executor = Executors.newFixedThreadPool(CANTIDAD_INDIVIDUOS);
            MonitorHilos monitor = new MonitorHilos(executor, 10);
            Thread monitorThread = new Thread(monitor);
            monitorThread.start();
            for (int i = 0; i < CANTIDAD_INDIVIDUOS; i++) {
//
                Hormiga indiv = new Hormiga();
//                for (int ii = 0; ii < indiv.cromosomas.size(); ii++) {
////
////                    Double elemento = Precision.round(indiv.cromosomas.get(ii), 3, BigDecimal.ROUND_HALF_UP);
////                    indiv.cromosomas.set(ii, elemento);
////
////                }
                Callable<Hormiga> worker = indiv;
//
                Future<Hormiga> submit = executor.submit(worker);
                listaFutures.add(submit);
//
            }
//            // This will make the executor accept no new threads
//            // and finish all existing threads in the queue
            executor.shutdown();
            monitor.shutdown();
//            // Wait until all threads are finish
            while (!executor.isTerminated()) {
            }
//
            hormigas.clear();
            for (Future<Hormiga> future : listaFutures) {
                try {
                    hormigas.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            actualizarFeromonas(hormigas);

//            log.info("***feromonas***");
//
//            for (ArrayList<Double> elemento : Init.feromonas) {
//
//                StringBuilder tester = new StringBuilder();
//                DecimalFormat df = new DecimalFormat("#.000");
//                for (int jj = 0; jj < elemento.size(); jj++) {
//
//                    tester.append(df.format(elemento.get(jj))).append(" ");
//                }
//                log.debug("" + tester.toString());
//
//            }
            for (Hormiga hormiga : hormigas) {
                log.info("solucion: " + Arrays.asList(hormiga.ciudades.toArray()));
            }
            

            log.debug("seguimos");

        }

        log.info("***soluciones finales***");

        for (Hormiga hormiga : hormigas) {

            log.info("" + Arrays.toString(hormiga.ciudades.toArray()));

        }

//        log.info("lista feromonas");
//        for (ArrayList<Double> elemento : Init.feromonas) {
//
//            log.info("" + Arrays.toString(elemento.toArray()));
//
//        }

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
    }

    private static ArrayList<ArrayList<Double>> inicializarVisibilidades(ArrayList<ArrayList<Double>> ciudades) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        ArrayList<ArrayList<Double>> visibilidades = new ArrayList<>();
        int row = 0;
        for (ArrayList<Double> elemento : ciudades) {

            visibilidades.add(new ArrayList<>());

            for (Double elemento2 : elemento) {

                visibilidades.get(row).add(COEFICIENTE_VISIBILIDAD / elemento2);
            }
            row++;
        }
        return visibilidades;

    }

    private static void actualizarFeromonas(ArrayList<Hormiga> hormigas) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int fila, columna = 0;
        log.debug("hormiga: " + Arrays.asList(hormigas.get(0).ciudades.toArray()));
        for (fila = 0; fila < Init.CANTIDAD_CIUDADES - 1; fila++) {

            for (columna = fila + 1; columna < Init.CANTIDAD_CIUDADES; columna++) {

                Double deltaTau = getDeltaTau(fila, columna, hormigas);

                Double feromona = (1 - Init.COEFICIENTE_EVAPORACION) * Init.feromonas.get(fila).get(columna);

                feromona += deltaTau;

                Init.feromonas.get(fila).set(columna, feromona);
                Init.feromonas.get(columna).set(fila, feromona);

            }
        }
        log.debug("termino de actualizar feromonas");
    }

    private static Double getDeltaTau(Integer ciudadActual, Integer siguienteCiudad, ArrayList<Hormiga> hormigas) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //verificar si la hormiga uso el tour
        Double resultado = 0.0;
        for (Hormiga hormiga : hormigas) {

            Integer indiceCiudadActual = hormiga.ciudades.indexOf(ciudadActual);
            Integer indiceCiudadSiguiente = hormiga.ciudades.indexOf(siguienteCiudad);
            //la hormiga definitivamente recorrio todas las ciudades,
            //ahora, hay que ver si los indices son contiguos, en ese caso,
            //se depositan feromonas

            if (indiceCiudadActual + 1 == indiceCiudadSiguiente
                    || (indiceCiudadActual == hormiga.ciudades.size() - 1
                    && indiceCiudadSiguiente == 0 //puede ser que ciudadActual sea la ultima
                    //ciudad visitada, y ciudad siguiente sea el inicio del tour, en ese caso
                    //son contiguos
                    )) {
                resultado += Q / getCostoTour(hormiga);
            } else {
                //nada
            }

        }

        return resultado;
    }

    private static void initListaCiudades() {
        Init.LISTA_CIUDADES = new ArrayList<>();
        for (int i = 0; i < Init.CANTIDAD_CIUDADES; i++) {

            Init.LISTA_CIUDADES.add(i);

        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static Double getCostoTour(Hormiga hormiga) {

        Double resultado = -1.0;
        Integer ciudadActual = 0;
        Integer ciudadSiguiente = 0;
        for (Integer ciudad : hormiga.ciudades) {

            if (resultado == -1.0) { //primera vez
                ciudadActual = ciudad;
                resultado = 0.0;
                continue;
            }
            ciudadSiguiente = ciudad;
            resultado += Init.ciudades.get(ciudadActual).get(ciudadSiguiente);
            ciudadActual = ciudadSiguiente;

        }
        return resultado;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
