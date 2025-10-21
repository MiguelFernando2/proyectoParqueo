/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

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
}
