/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.fpuna.trabajopractico3maven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.UnaryOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author lg_more
 */
public class Individuo implements Callable {

    public ArrayList<Double> cromosomas;
    public ArrayList<Double> fitnessSolucionActual;
    public ArrayList<Double> resultadoSolucionActual;

    public Double funcionDistribucionAcumulativa;
    static final Logger log = LogManager.getLogger(Individuo.class.getName());
    boolean dominado = false; //no dominado hasta que se diga lo contrario
    Double coeficienteFitnessSharing=null;

    public Individuo() {

        //solucionActual = ciudadesIniciales;
        //int cantidadCiudades = ciudadesIniciales.size();
        //Random rn = new Random();
        //Collections.shuffle(solucionActual, rn);
    }

    void evaluarFuncion() {
        resultadoSolucionActual = new ArrayList<>();
        //(a+2b+3c+4d)-30

        //x^2
        UnaryOperator<Double> funcion1 = (x) -> {
            return Math.pow(x, 2);
        };

        UnaryOperator<Double> funcion2 = (x) -> {

            return Math.pow(x - 2, 2);

        };

        resultadoSolucionActual.add(funcion1.apply(cromosomas.get(0)));
        resultadoSolucionActual.add(funcion2.apply(cromosomas.get(0)));

        //antes de volver, se imprime el resultado provisorio
        log.debug("Sol individuo: " + Arrays.toString(cromosomas.toArray()));
        log.debug("resultado solucion " + resultadoSolucionActual);
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



}
