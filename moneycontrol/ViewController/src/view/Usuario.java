package view;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.context.FacesContext;

public class Usuario {
    private String nombre;
    private String nombre_completo;
    private String clave;
    private String mail;
    private boolean admin;
    
    private Categoria[] categorias;
    private Cuenta[] cuentas;
    public Usuario() {
        super();
    }

    public Usuario(String nombre, String nombre_completo, String clave, String mail, boolean admin) {
        super();
        this.nombre = nombre;
        this.nombre_completo = nombre_completo;
        this.clave = clave;
        this.mail = mail;
        this.admin = admin;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }
    
    public boolean getUsuario(String n, String c){  
        Conexion.crear();
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        Statement st;
        ResultSet rs;        
        boolean flag = false;
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        try {
            rs = st.executeQuery("SELECT * FROM USUARIO WHERE nombre = '" + n + "' AND contraseña = '" + c + "'");
        } catch (SQLException e) {
            try {
                st.close();
            } catch (SQLException f) {
                f.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        
        try {
            if(rs.next()){
                setNombre(rs.getString("nombre"));
                setNombre_completo(rs.getString("nombre_completo"));
                setClave(rs.getString("contraseña"));
                setMail(rs.getString("correo"));
                setAdmin(rs.getString("admin") == "1");
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                rs.close();            
                st.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flag;
    }
    
    public boolean getCategorias(String n){
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        Statement st;
        ResultSet rs;        
        boolean flag = false;
        
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        try {
            rs = st.executeQuery("SELECT * FROM CATEGORIA WHERE Usuario_nombre = '" + n + "'");
        } catch (SQLException e) {
            try {
                st.close();
            } catch (SQLException f) {
                f.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        
        try {
            if(rs.next()){
                rs.last();
                categorias = new Categoria[rs.getRow()];
                rs.beforeFirst();
                
                for(int i = 0; i < categorias.length && rs.next(); ++i)
                    categorias[i] = new Categoria(rs.getInt("idCategoria"), rs.getString("nombre"), n);
                
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                rs.close();            
                st.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flag;
    }
    
    public boolean getCuentas(String n){
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        Statement st;
        ResultSet rs;        
        boolean flag = false;
        
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        try {
            rs = st.executeQuery("SELECT * FROM CUENTA WHERE Usuario_nombre = '" + n + "'");
        } catch (SQLException e) {
            try {
                st.close();
            } catch (SQLException f) {
                f.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
        
        try {
            if(rs.next()){
                rs.last();
                cuentas = new Cuenta[rs.getRow()];
                rs.beforeFirst();
                
                for(int i = 0; i < cuentas.length && rs.next(); ++i)
                    cuentas[i] = new Cuenta(rs.getInt("idCuenta"), rs.getString("nombre"), rs.getString("comentario"), rs.getInt("saldo"), n);
                
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                rs.close();            
                st.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flag;
    }
    
    public boolean registrarUsuario(Usuario u){
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        Statement st;
        boolean flag = false;
        
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try {
            st.executeUpdate("INSERT INTO USUARIO(nombre, nombre_completo, contraseña, mail, admin) VALUES("
                             + " '" + u.getNombre() + "',"
                             + " '" + u.getNombre_completo() + "',"
                             + " '" + u.getClave() + "',"
                             + " '" + u.getMail() + "',"
                             + " '" + (u.isAdmin() ? "1" : "0") + "')");
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{            
                st.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flag;
    }
    
    public boolean editarUsuario(Usuario u){
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        Statement st;
        boolean flag = false;
        
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        try {
            st.executeUpdate("UPDATE USUARIO SET"
                             + " nombre_completo = '" + u.getNombre_completo() + "',"
                             + " contraseña = '" + u.getClave() + "',"
                             + " mail = '" + u.getMail() + "',"
                             + " admin = '" + (u.isAdmin() ? "1" : "0") + "'"
                             + " WHERE nombre = '" + u.getNombre() + "'");
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{            
                st.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flag;
    }
    
    public boolean borrarUsuario(String n){
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        Statement st;
        boolean flag = false;
        
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        try {
            st.executeUpdate("DELETE FROM USUARIO WHERE nombre = '" + n + "'");
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{            
                st.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flag;
    }
}
