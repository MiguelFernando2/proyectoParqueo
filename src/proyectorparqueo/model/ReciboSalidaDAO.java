/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

//////GUARDA Y LEE SQL

public class ReciboSalidaDAO {

    /////// SENTENCIAS SQL 
    private static final String SQL_INSERT =
        "INSERT INTO ReciboSalida " +
        " (placa, propietario, tipoVehiculo, tipoPlan, " +
        "  horaIngreso, horaSalida, minutos, horas, total, nota) " +
        " VALUES (?,?,?,?,?,?,?,?,?,?)";

    private static final String SQL_SELECT_TODOS =
        "SELECT placa, propietario, tipoVehiculo, tipoPlan, " +
        "       horaIngreso, horaSalida, minutos, horas, total, nota " +
        "FROM ReciboSalida";

    private static final String SQL_SELECT_RANGO =
        "SELECT placa, propietario, tipoVehiculo, tipoPlan, " +
        "       horaIngreso, horaSalida, minutos, horas, total, nota " +
        "FROM ReciboSalida " +
        "WHERE horaSalida BETWEEN ? AND ?";

    ///////////////INSERTAR 
    public static void insertar(ReciboSalida r) {
        if (r == null || r.getVehiculo() == null) return;

        try (Connection cn = ConexionSQL.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERT)) {

            vehiculo v = r.getVehiculo();

            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getPropietario());
            ps.setString(3, v.getTipoVehiculo());
            ps.setString(4, v.getTipoPlan());
            ps.setTimestamp(5, Timestamp.valueOf(v.getHoraIngreso()));
            ps.setTimestamp(6, Timestamp.valueOf(r.getHoraSalida()));
            ps.setLong(7, r.getMinutos());
            ps.setLong(8, r.getHoras());
            ps.setDouble(9, r.getTotal());
            ps.setString(10, r.getNota());

            ps.executeUpdate();
            System.out.println("  ReciboSalida guardado en SQL (placa: " + v.getPlaca() + ")");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                null,
                "Error BD al insertar ReciboSalida:\n" + ex.getMessage()
            );
        }
    }

//////////////7// LISTAR TODOS (para cargar en memoria) 
    public static List<ReciboSalida> listarTodos() {
        List<ReciboSalida> lista = new ArrayList<>();

        try (Connection cn = ConexionSQL.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ReciboSalida r = mapearRecibo(rs);
                if (r != null) {
                    lista.add(r);
                }
            }

        } catch (SQLException ex) {
            System.err.println("✘ Error al listar ReciboSalida desde SQL: " + ex.getMessage());
        }

        return lista;
    }

//////////////////7// LISTAR POR RANGO DE FECHAS (para reportes por periodo) 
    public static List<ReciboSalida> listarPorRango(LocalDateTime desde, LocalDateTime hasta) {
        List<ReciboSalida> lista = new ArrayList<>();
        if (desde == null || hasta == null) return lista;

        try (Connection cn = ConexionSQL.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_SELECT_RANGO)) {

            ps.setTimestamp(1, Timestamp.valueOf(desde));
            ps.setTimestamp(2, Timestamp.valueOf(hasta));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReciboSalida r = mapearRecibo(rs);
                    if (r != null) {
                        lista.add(r);
                    }
                }
            }

        } catch (SQLException ex) {
            System.err.println("✘ Error al listar ReciboSalida por rango: " + ex.getMessage());
        }

        return lista;
    }

////////////////////7 MAPEAR UNA FILA A ReciboSalida 
    private static ReciboSalida mapearRecibo(ResultSet rs) {
        try {
            String placa       = rs.getString("placa");
            String propietario = rs.getString("propietario");
            String tipo        = rs.getString("tipoVehiculo");
            String plan        = rs.getString("tipoPlan");

            LocalDateTime horaIng = rs.getTimestamp("horaIngreso").toLocalDateTime();
            LocalDateTime horaSal = rs.getTimestamp("horaSalida").toLocalDateTime();
            long minutos          = rs.getLong("minutos");
            long horas            = rs.getLong("horas");
            double total          = rs.getDouble("total");
            String nota           = rs.getString("nota");

            // Como en la tabla ReciboSalida NO guardamos rol ni área,
            // ponemos valores razonables de apoyo (solo para reportes).
            String rol  = "ESTUDIANTE";
            String area = (tipo != null && tipo.equalsIgnoreCase("MOTO"))
                          ? "MOTOS"
                          : "ESTUDIANTES";

            vehiculo v = new vehiculo(
                    placa,
                    propietario,
                    tipo,
                    plan,
                    true,        // planActivo (no afecta reportes)
                    horaIng,
                    rol,
                    area
            );

            return new ReciboSalida(v, horaSal, minutos, horas, total, nota);

        } catch (SQLException ex) {
            System.err.println("✘ Error al mapear ReciboSalida: " + ex.getMessage());
            return null;
        }
    }
}