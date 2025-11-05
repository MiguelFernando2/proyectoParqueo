/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

import java.time.*;

/**
 *
 * @author DIEGO
 */
public class DatosApp {
    //la instancia unica y compartida del controlParqueo
    public static final controlParqueo PARQUEO = new controlParqueo();
    
    //TARIFAS
    public static final double TARIFA_CARRO_POR_HORA = 10.0;
    public static final double TARIFA_MOTO_POR_HORA =6.0;
    
    public static ReciboSalida registrarSalida(String placa){
        vehiculo v = PARQUEO.buscarPorPlaca(placa);
        if (v == null) return null;
        
        LocalDateTime salida = LocalDateTime.now();
        long minutos = Duration.between(v.getHoraIngreso(), salida).toMinutes();
        long horas = (minutos +59) / 60;
        if (horas == 0) horas = 1;
        double total = 0.0;
        String nota = "";
        
        if (v.getTipoPlan().equalsIgnoreCase("PLAN (FLAT")){
            if (minutos > 120){
                nota = "El plan FLAT salio y no volcio en 2 horas: plan cancelad0, ";
            }else {
                double tarifa = v.getTipoVehiculo().equalsIgnoreCase("MOTO") ? 
                        TARIFA_MOTO_POR_HORA : TARIFA_CARRO_POR_HORA;
                total = horas * tarifa;
            }
            //liberar espacio sacando vehiculo del sistema
            PARQUEO.eliminarVehiculo(placa);
            
            return new ReciboSalida(v, salida, minutos, horas, total, nota);
        }
    }

    //un constructor privado para que no crea objetos de esta clase
    private DatosApp(){}
    
}
