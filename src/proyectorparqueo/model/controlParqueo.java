/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author DIEGO
 */
public class controlParqueo {
    private ArrayList<vehiculo> vehiculos;
    
    public controlParqueo(){
        vehiculos = new ArrayList<>();
    }
    
    public void registrarVehiculo(vehiculo v){
        vehiculos.add(v);
        System.out.println("Vehiculo registrado: " + v.getPlaca());
    }
    
    public void mostrarVehiculos(){
        for(vehiculo v : vehiculos){
            v.mostrarDatos();
            System.out.println("---------------------");
        }
    }
    
    public vehiculo buscarPorPlaca(String placa){
        for(vehiculo v : vehiculos){
            if (v.getPlaca().equalsIgnoreCase(placa)){
                return v;
            }
        }
        return null;
    }
    
    public void guardarCSV(String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))){
            for (vehiculo v : vehiculos){
                writer.println(v.getPlaca()+ "," 
                       + v.getPropietario()+ ","
                + v.getTipoVehiculo() + ","
                + v.getTipoPlan());
            }
                System.out.println("ARCHIVO SCV GUARDADO CORRECTAMENTE. ");
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        public void cargarCSV(String nombreArchivo){
            vehiculos.clear();
            try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))){
                String linea;
                while ((linea = br.readLine())!=null){
                    
                String[] datos = linea.split(",");
                if (datos.length == 4){
                    vehiculo v = new vehiculo(datos[0], datos[1], datos[2], datos[3], true);
                    vehiculos.add(v);
                }
            }
                System.out.println("Archivo CSV cargado correctamente. ");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }    
