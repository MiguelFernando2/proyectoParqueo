/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;

//////////////////LOGICA PRINCIPAL CONTROLA SOLO LO QUE ESTA DENTRO DEL PARQUEONEN MEMORIA

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
        
        if (plan.toUpperCase().contains("VARIABLE")){ // redondeo por arriba a la hora siguiente
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

        java.io.File archivo = new java.io.File(nombreArchivo);

    boolean escribirHeader = !archivo.exists() || archivo.length() == 0;

    try (java.io.PrintWriter pw = new java.io.PrintWriter(
            new java.io.BufferedWriter(new java.io.FileWriter(archivo, true)))) {

        if (escribirHeader) {
            // Nuevo encabezado con ROL y AREA
            pw.println("Placa,Propietario,Tipo,Plan,Rol,Area,FechaIngreso");
        }

        for (vehiculo v : getVehiculos()) {
            String rol = (v.getRol() == null || v.getRol().isBlank()) ? "ESTUDIANTE" : v.getRol();
            String area = (v.getArea() == null || v.getArea().isBlank()) ? "ESTUDIANTES" : v.getArea();

            pw.printf("%s,%s,%s,%s,%s,%s,%s%n",
                    v.getPlaca(),
                    v.getPropietario(),
                    v.getTipoVehiculo(),
                    v.getTipoPlan(),
                    v.getRol(),
                    v.getArea(),
                    (v.getHoraIngreso() == null ? java.time.LocalDateTime.now().toString()
                                                : v.getHoraIngreso().toString()));
        }
    } catch (Exception e) {
        throw new RuntimeException("Error al guardar CSV: " + e.getMessage(), e);
    }
}
    public void cargarCSV(String nombreArchivo) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
        String linea;
        boolean primera = true;

        while ((linea = br.readLine()) != null) {
            if (primera) {
                primera = false;
                // salta encabezado típico
                if (linea.toLowerCase().contains("placa")) continue;
            }
            if (linea.trim().isEmpty()) continue;

            String[] datos = linea.split(",");
            for (int i = 0; i < datos.length; i++) datos[i] = datos[i].trim();

            // CSV mínimo: placa, propietario, tipo, plan
            if (datos.length >= 4) {
                String placa       = datos[0];
                String propietario = datos[1];
                String tipo        = datos[2]; // "MOTO" / "CARRO"
                String plan        = datos[3]; // "PLAN (FLAT)" / "TARIFA VARIABLE"

                // Opcionales en el CSV:
                String rolCSV  = (datos.length >= 5) ? datos[4] : "";
                String areaCSV = (datos.length >= 6) ? datos[5] : "";

                // Normalizar ROL para que coincida con lo que guardas
                String rol = rolCSV == null ? "" : rolCSV.trim().toUpperCase();
                if (rol.equals("ESTUDIANTES"))  rol = "ESTUDIANTE";
                if (rol.equals("CATEDRATICOS")) rol = "CATEDRATICO";

                // Determinar área destino (si no viene en CSV)
                String areaNombre;
                if (!areaCSV.isEmpty()) {
                    areaNombre = areaCSV.toUpperCase();
                } else if ("MOTO".equalsIgnoreCase(tipo)) {
                    areaNombre = "MOTOS";
                } else {
                    areaNombre = "ESTUDIANTE".equals(rol) ? "ESTUDIANTES" : "CATEDRATICOS";
                }

                // Evitar duplicados por placa
                if (buscarPorPlaca(placa) != null) {
                    // ya existe -> saltar
                    continue;
                }

                // Respetar cupo del área
                proyectorparqueo.model.Area area = proyectorparqueo.model.DatosApp.getAreaPorNombre(areaNombre);
                if (area != null && area.estaLlena()) {
                    // sin espacio -> saltar
                    continue;
                }

                // Crear vehículo (usa tu constructor con rol y área)
                vehiculo v = new vehiculo(placa, propietario, tipo, plan, true, rol, areaNombre);

                // Agregar al sistema usando el método central (así actualiza ocupación)
                registrarVehiculo(v); // <- este suma area.ocupados()
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
