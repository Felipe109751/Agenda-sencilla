/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.*;
import javax.swing.table.DefaultTableModel;
import Modelo.ConexionBD;

public class Controlador {

    public void cargarDatosTabla(DefaultTableModel modelo) {
        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nombres, apellidos, telefono, direccion, email FROM datos")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("telefono"),
                    rs.getString("direccion"),
                    rs.getString("email")
                });
            }
        } catch (Exception e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
    }

    public DefaultTableModel listarContactos() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("nombres");
        modelo.addColumn("teléfono");
        modelo.addColumn("email");
        modelo.addColumn("dirección");

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nombres, apellidos, telefono, email, direccion FROM datos")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombres"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelo;
    }

    public String agregarContacto(String nombres, String apellidos, String telefono, String direccion, String email) {
        if (nombres.isEmpty() || apellidos.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || email.isEmpty()) {
            return "No se permiten campos vacíos.";
        }

        String query = "INSERT INTO datos(id, nombres, apellidos, telefono, direccion, email) VALUES (null, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, nombres);
            ps.setString(2, apellidos);
            ps.setString(3, telefono);
            ps.setString(4, direccion);
            ps.setString(5, email);
            ps.executeUpdate();
            return "Contacto agregado correctamente.";
        } catch (SQLException e) {
            return "Error al agregar contacto.";
        }
    }

    public static void consultarContactos(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        String query = "SELECT * FROM datos";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
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
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return (filas > 0) ? "Contacto eliminado." : "ID no encontrado.";
        } catch (SQLException e) {
            return "Error al eliminar contacto.";
        }
    }

    public static String editarContacto(int id, String nombres, String apellidos, String direccion, String email) {
        String query = "UPDATE datos SET nombres = ?, apellidos = ?, direccion = ?, email = ? WHERE id = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
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

    public DefaultTableModel buscarPorNombre(String nombre) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("nombres");
        modelo.addColumn("apellidos");
        modelo.addColumn("teléfono");
        modelo.addColumn("email");
        modelo.addColumn("dirección");

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT nombres, apellidos, telefono, email, direccion FROM datos WHERE nombres LIKE ?")) {
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelo;
    }

    public DefaultTableModel buscarPorID(int id) {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("ID");
    modelo.addColumn("Nombre");
    modelo.addColumn("Apellido");
    modelo.addColumn("Teléfono");
    modelo.addColumn("Correo");
    modelo.addColumn("Dirección");

    String query = "SELECT id, nombres, apellidos, telefono, email, direccion FROM datos WHERE id = ?";
    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("direccion")
            });
        }
    } catch (SQLException e) {
        System.out.println("Error al buscar por ID: " + e.getMessage());
    }

    return modelo;
}
    
    
    public DefaultTableModel buscarPorTelefono(String telefono) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("nombres");
        modelo.addColumn("teléfono");
        modelo.addColumn("email");
        modelo.addColumn("dirección");

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT nombres, telefono, email, direccion FROM datos WHERE telefono LIKE ?")) {
            ps.setString(1, "%" + telefono + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombres"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelo;
    }

    public static void buscarPorApellido(DefaultTableModel modelo, String apellido) {
        modelo.setRowCount(0);
        String query = "SELECT * FROM datos WHERE apellidos LIKE ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
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

    public DefaultTableModel filtrarPorCiudad(String ciudad) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("nombres");
        modelo.addColumn("teléfono");
        modelo.addColumn("email");
        modelo.addColumn("dirección");

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT nombres, telefono, email, direccion FROM datos WHERE direccion LIKE ?")) {
            ps.setString(1, "%" + ciudad + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombres"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelo;
    }

    public DefaultTableModel buscarPorRangoID(int desde, int hasta) {
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("ID");
    modelo.addColumn("Nombre");
    modelo.addColumn("Apellido");
    modelo.addColumn("Teléfono");
    modelo.addColumn("Correo");
    modelo.addColumn("Dirección");

    String query = "SELECT id, nombres, apellidos, telefono, email, direccion FROM datos WHERE id BETWEEN ? AND ?";
    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, desde);
        ps.setInt(2, hasta);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("direccion")
            });
        }
    } catch (SQLException e) {
        System.out.println("Error al buscar por rango de ID: " + e.getMessage());
    }

    return modelo;
}
    
    
    
    public DefaultTableModel listarContactosOrdenados() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("nombres");
        modelo.addColumn("apellidos");
        modelo.addColumn("teléfono");
        modelo.addColumn("email");
        modelo.addColumn("dirección");

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT nombres, apellidos, telefono, email, direccion FROM datos ORDER BY nombres ASC");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelo;
    }

    public DefaultTableModel buscarPorNombreCompleto(String nombreCompleto) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("nombres");
        modelo.addColumn("apellidos");
        modelo.addColumn("teléfono");
        modelo.addColumn("email");
        modelo.addColumn("dirección");

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT nombres, apellidos, telefono, email, direccion FROM datos WHERE CONCAT(nombres, ' ', apellidos) LIKE ?")) {
            ps.setString(1, "%" + nombreCompleto + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelo;
    }

    public static DefaultTableModel mostrarContactosOrdenados() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Teléfono");

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT nombres, telefono FROM datos ORDER BY nombres ASC");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombres"),
                    rs.getString("telefono")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return modelo;
    }

    public boolean eliminarContacto(String nombre) {
        String sql = "DELETE FROM datos WHERE nombres = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void listarPorRangoID(DefaultTableModel modelo, int desde, int hasta) {
        modelo.setRowCount(0);
        String query = "SELECT * FROM datos WHERE id BETWEEN ? AND ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
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
}
