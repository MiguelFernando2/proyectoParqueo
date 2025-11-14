/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

import java.time.LocalDateTime;

/**
 *
 * @author DIEGO
 */
public class ReciboSalida {
    public final vehiculo vehiculo;
    public final LocalDateTime horaSalida;
    public final long minutos;
    public final long horas;
    public final double total;
    public final String nota; //valides del plan
    
    public ReciboSalida (vehiculo v, LocalDateTime out, long mins, long hrs, double total, String nota){
     this.vehiculo = v;
     this.horaSalida = out;
     this.minutos = mins;
     this.horas = hrs;
     this.total = total;
     this.nota = nota;
    }

    public vehiculo getVehiculo() {
        return vehiculo;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public long getMinutos() {
        return minutos;
    }

    public long getHoras() {
        return horas;
    }

    public double getTotal() {
        return total;
    }

    public String getNota() {
        return nota;
    }
    
    
    
}
