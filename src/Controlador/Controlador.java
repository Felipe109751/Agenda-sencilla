/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author PC FELIPE
 */

public class Controlador {

    private static final String URL = "jdbc:mysql://localhost:3306/agenda";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "root";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (SQLException e) {
            return null;
        }
    }

    public static String agregarContacto(String nombres, String apellidos, String direccion, String email) {
        if (nombres.isEmpty() || apellidos.isEmpty() || direccion.isEmpty() || email.isEmpty()) {
            return "No se permiten campos vacíos.";
        }

        String query = "INSERT INTO datos(id, nombres, apellidos, direccion, email) VALUES (null, ?, ?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nombres);
            ps.setString(2, apellidos);
            ps.setString(3, direccion);
            ps.setString(4, email);
            ps.executeUpdate();
            return "Contacto agregado correctamente.";
        } catch (SQLException e) {
            return "Error al agregar contacto.";
        }
    }

    public static void consultarContactos(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        String query = "SELECT * FROM datos";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("direccion"),
                    rs.getString("email")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar contactos.");
        }
    }

    public static String borrarContacto(int id) {
        String query = "DELETE FROM datos WHERE id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return (filas > 0) ? "Contacto eliminado." : "ID no encontrado.";
        } catch (SQLException e) {
            return "Error al eliminar contacto.";
        }
    }

    public static String editarContacto(int id, String nombres, String apellidos, String direccion, String email) {
        String query = "UPDATE datos SET nombres = ?, apellidos = ?, direccion = ?, email = ? WHERE id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nombres);
            ps.setString(2, apellidos);
            ps.setString(3, direccion);
            ps.setString(4, email);
            ps.setInt(5, id);
            int filas = ps.executeUpdate();
            return (filas > 0) ? "Contacto actualizado." : "ID no encontrado.";
        } catch (SQLException e) {
            return "Error al editar contacto.";
        }
    }

    public static void listarPorCiudad(DefaultTableModel modelo, String ciudad) {
        modelo.setRowCount(0);
        String query = "SELECT * FROM datos WHERE direccion LIKE ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, "%" + ciudad + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("direccion"),
                    rs.getString("email")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error al listar por ciudad.");
        }
    }

    public static void listarPorRangoID(DefaultTableModel modelo, int desde, int hasta) {
        modelo.setRowCount(0);
        String query = "SELECT * FROM datos WHERE id BETWEEN ? AND ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, desde);
            ps.setInt(2, hasta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("direccion"),
                    rs.getString("email")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error al listar por rango de ID.");
        }
    }

    public static void buscarPorNombre(DefaultTableModel modelo, String nombre) {
        modelo.setRowCount(0);
        String query = "SELECT * FROM datos WHERE nombres LIKE ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("direccion"),
                    rs.getString("email")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por nombre.");
        }
    }

    public static void buscarPorApellido(DefaultTableModel modelo, String apellido) {
        modelo.setRowCount(0);
        String query = "SELECT * FROM datos WHERE apellidos LIKE ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, apellido + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("direccion"),
                    rs.getString("email")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por apellido.");
        }
    }
}