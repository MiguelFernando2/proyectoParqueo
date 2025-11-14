/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.swing.JOptionPane;

/**
 *
 * @author DIEGO
 */
public class VehiculoDAO {
    
    
    private static final String SQL_INSERT =
        "INSERT INTO Vehiculo " +
        " (placa, propietario, tipoVehiculo, tipoPlan, planActivo, horaIngreso, rol, area) " +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_DELETE_POR_PLACA =
        "DELETE FROM Vehiculo WHERE placa = ?";

    // ============= INSERTAR =============
    public static void insertar(vehiculo v) {
        if (v == null) return;

        try (Connection cn = ConexionSQL.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getPropietario());
            ps.setString(3, v.getTipoVehiculo());
            ps.setString(4, v.getTipoPlan());
            ps.setBoolean(5, v.isPlanActivo());

            Timestamp ts = Timestamp.valueOf(v.getHoraIngreso());
            ps.setTimestamp(6, ts);

            ps.setString(7, v.getRol());
            ps.setString(8, v.getArea());

            int filas = ps.executeUpdate();
            System.out.println("✔ Vehículo guardado en SQL (" + filas + " fila): " + v.getPlaca());
            // Si quieres mantener el mensaje gráfico:
            // JOptionPane.showMessageDialog(null, "Vehículo guardado en SQL: " + v.getPlaca());

        } catch (SQLException e) {
            System.err.println("✘ Error al insertar Vehiculo en SQL: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Error al insertar vehículo en SQL:\n" + e.getMessage());
        }
    }

    // ============= ELIMINAR POR PLACA =============
    public static void eliminarPorPlaca(String placa) {
        if (placa == null || placa.isBlank()) return;

        try (Connection cn = ConexionSQL.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_DELETE_POR_PLACA)) {

            ps.setString(1, placa);
            int filas = ps.executeUpdate();
            System.out.println("✔ Vehículos borrados con placa " + placa + ": " + filas);
            // JOptionPane.showMessageDialog(null, "Vehículos borrados con placa " + placa + ": " + filas);

        } catch (SQLException e) {
            System.err.println("✘ Error al eliminar vehículo en SQL: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar vehículo en SQL:\n" + e.getMessage());
        }
    }
}
