/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.fpuna.trabajopractico3maven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Callable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static py.com.fpuna.trabajopractico3maven.Init.log;

/**
 *
 * @author lg_more
 */
public class Individuo implements Callable {

    public ArrayList<Double> cromosomas;
    public Double fitnessSolucionActual;
    public Double resultadoSolucionActual;
    public Double probabilidadSiguienteGeneracion;
    public Double funcionDistribucionAcumulativa;
    static final Logger log = LogManager.getLogger(Individuo.class.getName());

    public Individuo() {

        //solucionActual = ciudadesIniciales;
        //int cantidadCiudades = ciudadesIniciales.size();
        //Random rn = new Random();
        //Collections.shuffle(solucionActual, rn);
    }

    /**
     * @return the fitnessSolucionActual
     */
    public Double getFitnessSolucionActual() {
        return fitnessSolucionActual;
    }

    /**
     * @param fitnessSolucionActual the fitnessSolucionActual to set
     */
    public void setFitnessSolucionActual(Double fitnessSolucionActual) {
        this.fitnessSolucionActual = fitnessSolucionActual;
    }

    void evaluarFuncion() {
        //(a+2b+3c+4d)-30

        resultadoSolucionActual
                = (cromosomas.get(0)
                + 2 * cromosomas.get(1)
                + 3 * cromosomas.get(2)
                + 4 * cromosomas.get(3))
                - 30;

        //antes de volver, se imprime el resultado provisorio
        log.info("Sol individuo: " + Arrays.toString(cromosomas.toArray()));
        log.info("resultado solucion " + resultadoSolucionActual);
    }

    @Override
    public Individuo call() throws Exception {
        this.evaluarFuncion();
        return this;
    }

//    private Double calcularProbabilidadDePasar(Integer ciudadDondeEstoy, Integer ciudadADondeVoy) {
//        //int i = ant.tour[currentIndex];
//        Double resultado = 0.0;
//        Double denominador = 0.0;
//        for (int l = 0; l < Init.CANTIDAD_CIUDADES; l++) { ///probabilidades de que pase a otro estado,
//            //dado que la ciudad todavia no se visito
//            if (!solucionActual.contains(l)) {
//                denominador += pow(GA.matrizFeromonas[ciudadDondeEstoy][l], Init.alpha) //feromonas
//                        * pow(1.0 / GA.matrizCiudades[ciudadDondeEstoy][l], Init.beta); //visibilidades
//            }
//        }
//
//        //for (int j = 0; j < Init.CANTIDAD_CIUDADES; j++) {
//        if (solucionActual.contains(ciudadADondeVoy)) {
//            resultado = 0.0;
//            
//        } else {
//            Double numerador = Math.pow(GA.matrizFeromonas[ciudadDondeEstoy][ciudadADondeVoy], Init.alpha)
//                    * Math.pow(1.0 / GA.matrizCiudades[ciudadDondeEstoy][ciudadADondeVoy], Init.beta);
//
//            //log.info("numerador: " + numerador);
//            //log.info("denominador: " + denominador);
//            resultado = numerador / denominador;
//        }
//        //}
//
//        return resultado;
//        
//    }
    /**
     * @return the probabilidadSiguienteGeneracion
     */
    public Double getProbabilidadSiguienteGeneracion() {
        return probabilidadSiguienteGeneracion;
    }

    /**
     * @param probabilidadSiguienteGeneracion the
     * probabilidadSiguienteGeneracion to set
     */
    public void setProbabilidadSiguienteGeneracion(Double probabilidadSiguienteGeneracion) {
        this.probabilidadSiguienteGeneracion = probabilidadSiguienteGeneracion;
    }
}
