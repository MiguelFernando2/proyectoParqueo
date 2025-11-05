/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

import java.time.Duration;
import java.time.LocalDateTime;

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
    
    
    //Tarifas HORA POR VARIABLE
    private static final double HORA_MOTO = 5.0;  // Hora por (Variavle)
    private static final double HORA_CARRO = 10.0; // 
    //HORA POR FLAT
    private static final double FLAT_MOTO = 25; 
    private static final double FLAT_CARRO = 40;
    
 
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
    
    //Calcular cobro al salir 
    public double calcularCobro(vehiculo v, LocalDateTime horaSalida){
        if (v == null ) return 0.0;
        
        String plan = v.getTipoPlan().toUpperCase(); // VARIABLE O FLAT
        String tipo = v.getTipoVehiculo().toUpperCase(); // CARRO P MOTO
        
        if (plan.contains("VARIABLE ")){ // redondeo por arriba a la hora siguiente
            long minutos = Duration.between(v.getHoraIngreso(), horaSalida).toMinutes();
            long horasRedondeadas = (minutos + 59) / 60; 
            double tarifaHora = tipo.contains("MOTO") ? HORA_MOTO : HORA_CARRO;
            
            return horasRedondeadas * tarifaHora;
        } else {
            // FLAT
            return tipo.contains("MOTO") ? FLAT_MOTO : FLAT_CARRO;
        }
    }
    
    ///////////////////////////////////////
    
    
    //REGISTRAR SALIDA 
    public double registrarSalida(String placa){
        vehiculo v = buscarPorPlaca(placa);
        if (v == null) return -1; //NO ENCONTRADO
        
        double total = calcularCobro(v, LocalDateTime.now());
        vehiculos.remove(v);
        return total;
        
    }
    
    // exponer la lista
    
    public java.util.List<vehiculo>getVehiculo(){
        return vehiculos;
    }
    
    public boolean eliminarVehiculo(String placa){
        vehiculo v = buscarPorPlaca(placa);
        if (v != null){
            vehiculos.remove(v);
            return true;
        }
        return false;
    }
    
    //////////////////////
    
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
        public void cargarCSV(String nombreArchivo) throws IOException {
            
            try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))){
                String linea;
                boolean primera = true;
                
                while ((linea = br.readLine())!=null){
                    if (primera){
                        primera = false;
                        continue;
                    }
                String[] datos = linea.split(",");
                if (datos.length >= 4){
                    vehiculo v = new vehiculo(datos[0].trim(), datos[1].trim(), datos[2].trim(), datos[3].trim(), true);
                    vehiculos.add(v);
                }
            }
                
            }
            
            
        }
        
        public ArrayList<vehiculo> getVehiculos(){
            return vehiculos;
        }
        public void limpiar(){
            vehiculos.clear();
        }
    }    
