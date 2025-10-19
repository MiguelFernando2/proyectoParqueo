/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

/**
 *
 * @author DIEGO
 */
public class Area {
    
    // Atributoss
    public String id;
    public String nombre;
    public int capacidad;
    public String tipoVehiculo;
    public int ocupados;
    
    // Constructor
    public Area(String id, String nombre, int capacidad, String tipoVehiculo) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.tipoVehiculo = tipoVehiculo;
        this.ocupados = 0;
    }

    // Métodos Get y Set
    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public int getCapacidad() {
        return capacidad;
    }
    public String getTipoVehiculo() {
        return tipoVehiculo;
    }
    public int getOcupados() {
        return ocupados;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }
    public void setOcupados(int ocupados) {
        this.ocupados = ocupados;
    }
    // Método para verificar si el área está llena
    public boolean estaLlena() {
        return ocupados >= capacidad;
    }
}
