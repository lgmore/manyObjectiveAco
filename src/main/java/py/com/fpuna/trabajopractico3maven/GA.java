/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.fpuna.trabajopractico3maven;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.BinaryOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author lg_more
 */
public class GA {

    static public final Integer CANTIDAD_GENES = 20;
    public static final int CANTIDAD_OBJETIVOS = 8;
    static public final Double CROSSOVER_RATE = 0.25;
    static public final Double MUTACION_RATE = 0.10;
    static public final Double[] EXTREMOS = {0.0, 1.0};
    static public ArrayList<Double> funcionDistribucionAcumulativa;
    static final Logger log = LogManager.getLogger(GA.class.getName());

    public static ArrayList<Individuo> getIndividuosNoDominados(ArrayList<Individuo> individuos) {

        ArrayList<Individuo> nuevaPoblacion = new ArrayList<>();

        while (individuos.size() > 0) {

            Individuo indiv = individuos.remove(0);

            if (individuos.size() <= 0) { //hacer la comparacion contra los individuos no dominados
                //obtenidos hasta ahora

                for (Individuo elemento : nuevaPoblacion) {

                    indiv = ese2dominado.apply(elemento, indiv);

                }

            } else {

                for (Individuo elemento : individuos) {

                    indiv = ese2dominado.apply(elemento, indiv);
                    if (indiv.dominado == true) {
                        break;
                    }
                }

                for (Individuo elemento : nuevaPoblacion) {

                    indiv = ese2dominado.apply(elemento, indiv);
                    if (indiv.dominado == true) {
                        break;
                    }

                }

            }

            if (!indiv.dominado) {

                nuevaPoblacion.add(indiv);

            }

        }

        return nuevaPoblacion;

    }

    static BinaryOperator<ArrayList<Double>> getDistanciaEuclidiana
            = (elemento1, elemento2) -> {
                ArrayList<Double> resultado = new ArrayList<>();
                Double distanciaEuclidiana = 0.0;
                for (int i = 0; i < elemento1.size(); i++) {
                    Double solucion1 = elemento1.get(i);
                    Double solucion2 = elemento2.get(i);
                    distanciaEuclidiana += Math.pow(solucion2 - solucion1, 2);
                }

                distanciaEuclidiana = Math.sqrt(distanciaEuclidiana);
                resultado.add(distanciaEuclidiana);

                return resultado;

            };

    static BinaryOperator<Individuo> ese2dominado = (e1, e2) -> {

        ArrayList<Double> fitnesse1 = e1.resultadoSolucionActual;
        ArrayList<Double> fitnesse2 = e2.resultadoSolucionActual;

        boolean esMayorOIgualEnTodosLosObjetivos = true;

        for (int j = 0; j < fitnesse1.size(); j++) {

            if (fitnesse2.get(j) < fitnesse1.get(j)) {

                esMayorOIgualEnTodosLosObjetivos = false;
                break;
            }

        }

        if (esMayorOIgualEnTodosLosObjetivos) {
            e2.dominado = true;
        }

        return e2;
    };

    static ArrayList<Individuo> inicializarIndividuos(Integer cantidadIndividuos) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ArrayList<Individuo> resultado = new ArrayList<>();

        Double minimum = EXTREMOS[0];
        Double maximum = EXTREMOS[1] - EXTREMOS[0];

        for (int j = 0; j < cantidadIndividuos; j++) {
            Individuo elemento = new Individuo();
            elemento.cromosomas = new ArrayList<>();

            for (int i = 0; i < CANTIDAD_GENES; i++) {

                Random rand = new Random();
                Double randomNum = EXTREMOS[0] + (EXTREMOS[1] - EXTREMOS[0]) * rand.nextDouble();
                elemento.cromosomas.add(randomNum);

            }

            resultado.add(elemento);

        }

