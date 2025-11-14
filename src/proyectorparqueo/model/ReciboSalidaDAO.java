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
public class ReciboSalidaDAO {
     // Opcional: dejar el SQL como constante
    private static final String SQL_INSERT =
        "INSERT INTO ReciboSalida " +
        "(placa, propietario, tipoVehiculo, tipoPlan, " +
        " horaIngreso, horaSalida, minutos, horas, total, nota) " +
        "VALUES (?,?,?,?,?,?,?,?,?,?)";

    public static void insertar(ReciboSalida r) {
        if (r == null || r.getVehiculo() == null) {
            return;
        }

        try (Connection cn = ConexionSQL.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERT)) {

            vehiculo v = r.getVehiculo();

            // 1–4: datos básicos
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getPropietario());
            ps.setString(3, v.getTipoVehiculo());
            ps.setString(4, v.getTipoPlan());

            // 5–6: fechas/hora
            ps.setTimestamp(5, Timestamp.valueOf(v.getHoraIngreso()));
            ps.setTimestamp(6, Timestamp.valueOf(r.getHoraSalida()));

            // 7: minutos
            ps.setInt(7, (int) r.getMinutos());

            // 8: horas  👈 AQUÍ FALTABA
            ps.setInt(8, (int) r.getHoras());

            // 9: total Q
            ps.setDouble(9, r.getTotal());

            // 10: nota
            ps.setString(10, r.getNota());

            ps.executeUpdate();
            System.out.println("ReciboSalida guardado en SQL para placa " + v.getPlaca());

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                null,
                "Error BD al insertar ReciboSalida: " + ex.getMessage()
            );
        }
    }
}
