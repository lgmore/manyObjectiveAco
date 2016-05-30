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
public class Hormiga implements Callable {

    private ArrayList<Integer> solucionActual;
    private Double fitnessSolucionActual;
    private Double fitnessMejorSolucionLocal;
    private Double velocidadActual;
    static final Logger log = LogManager.getLogger(Hormiga.class.getName());

    public Hormiga() {

        //solucionActual = ciudadesIniciales;

        //int cantidadCiudades = ciudadesIniciales.size();
        //Random rn = new Random();

        //Collections.shuffle(solucionActual, rn);

    }

    /**
     * @return the solucionActual
     */
    public ArrayList<Integer> getSolucionActual() {
        return solucionActual;
    }

    /**
     * @param solucionActual the solucionActual to set
     */
    public void setSolucionActual(ArrayList<Integer> solucionActual) {
        this.solucionActual = solucionActual;
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

    /**
     * @return the fitnessMejorSolucionLocal
     */
    public Double getFitnessMejorSolucionLocal() {
        return fitnessMejorSolucionLocal;
    }

    /**
     * @param fitnessMejorSolucionLocal the fitnessMejorSolucionLocal to set
     */
    public void setFitnessMejorSolucionLocal(Double fitnessMejorSolucionLocal) {
        this.fitnessMejorSolucionLocal = fitnessMejorSolucionLocal;
    }

    void doRecorrido() {
        //realizar el recorrido, de acuerdo a como esta en la matriz de ciudades
        Double resultado = 0.0;
        boolean primeraVez = true;
        Random rand = new Random();
        Integer ciudadDondeEstoy = rand.nextInt((Init.CANTIDAD_CIUDADES));
        Integer ciudadDondeVoy = 0;
        solucionActual=new ArrayList<>();
        solucionActual.add(ciudadDondeEstoy);
        fitnessSolucionActual = 0.0;
        while (solucionActual.size() < Init.CANTIDAD_CIUDADES) {

            Double numerito;
            do {
                if (solucionActual.size() >= Init.CANTIDAD_CIUDADES) {
                    break;
                }
                numerito = Math.random(); // si numerito cae entre cero y ProbPasar,
                //entonces la hormiga pasa a esa ciudad
                ciudadDondeVoy = rand.nextInt((Init.CANTIDAD_CIUDADES));
                if(ciudadDondeVoy >13){
                    log.debug("ciudadDondeVoy: " + ciudadDondeVoy);
                }
                log.debug("ProbPasar: " + (calcularProbabilidadDePasar(ciudadDondeEstoy, ciudadDondeVoy)));
                log.debug("numerito: " + numerito);
                log.debug("sol provisoria: " + Arrays.toString(solucionActual.toArray()));
            } while (ciudadYaExiste(ciudadDondeVoy)
                    || (numerito.compareTo(calcularProbabilidadDePasar(ciudadDondeEstoy, ciudadDondeVoy)) > 0)); //
            //thou shalt not pass
            if (solucionActual.size() >= Init.CANTIDAD_CIUDADES) {
                break;
            }
            resultado += ACO.matrizCiudades[ciudadDondeEstoy][ciudadDondeVoy];
            solucionActual.add(ciudadDondeVoy);
            ciudadDondeEstoy = ciudadDondeVoy;
        }
        //se agrega el resultado del recorrido mas el camino de vuelta a la 
        //solucion inicial
        fitnessSolucionActual = resultado
                + ACO.matrizCiudades[solucionActual.get(solucionActual.size() - 1)][solucionActual.get(0)];

        //antes de volver, se imprime el resultado provisorio
        log.info("Sol hormiga: " + Arrays.toString(solucionActual.toArray()));
        log.info("Fitness solucion " + fitnessSolucionActual);
    }

    /**
     * @return the velocidadActual
     */
    public Double getVelocidadActual() {
        return velocidadActual;
    }

    /**
     * @param velocidadActual the velocidadActual to set
     */
    public void setVelocidadActual(Double velocidadActual) {
        this.velocidadActual = velocidadActual;
    }

    @Override
    public Hormiga call() throws Exception {
        this.doRecorrido();
        return this;
    }

    private boolean ciudadYaExiste(Integer ciudadDondeVoy) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        return solucionActual.contains(ciudadDondeVoy);

    }

    public static double pow(final double a, final double b) {
        final int x = (int) (Double.doubleToLongBits(a) >> 32);
        final int y = (int) (b * (x - 1072632447) + 1072632447);
        return Double.longBitsToDouble(((long) y) << 32);
    }

    private Double calcularProbabilidadDePasar(Integer ciudadDondeEstoy, Integer ciudadADondeVoy) {
        //int i = ant.tour[currentIndex];
        Double resultado = 0.0;
        Double denominador = 0.0;
        for (int l = 0; l < Init.CANTIDAD_CIUDADES; l++) { ///probabilidades de que pase a otro estado,
            //dado que la ciudad todavia no se visito
            if (!solucionActual.contains(l)) {
                denominador += pow(ACO.matrizFeromonas[ciudadDondeEstoy][l], Init.alpha) //feromonas
                        * pow(1.0 / ACO.matrizCiudades[ciudadDondeEstoy][l], Init.beta); //visibilidades
            }
        }

        //for (int j = 0; j < Init.CANTIDAD_CIUDADES; j++) {
        if (solucionActual.contains(ciudadADondeVoy)) {
            resultado = 0.0;

        } else {
            Double numerador = Math.pow(ACO.matrizFeromonas[ciudadDondeEstoy][ciudadADondeVoy], Init.alpha)
                    * Math.pow(1.0 / ACO.matrizCiudades[ciudadDondeEstoy][ciudadADondeVoy], Init.beta);

            //log.info("numerador: " + numerador);
            //log.info("denominador: " + denominador);
            resultado = numerador / denominador;
        }
        //}

        return resultado;

    }

}