        return resultado;

    }

    static ArrayList<Individuo> calcularFDA(ArrayList<Individuo> individuos) {

        ArrayList<Individuo> resultado = individuos;

        Double totalFitness = 0.0;
        Double totalFDA = 0.0;

        for (Individuo elemento : resultado) {

            totalFDA += elemento.coeficienteFitnessSharing;

        }

        Double totalAnterior = 0.0;

        for (Individuo elemento : resultado) {

            elemento.funcionDistribucionAcumulativa = (totalAnterior + elemento.coeficienteFitnessSharing) / totalFDA;
            totalAnterior += elemento.coeficienteFitnessSharing;

        }

        return resultado;

//        for (Individuo elemento : individuos) {
//
//            elemento.probabilidadSiguienteGeneracion = elemento.fitnessSolucionActual / totalFitness;
//            totalFDA += elemento.probabilidadSiguienteGeneracion;
//            elemento.funcionDistribucionAcumulativa = (totalFDA);
//        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static ArrayList<Individuo> generarNuevosIndividuos(ArrayList<Individuo> individuos) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

//        // ordeno por si las dudas
//        Collections.sort(individuos,
//                (Individuo fruit2, Individuo fruit1)
//                -> fruit1.funcionDistribucionAcumulativa.compareTo(
//                        fruit2.funcionDistribucionAcumulativa));
        ArrayList<Individuo> nuevosIndividuos = new ArrayList<>();

        while (nuevosIndividuos.size() < individuos.size()) {

            Double numeroRandom = Math.random();

            for (Individuo elemento : individuos) {

                if (numeroRandom <= elemento.funcionDistribucionAcumulativa) {

                    nuevosIndividuos.add(elemento);
                    break;
                }

            }

        }

        return nuevosIndividuos;
    }

    static ArrayList<Individuo> realizarCrossover(ArrayList<Individuo> PoblacionPareto, ArrayList<Individuo> Poblacion) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        ArrayList<Individuo> nuevaPoblacion = new ArrayList<>();

        while (nuevaPoblacion.size() < Init.CANTIDAD_INDIVIDUOS) {

            //seleccionar uno del frente pareto
            Double rnd = Math.random();

            Individuo ladoPareto = null;
            for (Individuo elemento : PoblacionPareto) {
                if (rnd <= elemento.funcionDistribucionAcumulativa) {

                    ladoPareto = elemento;
                    break;

                }
            }

            Random rn2 = new Random();
            Integer posLadoPoblacion = rn2.nextInt(Poblacion.size());

            Individuo ladoPoblacion = Poblacion.get(posLadoPoblacion);

            Integer puntoCrossover = rn2.nextInt(CANTIDAD_GENES);
            ArrayList<Double> cromosomasCrossover = new ArrayList<>();
            cromosomasCrossover.addAll(ladoPareto.cromosomas.subList(0, puntoCrossover));
            cromosomasCrossover.addAll(ladoPoblacion.cromosomas.subList(puntoCrossover, CANTIDAD_GENES));

            Individuo resultado = new Individuo();
            resultado.cromosomas = cromosomasCrossover;
            nuevaPoblacion.add(resultado);

        }

        return nuevaPoblacion;
    }

    static ArrayList<Individuo> realizarMutacion(ArrayList<Individuo> Poblacion) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Double cantidadGenes = (double) Poblacion.size() * CANTIDAD_GENES;
        ArrayList<Individuo> nuevaPoblacion = Poblacion;

        Double numeroMutaciones;
        numeroMutaciones = (MUTACION_RATE * cantidadGenes);

        for (int i = 0; i < numeroMutaciones.intValue(); i++) {

            Random rnd = new Random();
            Integer posicionMutacion = rnd.nextInt(cantidadGenes.intValue());

            Integer iteradorGenes = 0;
            Integer posicionCromosomas = 0;
            Integer posicionGenes;
            //buscar en que posicion se muta
            while (iteradorGenes < posicionMutacion) {

                if (iteradorGenes + CANTIDAD_GENES < posicionMutacion) {
                    posicionCromosomas++;
                    iteradorGenes += CANTIDAD_GENES;

                } else {

                    break;

                }

            }
            posicionGenes = posicionMutacion - iteradorGenes;
            posicionGenes = posicionGenes % CANTIDAD_GENES; //PARA EVITAR INDEXOUTOFBOUNDS
            rnd = new Random();
            Double ValorMutado = EXTREMOS[0] + (EXTREMOS[1] - EXTREMOS[0]) * rnd.nextDouble();
            nuevaPoblacion.get(posicionCromosomas).cromosomas.set(posicionGenes, ValorMutado);

        }

        return nuevaPoblacion;

    }

    static ArrayList<Individuo> calcularFitnessSharing(ArrayList<Individuo> poblacionPareto, Double RADIO_FITNESS_SHARING) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        ArrayList<Individuo> resultado = poblacionPareto;

        Collections.sort(resultado, (Individuo elemento2, Individuo elemento1)
                -> elemento1.resultadoSolucionActual.get(0)
                .compareTo(elemento2.resultadoSolucionActual
                        .get(0)));

        for (Individuo individuo : resultado) {

            Double contadorVecinos = 0.0;
            for (Individuo individuoASerAsignado : resultado) {

                ArrayList<Double> distanciaE = getDistanciaEuclidiana.apply(
                        individuo.resultadoSolucionActual,
                        individuoASerAsignado.resultadoSolucionActual);
                if (distanciaE.get(0) <= RADIO_FITNESS_SHARING) {
                    if (distanciaE.get(0) > 0.0) {
                        contadorVecinos++;
                    }
                } else {
                    //break;
                }

            }

            individuo.coeficienteFitnessSharing = 1 / (1 + contadorVecinos);

            for (Individuo individuoASerAsignado : resultado) {

                ArrayList<Double> distanciaE = getDistanciaEuclidiana.apply(
                        individuo.resultadoSolucionActual,
                        individuoASerAsignado.resultadoSolucionActual);
                if (distanciaE.get(0) <= RADIO_FITNESS_SHARING) {
                    individuoASerAsignado.coeficienteFitnessSharing = 1 / (1 + contadorVecinos);
                } else {
                    //break;
                }

            }

        }

        for (Individuo elemento : resultado) {

            log.debug("fitness sharing : " + elemento.coeficienteFitnessSharing);

        }

        return resultado;

    }

}
