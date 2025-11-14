/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

import proyectorparqueo.model.VehiculoDAO;
import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DIEGO
 */
public class DatosApp {
    
    // Vehículos FLAT que salieron pero aún están dentro de la ventana de reingreso
    public static final java.util.Map<String, java.time.LocalDateTime> PENDIENTES_FLAT =
        new java.util.HashMap<>();
    
    // Instancia compartida del “negocio”
    public static final controlParqueo PARQUEO = new controlParqueo();
    
    // 🔹 Historial de salidas (lista)
    public static final List<ReciboSalida> HISTORIAL_SALIDAS = new ArrayList<>();

    // TARIFAS (por hora)
    public static final double TARIFA_CARRO_POR_HORA = 10.0;
    public static final double TARIFA_MOTO_POR_HORA  = 6.0;

    // === Catálogo de ÁREAS con capacidad (clave exacta: MOTOS, ESTUDIANTES, CATEDRATICOS)
    private static final Map<String, Area> AREAS = new HashMap<>();
    static {
        AREAS.put("MOTOS",        new Area("A01", "MOTOS",         4, "MOTO"));
        AREAS.put("ESTUDIANTES",  new Area("A02", "ESTUDIANTES",   4, "AUTO"));
        AREAS.put("CATEDRATICOS", new Area("A03", "CATEDRATICOS",  4, "AUTO"));
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

    // 1) Actualizar ocupados físicos del área (adentro)
    Area area = getAreaPorNombre(v.getArea());
    if (area != null && area.getOcupados() > 0) {
        area.setOcupados(area.getOcupados() - 1);
    }

    // 2) Calcular tiempos
    LocalDateTime salida = LocalDateTime.now();
    long minutos = Duration.between(v.getHoraIngreso(), salida).toMinutes();
    long horas = (minutos + 59) / 60;   // redondeo hacia arriba
    if (horas == 0) horas = 1;

    double total = 0.0;
    String nota  = "";
    String plan  = (v.getTipoPlan() == null) ? "" : v.getTipoPlan().toUpperCase();

    // monto que se cobró en el ingreso por ser PLAN FLAT
    double montoFlat = v.getTipoVehiculo().equalsIgnoreCase("MOTO")
            ? 25.0        // Q25 para moto
            : 40.0;       // Q40 para carro

    // 3) Lógica según plan
    if (plan.contains("FLAT")) {
        // PLAN (FLAT): ya pagó al ingresar

        if (minutos <= 120) {
            // Sale pero todavía está dentro de la ventana de 2 horas
            PENDIENTES_FLAT.put(placa.toUpperCase(), salida);
            nota = "PLAN (FLAT): pagó Q " + montoFlat +
                   " al ingresar. Salida dentro de 2 horas, reingreso permitido.";
        } else {
            // Más de 2h fuera: se considera que el plan terminó
            PENDIENTES_FLAT.remove(placa.toUpperCase());
            nota = "PLAN (FLAT): pagó Q " + montoFlat +
                   " al ingresar. Más de 2 horas fuera: plan finalizado.";
        }

        // 🔴 Guardamos en el recibo lo que realmente se pagó
        total = montoFlat;

    } else {
        // VARIABLE: cobrar por horas
        double tarifa = v.getTipoVehiculo().equalsIgnoreCase("MOTO")
                ? TARIFA_MOTO_POR_HORA
                : TARIFA_CARRO_POR_HORA;
        total = horas * tarifa;
    }

    // 4) Sacar del parqueo (ya no está físicamente adentro)
    PARQUEO.eliminarVehiculo(placa);

    // 5) Crear recibo
    ReciboSalida r = new ReciboSalida(v, salida, minutos, horas, total, nota);

    // Guardar en memoria
    HISTORIAL_SALIDAS.add(r);

    // 6) Guardar en BD
    ReciboSalidaDAO.insertar(r);         // Inserta el recibo
    VehiculoDAO.eliminarPorPlaca(placa); // Borra el vehículo de la tabla Vehiculo

    return r;
}
    // 🔎 Buscar la ÚLTIMA salida registrada de esa placa
    public static ReciboSalida ultimaSalidaDe(String placa) {
        if (placa == null || placa.isEmpty()) return null;

        for (int i = HISTORIAL_SALIDAS.size() - 1; i >= 0; i--) {
            ReciboSalida r = HISTORIAL_SALIDAS.get(i);
            if (r.getVehiculo().getPlaca().equalsIgnoreCase(placa)) {
                return r;
            }
        }
        return null;
    }

    // Evitar instancias
    private DatosApp() {}
}