/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

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

    // ↓↓↓ ya lo tenías, no lo borres
    Area area = getAreaPorNombre(v.getArea());
    if (area != null && area.getOcupados() > 0) {
        area.setOcupados(area.getOcupados() - 1);
    }

    LocalDateTime salida = LocalDateTime.now();
    long minutos = Duration.between(v.getHoraIngreso(), salida).toMinutes();
    long horas = (minutos + 59) / 60;
    if (horas == 0) horas = 1;

    double total = 0.0;
    String nota = "";
    String plan = v.getTipoPlan() == null ? "" : v.getTipoPlan().toUpperCase();

    if (plan.contains("FLAT")) {
        // 👉 guardar como "pendiente" mientras está en ventana de 2h
        if (minutos <= 120) {
            PENDIENTES_FLAT.put(placa.toUpperCase(), salida);
            nota = "PLAN (FLAT): salida dentro de 2 horas, reingreso pendiente.";
        } else {
            // si ya pasó de 2h, que no quede pendiente
            PENDIENTES_FLAT.remove(placa.toUpperCase());
            nota = "PLAN (FLAT): ya habías pagado al ingresar. Más de 2 horas fuera: plan se considera terminado.";
        }
        // No se cobra en la salida
    } else {
        double tarifa = v.getTipoVehiculo().equalsIgnoreCase("MOTO")
                ? TARIFA_MOTO_POR_HORA
                : TARIFA_CARRO_POR_HORA;
        total = horas * tarifa;
    }

    PARQUEO.eliminarVehiculo(placa);

    ReciboSalida r = new ReciboSalida(v, salida, minutos, horas, total, nota);
    HISTORIAL_SALIDAS.add(r);

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