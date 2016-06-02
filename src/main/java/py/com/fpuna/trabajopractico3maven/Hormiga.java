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
        ArrayList<String> poolPosCiudades = initListaCiudades();
        ciudades = new ArrayList<>();
        Random rand = new Random();
        //se elige una ciudad para iniciar
        Integer randomPosCiudad = rand.nextInt((poolPosCiudades.size()));

        ciudades.add(Init.LISTA_CIUDADES.get(Integer.valueOf(poolPosCiudades.get(randomPosCiudad))));
        poolPosCiudades.remove(randomPosCiudad.intValue());

        while (ciudades.size() < Init.CANTIDAD_CIUDADES) {
            //el recorrido para al tener todas las ciudades
            Double realizoPaso;
            Integer randomCiudad;
            Integer RandomPos;
            do {
                rand = new Random();
                realizoPaso = Math.random();
                RandomPos = rand.nextInt((poolPosCiudades.size()));
                //log.debug("randompos "+RandomPos);
                //log.debug("poolPosCiudades "+poolPosCiudades.size());
                randomCiudad = Init.LISTA_CIUDADES.get(Integer.valueOf(poolPosCiudades.get(RandomPos)));

            } while ((realizoPaso > getProbabilidadPaso(ciudades.get(ciudades.size() - 1), randomCiudad, this))
                    || ciudades.contains(randomCiudad));

            ciudades.add(randomCiudad);
            poolPosCiudades.remove(RandomPos.intValue());
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
        resultado = Math.pow(Init.visibilidades.get(ciudadActual).get(ciudadPaso), Init.BETA)
                * Math.pow(Init.feromonas.get(ciudadActual).get(ciudadPaso), Init.ALFA);
        //numerador

        resultado /= getSumatoriaOtrasVisibilidadesPosibles(Init.visibilidades, Init.feromonas, hormiga, ciudadActual, ciudadPaso);;
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
                                        : feromonas.get(ciudadActual).get(ciudadSiguiente), Init.BETA);
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

}
