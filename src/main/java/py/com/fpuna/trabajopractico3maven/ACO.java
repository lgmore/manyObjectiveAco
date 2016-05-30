/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.fpuna.trabajopractico3maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author lg_more
 */
public class ACO {

    public static Double[][] matrizCiudades;
    public static Double[][] matrizFeromonas;
    public static Double[][] matrizVisibilidades;

    static public Double[][] create2DIntMatrixFromFile(Path path) throws IOException {
        String fileName = path.toString();
        Scanner inputStream = null;

        try {
            inputStream = new Scanner(new File(fileName));//The txt file is being read correctly.
        } catch (FileNotFoundException e) {
            System.out.println("Error opening the file " + fileName);
            System.exit(0);
        }

        Double[][] temperatures = new Double[Init.CANTIDAD_CIUDADES][Init.CANTIDAD_CIUDADES];
        for (int row = 0; row < Init.CANTIDAD_CIUDADES; row++) {
            String line = inputStream.nextLine();
            String[] tokens = line.split(" ");
            for (int column = 0; column < Init.CANTIDAD_CIUDADES; column++) {

                temperatures[row][column] = Double.parseDouble(tokens[column]);
            }
        }
        inputStream.close();
        return temperatures;
    }

    static void inicializarVisibilidades(int cantidadCiudades) {
        matrizVisibilidades = new Double[cantidadCiudades][cantidadCiudades];

        for (int i = 0; i < cantidadCiudades; i++) {

            for (int j = 0; j < cantidadCiudades; j++) {

                matrizVisibilidades[i][j] = 1 / matrizCiudades[i][j];

            }

        }
    }

    static void inicializarFeromonas(Integer cantidadCiudades) {
        matrizFeromonas = new Double[cantidadCiudades][cantidadCiudades];

        for (int i = 0; i < cantidadCiudades; i++) {

            for (int j = 0; j < cantidadCiudades; j++) {

                matrizFeromonas[i][j] = 1.0;

            }

        }

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static void actualizarFeromonas(ArrayList<Hormiga> colonia) {

        Double sumatoriaDeltaTau = 0.0;

        for (Hormiga elemento : colonia) {

            sumatoriaDeltaTau += Init.Q / elemento.getFitnessSolucionActual();
        }

        for (Hormiga elemento : colonia) {

            boolean primeraVez = true;
            Integer ciudadDondeEstoy = 0;
            Integer ciudadDondeVoy = 0;
            for (Integer elementoSolucion : elemento.getSolucionActual()) {

                if (primeraVez) {
                    ciudadDondeEstoy = elementoSolucion;
                    primeraVez = false;
                } else {
                    ciudadDondeVoy = elementoSolucion;
                    matrizFeromonas[ciudadDondeEstoy][ciudadDondeVoy]
                            = (1 - Init.evaporacion)
                            * matrizFeromonas[ciudadDondeEstoy][ciudadDondeVoy]
                            + sumatoriaDeltaTau;
                    matrizFeromonas[ciudadDondeVoy][ciudadDondeEstoy]
                            = (1 - Init.evaporacion)
                            * matrizFeromonas[ciudadDondeVoy][ciudadDondeEstoy]
                            + sumatoriaDeltaTau; //la matriz es simetrica
                    ciudadDondeEstoy = ciudadDondeVoy;
                }

            }

        }

    }

}
