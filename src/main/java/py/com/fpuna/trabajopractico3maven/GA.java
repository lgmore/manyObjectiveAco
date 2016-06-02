/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.fpuna.trabajopractico3maven;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
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
    static public final Double MUTACION_RATE = 0.05;
    static public final Double[] EXTREMOS = {0.0, 1.0};
    static public ArrayList<Double> funcionDistribucionAcumulativa;
    static final Logger log = LogManager.getLogger(GA.class.getName());

    public static ArrayList<Hormiga> getIndividuosNoDominados(ArrayList<Hormiga> individuos) {

        ArrayList<Hormiga> nuevaPoblacion = new ArrayList<>();

        while (individuos.size() > 0) {

            Hormiga indiv = individuos.remove(0);

            if (individuos.size() <= 0) { //hacer la comparacion contra los individuos no dominados
                //obtenidos hasta ahora

                for (Hormiga elemento : nuevaPoblacion) {

                    indiv = ese2dominado.apply(elemento, indiv);

                }

            } else {

                for (Hormiga elemento : individuos) {

                    indiv = ese2dominado.apply(elemento, indiv);
                    if (indiv.dominado == true) {
                        break;
                    }
                }

                for (Hormiga elemento : nuevaPoblacion) {

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

    static BinaryOperator<Hormiga> ese2dominado = (e1, e2) -> {

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

    static ArrayList<Hormiga> inicializarIndividuos(Integer cantidadIndividuos) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ArrayList<Hormiga> resultado = new ArrayList<>();

        Double minimum = EXTREMOS[0];
        Double maximum = EXTREMOS[1] - EXTREMOS[0];

        for (int j = 0; j < cantidadIndividuos; j++) {
            Hormiga elemento = new Hormiga();
            elemento.cromosomas = new ArrayList<>();

            for (int i = 0; i < CANTIDAD_GENES; i++) {

                Random rand = new Random();
                Double randomNum = EXTREMOS[0] + (EXTREMOS[1] - EXTREMOS[0]) * rand.nextDouble();
                //randomNum = Precision.round(randomNum, 3, BigDecimal.ROUND_HALF_UP);
                elemento.cromosomas.add(randomNum);

            }

            resultado.add(elemento);

        }

        return resultado;

    }

    static ArrayList<Hormiga> calcularFDA(ArrayList<Hormiga> individuos) {

        ArrayList<Hormiga> resultado = individuos;

        Double totalFitness = 0.0;
        Double totalFDA = 0.0;

        for (Hormiga elemento : resultado) {

            totalFDA += elemento.coeficienteFitnessSharing;

        }

        Double totalAnterior = 0.0;

        for (Hormiga elemento : resultado) {

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

    static ArrayList<Hormiga> generarNuevosIndividuos(ArrayList<Hormiga> individuos) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

//        // ordeno por si las dudas
//        Collections.sort(individuos,
//                (Individuo fruit2, Individuo fruit1)
//                -> fruit1.funcionDistribucionAcumulativa.compareTo(
//                        fruit2.funcionDistribucionAcumulativa));
        ArrayList<Hormiga> nuevosIndividuos = new ArrayList<>();

        while (nuevosIndividuos.size() < individuos.size()) {

            Double numeroRandom = Math.random();

            for (Hormiga elemento : individuos) {

                if (numeroRandom <= elemento.funcionDistribucionAcumulativa) {

                    nuevosIndividuos.add(elemento);
                    break;
                }

            }

        }

        return nuevosIndividuos;
    }

    static ArrayList<Hormiga> realizarCrossover(ArrayList<Hormiga> PoblacionPareto) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        ArrayList<Hormiga> nuevaPoblacion = new ArrayList<>();
        nuevaPoblacion.addAll(PoblacionPareto);
        ArrayList<Hormiga> Poblacion = new ArrayList<>();
        Poblacion.addAll(PoblacionPareto);

        while (nuevaPoblacion.size() < Init.CANTIDAD_INDIVIDUOS) {

            //seleccionar uno del frente pareto
            Double rnd = Math.random();

            Hormiga ladoPareto = null;
            for (Hormiga elemento : PoblacionPareto) {
                if (rnd <= elemento.funcionDistribucionAcumulativa) {

                    ladoPareto = elemento;
                    break;

                }
            }

            Random rn2 = new Random();
            Integer posLadoPoblacion = rn2.nextInt(Poblacion.size());

            Hormiga ladoPoblacion = Poblacion.get(posLadoPoblacion);

            Integer puntoCrossover = rn2.nextInt(CANTIDAD_GENES);
            ArrayList<Double> cromosomasCrossover = new ArrayList<>();
            cromosomasCrossover.addAll(ladoPareto.cromosomas.subList(0, puntoCrossover));
            cromosomasCrossover.addAll(ladoPoblacion.cromosomas.subList(puntoCrossover, CANTIDAD_GENES));

            Hormiga resultado = new Hormiga();
            resultado.cromosomas = cromosomasCrossover;
            nuevaPoblacion.add(resultado);

        }

        return nuevaPoblacion;
    }

    static ArrayList<Hormiga> realizarMutacion(ArrayList<Hormiga> Poblacion) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Double cantidadGenes = (double) Poblacion.size() * CANTIDAD_GENES;
        ArrayList<Hormiga> nuevaPoblacion = Poblacion;

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

    static ArrayList<Hormiga> calcularFitnessSharing(ArrayList<Hormiga> poblacionPareto, Double RADIO_FITNESS_SHARING) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        ArrayList<Hormiga> resultado = poblacionPareto;

        Collections.sort(resultado, (Hormiga elemento2, Hormiga elemento1)
                -> elemento1.resultadoSolucionActual.get(0)
                .compareTo(elemento2.resultadoSolucionActual
                        .get(0)));

        for (Hormiga individuo : resultado) {

            Double contadorVecinos = 0.0;
            for (Hormiga individuoASerAsignado : resultado) {

                ArrayList<Double> distanciaE = getDistanciaEuclidiana.apply(
                        individuo.resultadoSolucionActual,
                        individuoASerAsignado.resultadoSolucionActual);
                log.debug("distanciaE: " + distanciaE);
                if (distanciaE.get(0) <= RADIO_FITNESS_SHARING) {
                    if (distanciaE.get(0) > 0.0) {
                        contadorVecinos++;
                    }
                } else {
                    //break;
                }

            }

            individuo.coeficienteFitnessSharing = 1 / (1 + contadorVecinos);

            for (Hormiga individuoASerAsignado : resultado) {

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

        for (Hormiga elemento : resultado) {

            log.debug("fitness sharing : " + elemento.coeficienteFitnessSharing);

        }

        return resultado;

    }

    static ArrayList<Hormiga> podarPoblacion(ArrayList<Hormiga> poblacionIntermedia) {

        ArrayList<Hormiga> resultado = new ArrayList<>();

        while (resultado.size() > Init.CANTIDAD_INDIVIDUOS) {

            Integer contador = 0;
            for (Hormiga elemento : resultado) {
                Double rnd = Math.random();

                if (rnd < elemento.funcionDistribucionAcumulativa) {
                    //entra si no tiene tantos vecinos
                    resultado.add(elemento);
                }
                contador++;

            }

        }

        return resultado;

    }

    static ArrayList<Hormiga> calcularStrength(ArrayList<Hormiga> poblacionPareto, ArrayList<Hormiga> individuosDominados) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        for (Hormiga elemento : poblacionPareto) {

            elemento.strengh = 0.0;
            for (Hormiga elementoDominado : individuosDominados) {

                elementoDominado.dominado = false;
                elementoDominado = ese2dominado.apply(elemento, elementoDominado);
                if (elementoDominado.dominado) {

                    elemento.strengh++;

                }

            }
            elemento.coeficienteFitnessSharing=elemento.strengh;

        }
//
//        for (Individuo elemento : poblacionPareto) {
//
//            elemento.coeficienteFitnessSharing = 1 / (1 + elemento.strengh);
//
//    
//        }
        return poblacionPareto;

    }

}
