/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.fpuna.trabajopractico3maven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author lg_more
 */
public class Hormiga implements Callable {

    public ArrayList<Integer> ciudades;

    static final Logger log = LogManager.getLogger(Hormiga.class.getName());
    boolean dominado = false; //no dominado hasta que se diga lo contrario

    public Hormiga() {

        //solucionActual = ciudadesIniciales;
        //int cantidadCiudades = ciudadesIniciales.size();
        //Random rn = new Random();
        //Collections.shuffle(solucionActual, rn);
    }
//
//    public static void main(String[] args) {
//    
//        Individuo ind = new Individuo();
//        ind.cromosomas = new ArrayList<>();
//        for (int i=0; i < 20; i++){
//        
//        
//            ind.cromosomas.add(0.5);
//            
//        }
//        ind.evaluarFuncion();
//    }

    private static ArrayList<String> initListaCiudades() {
        ArrayList<String> resultado = new ArrayList<>();
        for (int i = 0; i < Init.CANTIDAD_CIUDADES; i++) {

            resultado.add(String.valueOf(i));

        }
        return resultado;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void doRecorrido() {
        ArrayList<String> ciudadesNoVisitadas = initListaCiudades();
        ArrayList<Double> FDAS = new ArrayList<>();
        ciudades = new ArrayList<>();
        Random rand = new Random();
        //se elige una ciudad para iniciar
        //Integer randomPosCiudad = rand.nextInt((ciudadesNoVisitadas.size()));

        ciudades.add(Integer.valueOf(ciudadesNoVisitadas.get(Init.CIUDAD_INICIO)));
        ciudadesNoVisitadas.remove(Init.CIUDAD_INICIO.intValue());

        while (ciudades.size() < Init.CANTIDAD_CIUDADES) {
            //el recorrido para al tener todas las ciudades
            Double realizoPaso;
            rand = new Random();
            realizoPaso = Math.random();

            FDAS = getFDAS(ciudades.get(ciudades.size() - 1), ciudadesNoVisitadas);
            int contador = 0;
            Integer siguienteCiudad=0;
            for (Double fda : FDAS) {
            
                siguienteCiudad = Integer.valueOf(ciudadesNoVisitadas.get(contador));
                if (realizoPaso < fda){
                    //quiere decir que saltamos a esta ciudad
                    break;
                }
                contador++;
            
            }

            ciudades.add(siguienteCiudad);
            ciudadesNoVisitadas.remove(contador);
        }

        ciudades.add(ciudades.get(0));//termina el tour al principio

        log.debug("solucion: ");
    }

    @Override
    public Hormiga call() throws Exception {
        this.doRecorrido();
        return this;
    }

    private Double getProbabilidadPaso(Integer ciudadActual, Integer ciudadPaso, Hormiga hormiga) {

        Double resultado = 0.0;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Double interm1 = Math.pow(Init.visibilidades.get(ciudadActual).get(ciudadPaso), Init.BETA);
        Double interm2 = Math.pow(Init.feromonas.get(ciudadActual).get(ciudadPaso), Init.ALFA);
        resultado = interm1 * interm2;
        //numerador

        Double interm3 = getSumatoriaOtrasVisibilidadesPosibles(Init.visibilidades, Init.feromonas, hormiga, ciudadActual, ciudadPaso);

        resultado /= interm3;
        //denominador
        log.debug("prob paso: " + resultado);
        return resultado;

    }

    private double getSumatoriaOtrasVisibilidadesPosibles(ArrayList<ArrayList<Double>> visibilidades, ArrayList<ArrayList<Double>> feromonas, Hormiga hormiga, Integer ciudadActual, Integer ciudadSiguiente) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        Double resultado = 0.0;
        Integer contadorCiclos = 0;
        for (Integer elemento : Init.LISTA_CIUDADES) {

            if (elemento.compareTo(ciudadActual) == 0) {
                //no hacer nada
            } else if (!hormiga.ciudades.contains(ciudadSiguiente)) {
                //entonces es transicion posible, hacer sumatoria
                resultado
                        += Math.pow(visibilidades.get(ciudadActual).get(ciudadSiguiente) == Double.POSITIVE_INFINITY
                                        ? 0.0
                                        : visibilidades.get(ciudadActual).get(ciudadSiguiente), Init.BETA)
                        * Math.pow(feromonas.get(ciudadActual).get(ciudadSiguiente) == Double.POSITIVE_INFINITY
                                        ? 0.0
                                        : feromonas.get(ciudadActual).get(ciudadSiguiente), Init.ALFA);
            }
        }

        return resultado == 0.0 ? 1.0 : resultado;

    }

    private double getSumatoriaOtrasFeromonasPosibles(ArrayList<ArrayList<Double>> visibilidades, Hormiga hormiga, Integer ciudadActual, Integer ciudadSiguiente) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        Double resultado = 0.0;
        Integer contadorCiclos = 0;
        for (Integer elemento : Init.LISTA_CIUDADES) {

            if (elemento.compareTo(ciudadActual) == 0) {
                //no hacer nada
            } else if (!hormiga.ciudades.contains(ciudadSiguiente)) {
                //entonces es transicion posible, hacer sumatoria
                resultado
                        += Math.pow(visibilidades.get(ciudadActual).get(ciudadSiguiente) == Double.POSITIVE_INFINITY
                                        ? 0.0
                                        : visibilidades.get(ciudadActual).get(ciudadSiguiente), Init.ALFA);
            }
        }

        return resultado;

    }

    private ArrayList<Double> getFDAS(Integer ciudadActual, ArrayList<String> ciudadesNoVisitadas) {

        ArrayList<Double> fdas = new ArrayList<>();
        ArrayList<Double> probabilidades = new ArrayList<>();
        Double fda = 0.0;
        ArrayList<Double> tausxvis = new ArrayList<>();
        Double tausxvistotal = 0.0;

        for (String ciudadNoVisitada : ciudadesNoVisitadas) {

            //calcular tau por visibilidad
            Double elemento = 0.0;
            elemento = Math.pow(Init.feromonas.get(ciudadActual).get(Integer.valueOf(ciudadNoVisitada)),
                    Init.ALFA)
                    * Math.pow(Init.visibilidades.get(ciudadActual).get(Integer.valueOf(ciudadNoVisitada)), Init.BETA);
            tausxvis.add(elemento);
            tausxvistotal += elemento;

        }

        for (Double tauxvi : tausxvis) {

            Double elemento = 0.0;
            elemento = tauxvi / tausxvistotal;
            probabilidades.add(elemento);
            fda += elemento;
            fdas.add(fda);

        }
        return fdas;

//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
