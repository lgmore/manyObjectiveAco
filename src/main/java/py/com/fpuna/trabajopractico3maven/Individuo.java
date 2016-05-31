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
    Double coeficienteFitnessSharing = null;

    public Individuo() {

        //solucionActual = ciudadesIniciales;
        //int cantidadCiudades = ciudadesIniciales.size();
        //Random rn = new Random();
        //Collections.shuffle(solucionActual, rn);
    }

    void evaluarFuncion() {

        resultadoSolucionActual= new ArrayList<>();

        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> f = new ArrayList<>();

        for (Double gen : cromosomas) {

            x.add(gen);

        }

        Double g = 0.0;
        for (Double elemento : x) {

            g += Math.pow(elemento - 0.5, 2);

        }

        for (int i = 0; i < GA.CANTIDAD_OBJETIVOS; i++) {

            Double elemento = 1 + g;
            f.add(elemento);

        }

        for (int i = 0; i < GA.CANTIDAD_OBJETIVOS; i++) {
            for (int j = 0; j < GA.CANTIDAD_OBJETIVOS - (i + 1); j++) {
                Double f_i = f.get(i);
                f_i *= Math.cos(x.get(j) * 0.5 * Math.PI);
                f.set(i, f_i);
            }
            if (i != 0) {
                Double f_i = f.get(i);
                int aux = GA.CANTIDAD_OBJETIVOS - (i + 1);
                f_i *= Math.sin(x.get(aux) * 0.5 * Math.PI);
                //f[i] *= Math.sin(x[aux] * 0.5 * Math.PI);
                f.set(i, f_i);
            } //if 
        } // for

        for (int i = 0; i < GA.CANTIDAD_OBJETIVOS; i++) {
            resultadoSolucionActual.add(f.get(i));
        }
        
        log.debug("individuo: "+ Arrays.toString(cromosomas.toArray()));
        log.debug("solucion: "+ Arrays.toString(resultadoSolucionActual.toArray()));

    }

    @Override
    public Individuo call() throws Exception {
        this.evaluarFuncion();
        return this;
    }

}
