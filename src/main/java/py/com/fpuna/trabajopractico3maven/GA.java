/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.fpuna.trabajopractico3maven;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author lg_more
 */
public class GA {

    static public final Integer CANTIDAD_CROMOSOMAS = 4;
    static public final Double CROSSOVER_RATE = 0.25;
    static public final Double MUTACION_RATE = 0.10;
    static public final Double[] EXTREMOS = {0.0, 30.0};
    static public ArrayList<Double> funcionDistribucionAcumulativa;

    static ArrayList<Individuo> inicializarIndividuos(Integer cantidadIndividuos) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ArrayList<Individuo> resultado = new ArrayList<>();

        Double minimum = EXTREMOS[0];
        Double maximum = EXTREMOS[1] - EXTREMOS[0];

        for (int j = 0; j < cantidadIndividuos; j++) {
            Individuo elemento = new Individuo();
            elemento.cromosomas = new ArrayList<>();

            for (int i = 0; i < CANTIDAD_CROMOSOMAS; i++) {

                Random rand = new Random();
                Double randomNum = EXTREMOS[0] + (EXTREMOS[1] - EXTREMOS[0]) * rand.nextDouble();
                elemento.cromosomas.add(randomNum);

            }

            resultado.add(elemento);

        }

        return resultado;

    }

    static void calcularFDA(ArrayList<Individuo> individuos) {

        Double totalFitness = 0.0;
        Double totalFDA = 0.0;

        for (Individuo elemento : individuos) {

            elemento.fitnessSolucionActual = (1 / (1 + elemento.resultadoSolucionActual));
            totalFitness += elemento.fitnessSolucionActual;
        }

        for (Individuo elemento : individuos) {

            elemento.probabilidadSiguienteGeneracion = elemento.fitnessSolucionActual / totalFitness;
            totalFDA += elemento.probabilidadSiguienteGeneracion;
            elemento.funcionDistribucionAcumulativa = (totalFDA);
        }

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

    static ArrayList<Individuo> realizarCrossover(ArrayList<Individuo> Poblacion) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        ArrayList<Individuo> nuevaPoblacion = Poblacion;

        ArrayList<Integer> individuosCruzamiento = new ArrayList<>();
        Integer contador = 0;
        //seleccionar individuos para cruzamiento
        for (Individuo elemento : Poblacion) {

            Double rnd = Math.random();

            if (rnd < CROSSOVER_RATE) {
                //individuo seleccionado
                individuosCruzamiento.add(contador);
            }
            contador++;

        }

        if (individuosCruzamiento.size() < 2) {//no cruzar, no hay suficientes elementos
            return nuevaPoblacion;
        }

        Integer individuoAnterior = null;
        Integer individuoActual = null;
        for (Integer elemento : individuosCruzamiento) {

            if (individuoAnterior == null) {

                individuoAnterior = elemento;
                continue;

            }

            individuoActual = elemento;
            Individuo individuoAnteriorI = Poblacion.get(individuoAnterior);
            Individuo individuoActualI = Poblacion.get(individuoActual);

            ArrayList<Double> cromosomasCrossover = new ArrayList<>();

            contador = 0;
            Random random = new Random();
            Integer puntoCrossover = random.nextInt(CANTIDAD_CROMOSOMAS);

            cromosomasCrossover.addAll(individuoAnteriorI.cromosomas.subList(0, puntoCrossover));
            cromosomasCrossover.addAll(individuoActualI.cromosomas.subList(puntoCrossover, CANTIDAD_CROMOSOMAS));

            nuevaPoblacion.get(individuoAnterior).cromosomas = cromosomasCrossover;

            individuoAnterior = individuoActual;

        }

        //al final hay que hacer entre el ultimo y el primero
        Individuo individuoAnteriorI = Poblacion.get(
                individuosCruzamiento.get(individuosCruzamiento.size() - 1));
        Individuo individuoActualI = Poblacion.get(
                individuosCruzamiento.get(0));

        ArrayList<Double> cromosomasCrossover = new ArrayList<>();

        contador = 0;
        Random random = new Random();
        Integer puntoCrossover = random.nextInt(CANTIDAD_CROMOSOMAS);

        cromosomasCrossover.addAll(individuoAnteriorI.cromosomas.subList(0, puntoCrossover));
        cromosomasCrossover.addAll(individuoActualI.cromosomas.subList(puntoCrossover, CANTIDAD_CROMOSOMAS));

        nuevaPoblacion.get(individuosCruzamiento.size() - 1).cromosomas = cromosomasCrossover;

        return nuevaPoblacion;
    }

    static ArrayList<Individuo> realizarMutacion(ArrayList<Individuo> Poblacion) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Double cantidadGenes = (double) Poblacion.size() * CANTIDAD_CROMOSOMAS;
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

                if (iteradorGenes + CANTIDAD_CROMOSOMAS < posicionMutacion) {
                    posicionCromosomas++;
                    iteradorGenes += CANTIDAD_CROMOSOMAS;

                } else {

                    break;

                }

            }
            posicionGenes = posicionMutacion - iteradorGenes;
            posicionGenes = posicionGenes % CANTIDAD_CROMOSOMAS; //PARA EVITAR INDEXOUTOFBOUNDS
            rnd = new Random();
            Double ValorMutado = EXTREMOS[0] + (EXTREMOS[1] - EXTREMOS[0]) * rnd.nextDouble();
            nuevaPoblacion.get(posicionCromosomas).cromosomas.set(posicionGenes, ValorMutado);

        }

        return nuevaPoblacion;

    }

}
