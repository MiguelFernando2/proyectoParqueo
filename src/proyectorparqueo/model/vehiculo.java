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
public class vehiculo {
    public String placa;
    public String propietario;
    public String tipoVehiculo;
    public String tipoPlan;
    public boolean planActivo;   
    private LocalDateTime horaIngreso;
    public String area;
    public String rol;

    public vehiculo(String placa, String propietario, String tipoVehiculo, String tipoPlan, boolean planActivo) {
        this.placa = placa;
        this.propietario = propietario;
        this.tipoVehiculo = tipoVehiculo;
        this.tipoPlan = tipoPlan;
        this.planActivo = planActivo; 
        this.horaIngreso = LocalDateTime.now();
        this.area = "ESTUDIANTE";
    }

    public vehiculo(String placa, String propietario, String tipoVehiculo, String tipoPlan, boolean planActivo, String area) {
        this.placa = placa;
        this.propietario = propietario;
        this.tipoVehiculo = tipoVehiculo;
        this.tipoPlan = tipoPlan;
        this.planActivo = planActivo;
        this.horaIngreso = LocalDateTime.now();
        this.area = (area == null || area.isEmpty()) ? "ESTUDIANTE" : area;
    }
    
    
    
    public String getPlaca() {
        return placa;
    }
    public String getPropietario() {
        return propietario;
    }
    public String getTipoVehiculo(){
        return tipoVehiculo;
    }
    public String getTipoPlan() {
        return tipoPlan;
    }
    public boolean isPlanActivo() {
        return planActivo;
    }
    public LocalDateTime getHoraIngreso(){
        return horaIngreso;
    }
    public String getArea(){
        return area;
    }
    public String rol(){
        return rol;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
    public void setTipoVehiculo(String tipoVehiculo){
        this.tipoVehiculo = tipoVehiculo;
    }
    public void setTipoPlan(String tipoPlan) {
        this.tipoPlan = tipoPlan;
    }
    public void setPlanActivo(boolean planActivo) {
        this.planActivo = planActivo;
    }
    public void setHoraIngreso(LocalDateTime h){
        this.horaIngreso = h;
    }
    public void setArea(String area){
        this.area = area;
    }
    public void setrol(String rol){
        this.rol = rol;
    }
    
    public void cancelarPlan(){
        this.planActivo = false;
    }
    public void reactivarPlan(){
        this.planActivo = true;
    }
    
    public void mostrarDatos(){
        System.out.println("Placa: " + placa);
        System.out.println("Propietario:" + propietario);
        System.out.println("Tipo de vehicula: " + tipoVehiculo);
        System.out.println("Tipo de PLan: " + tipoPlan);
        System.out.println("Plan Activo: " + planActivo);
        //System.out.println("Area: " + area);
       // System.out.println("Hora ingresado: " + horaIngreso);
    }
    
}
