package view;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Usuario {
    private String nombre;
    private String nombre_completo;
    private String clave;
    private String correo;
    private boolean admin;
    
    private Categoria[] categorias;
    private Cuenta[] cuentas;
    public Usuario() {
        super();
    }

    public Usuario(String nombre, String nombre_completo, String clave, String correo, boolean admin) {
        super();
        this.nombre = nombre;
        this.nombre_completo = nombre_completo;
        this.clave = clave;
        this.correo = correo;
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

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public Categoria[] getCategorias() {
        return categorias;
    }

    public Cuenta[] getCuentas() {
        return cuentas;
    }
    
    public boolean getUsuario(String n, String c){
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
                setCorreo(rs.getString("correo"));
                setAdmin(rs.getString("admin") == "1");
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                rs.close();            
                //st.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flag;
    }
    
    public boolean setCategorias(String n){
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        Statement st;
        ResultSet rs;        
        boolean flag = false;
        
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                     ResultSet.CONCUR_UPDATABLE);
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
    
    public boolean setCuentas(String n){
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        Statement st;
        ResultSet rs;        
        boolean flag = false;
        
        try {
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                     ResultSet.CONCUR_UPDATABLE);
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
            st.executeUpdate("INSERT INTO USUARIO(nombre, nombre_completo, contraseña, correo, admin) VALUES("
                             + " '" + u.getNombre() + "',"
                             + " '" + u.getNombre_completo() + "',"
                             + " '" + u.getClave() + "',"
                             + " '" + u.getCorreo() + "',"
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
                             + " correo = '" + u.getCorreo() + "',"
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
