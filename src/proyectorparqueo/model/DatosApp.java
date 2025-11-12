/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author DIEGO
 */
public class DatosApp {
    // Instancia compartida del “negocio”
    public static final controlParqueo PARQUEO = new controlParqueo();

    // TARIFAS (por hora)
    public static final double TARIFA_CARRO_POR_HORA = 10.0;
    public static final double TARIFA_MOTO_POR_HORA  = 6.0;

    // === Catálogo de ÁREAS con capacidad (clave exacta: MOTOS, ESTUDIANTES, CATEDRATICOS)
    private static final Map<String, Area> AREAS = new HashMap<>();
    static {
        AREAS.put("MOTOS",        new Area("A01", "MOTOS",        120, "MOTO"));
        AREAS.put("ESTUDIANTES",  new Area("A02", "ESTUDIANTES",  220, "AUTO"));
        AREAS.put("CATEDRATICOS", new Area("A03", "CATEDRATICOS",  60, "AUTO"));
    }

    // Helper: buscar área por nombre
    public static Area getAreaPorNombre(String nombre) {
        if (nombre == null) return null;
        return AREAS.get(nombre.trim().toUpperCase());
    }

    // Helper: % ocupación
    public static double porcentajeOcupacion(Area a) {
        if (a == null || a.getCapacidad() <= 0) return 0.0;
        return (a.getOcupados() * 100.0) / a.getCapacidad();
    }

    // === Registrar SALIDA y devolver Recibo ===
    public static ReciboSalida registrarSalida(String placa) {
        vehiculo v = PARQUEO.buscarPorPlaca(placa);
        if (v == null) return null;

        // seguridad por si horaIngreso viniera null
        if (v.getHoraIngreso() == null) {
            v.setHoraIngreso(LocalDateTime.now());
        }

        LocalDateTime salida = LocalDateTime.now();
        long minutos = Duration.between(v.getHoraIngreso(), salida).toMinutes();
        long horas = (minutos + 59) / 60; // redondeo hacia arriba
        if (horas == 0) horas = 1;

        String nota = "";
        if ("PLAN (FLAT)".equalsIgnoreCase(v.getTipoPlan())) {
            if (minutos > 120) {
                nota = "El plan FLAT salió y no volvió en 2 horas: plan cancelado.";
            }
        }

        double tarifa = "MOTO".equalsIgnoreCase(v.getTipoVehiculo())
                ? TARIFA_MOTO_POR_HORA
                : TARIFA_CARRO_POR_HORA;
        double total = horas * tarifa;

        // Liberar cupo del área (si está seteada)
        Area a = getAreaPorNombre(v.getArea());
        if (a != null && a.getOcupados() > 0) {
            a.setOcupados(a.getOcupados() - 1);
        }
        // Quita el sistema
        PARQUEO.eliminarVehiculo(placa);

        // Sacar el vehículo del sistema
        PARQUEO.eliminarVehiculo(placa);

        registrarHistorialSalida(new ReciboSalida(v, salida, minutos, horas, total, nota));
        
        return new ReciboSalida(v, salida, minutos, horas, total, nota);
    }

    private static final java.util.Map<String, ReciboSalida> HISTORIAL_SALIDAS = new java.util.HashMap<>();

// Guardar la salida registrada (para usar luego en reingreso)
    public static void registrarHistorialSalida(ReciboSalida r) {
        if (r != null && r.getVehiculo() != null) {
            HISTORIAL_SALIDAS.put(r.getVehiculo().getPlaca(), r);
        }
    }

// Obtener el último recibo de salida según placa
    public static ReciboSalida getHistorialSalida(String placa) {
        return HISTORIAL_SALIDAS.get(placa);
    }
    
    // Evitar instancias
    private DatosApp() {}
}
