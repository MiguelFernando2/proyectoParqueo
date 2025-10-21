/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

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

    public vehiculo(String placa, String propietario, String tipoVehiculo, String tipoPlan, boolean planActivo) {
        this.placa = placa;
        this.propietario = propietario;
        this.tipoVehiculo = tipoVehiculo;
        this.tipoPlan = tipoPlan;
        this.planActivo = planActivo;
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
    
    public void cancelarPlan(){
        this.planActivo = false;
    }
    public void reactivarPlan(){
        this.planActivo = false;
    }
    
    public void mostrarDatos(){
        System.out.println("Placa: " + placa);
        System.out.println("Propietario:" + propietario);
        System.out.println("Tipo de vehicula: " + tipoVehiculo);
        System.out.println("Tipo de PLan: " + tipoPlan);
        System.out.println("Plan Activo: " + planActivo);
    }
    
}
