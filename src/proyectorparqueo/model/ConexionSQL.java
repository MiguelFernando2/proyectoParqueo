/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectorparqueo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author DIEGO
 */
public class ConexionSQL {
    // URL a tu instancia SQLEXPRESS y BD Parqueo2025
    private static final String URL =
        "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=Parqueo2025;"
      + "encrypt=false;trustServerCertificate=true;";

    // usuario y contraseña que creaste en SQL Server
    private static final String USER = "parqueoUser";
    private static final String PASS = "Parqueo123*";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // pruebitas
    public static void main(String[] args) {
        try (Connection cn = getConnection()) {
            JOptionPane.showMessageDialog(null, "CONECTADO OK A SQL SEREVER.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR AL CONECTAR ");
        }
    }
    
}
