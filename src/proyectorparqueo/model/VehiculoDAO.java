/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;


// GUARDAN Y LEE SQL

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

///////////////////////  LISTAR TODOS 
    public static List<vehiculo> listarTodos() {
        List<vehiculo> lista = new ArrayList<>();

        String sql = "SELECT placa, propietario, tipoVehiculo, tipoPlan, " +
                     "       planActivo, horaIngreso, rol, area " +
                     "FROM Vehiculo";

        try (Connection cn = ConexionSQL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String placa       = rs.getString("placa");
                String propietario = rs.getString("propietario");
                String tipo        = rs.getString("tipoVehiculo");
                String plan        = rs.getString("tipoPlan");
                boolean planActivo = rs.getBoolean("planActivo");
                Timestamp tsIng    = rs.getTimestamp("horaIngreso");
                String rol         = rs.getString("rol");
                String area        = rs.getString("area");

                java.time.LocalDateTime horaIng =
                        (tsIng != null) ? tsIng.toLocalDateTime()
                                        : java.time.LocalDateTime.now();

                // Por si hay datos viejos con rol/area NULL en la BD:
                if (rol == null || rol.isBlank()) {
                    rol = "ESTUDIANTE";
                }
                if (area == null || area.isBlank()) {
                    if ("MOTO".equalsIgnoreCase(tipo)) {
                        area = "MOTOS";
                    } else {
                        area = rol.equalsIgnoreCase("CATEDRATICO")
                                ? "CATEDRATICOS"
                                : "ESTUDIANTES";
                    }
                }

                vehiculo v = new vehiculo(
                        placa, propietario, tipo, plan,
                        planActivo, horaIng, rol, area
                );
                lista.add(v);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar vehículos desde SQL: " + e.getMessage());
        }

        return lista;
    }

/////////////////////////  CONSTANTES SQL 
    private static final String SQL_INSERT =
        "INSERT INTO Vehiculo " +
        " (placa, propietario, tipoVehiculo, tipoPlan, planActivo, horaIngreso, rol, area) " +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_DELETE_POR_PLACA =
        "DELETE FROM Vehiculo WHERE placa = ?";

//////////////////////  INSERTAR 
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

//////////////////////// ROL (nunca NULL) 
            String rol = v.getRol();
            if (rol == null || rol.isBlank()) {
                rol = "ESTUDIANTE";
            }
            rol = rol.trim().toUpperCase();
            ps.setString(7, rol);

///////////////////////// ÁREA (nunca NULL) 
            String area = v.getArea();
            if (area == null || area.isBlank()) {
                if ("MOTO".equalsIgnoreCase(v.getTipoVehiculo())) {
                    area = "MOTOS";
                } else {
                    area = rol.equals("CATEDRATICO")
                            ? "CATEDRATICOS"
                            : "ESTUDIANTES";
                }
            }
            area = area.trim().toUpperCase();
            ps.setString(8, area);

            int filas = ps.executeUpdate();
            System.out.println("✔ Vehículo guardado en SQL (" + filas + " fila): " + v.getPlaca());

        } catch (SQLException e) {
            System.err.println("✘ Error al insertar Vehiculo en SQL: " + e.getMessage());
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Error al insertar vehículo en SQL:\n" + e.getMessage()
            );
        }
    }

////////////////////////    ELIMINAR POR PLACA 
    public static void eliminarPorPlaca(String placa) {
        if (placa == null || placa.isBlank()) return;

        try (Connection cn = ConexionSQL.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_DELETE_POR_PLACA)) {

            ps.setString(1, placa);
            int filas = ps.executeUpdate();
            System.out.println("✔ Vehículos borrados con placa " + placa + ": " + filas);

        } catch (SQLException e) {
            System.err.println("✘ Error al eliminar vehículo en SQL: " + e.getMessage());
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Error al eliminar vehículo en SQL:\n" + e.getMessage()
            );
        }
    }
}
