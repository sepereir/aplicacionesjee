package view;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

public class Usuario {
    
    //Variables de instancia
    private String nombre;
    private String nombre_completo;
    private String clave;
    private String correo;
    private boolean admin;
    private Categoria[] categorias;
    private Cuenta[] cuentas;
    ArrayList<Categoria> categoriasList;
    ArrayList<Cuenta> cuentasList;
    
    //Constructores
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
    
    //Setters & Getters
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

    public ArrayList<Categoria> getCategoriasList(){
        categoriasList = new ArrayList<Categoria>();
        for(Categoria c : getCategorias())
            categoriasList.add(c);
        return categoriasList;
    }

    public Cuenta[] getCuentas() {
        return cuentas;
    }
    
    public ArrayList<Cuenta> getCuentasList(){
        cuentasList = new ArrayList<Cuenta>();
        for(Cuenta c : getCuentas())
            cuentasList.add(c);
        return cuentasList;        
    }
    //Fin de Setters & Getters
    
    //Obtener objeto de la BD
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
    
    //Obtener categorias de este usuario
    public boolean setCategorias(){
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
            rs = st.executeQuery("SELECT * FROM CATEGORIA WHERE Usuario_nombre = '" + getNombre() + "'");
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
                    categorias[i] = new Categoria(rs.getInt("idCategoria"), rs.getString("nombre"), getNombre());
                
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
    
    //Obtener cuentas de este usuario
    public boolean setCuentas(){
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
            rs = st.executeQuery("SELECT * FROM CUENTA WHERE Usuario_nombre = '" + getNombre() + "' ORDER BY nombre");
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
                    cuentas[i] = new Cuenta(
                        rs.getInt("idCuenta"),
                        rs.getString("nombre"),
                        rs.getString("comentario"),
                        rs.getInt("saldo"),
                        getNombre());
                
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
    
    //Insertar objeto en la BD
    public boolean registrarUsuario(){
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
                             + " '" + getNombre() + "',"
                             + " '" + getNombre_completo() + "',"
                             + " '" + getClave() + "',"
                             + " '" + getCorreo() + "',"
                             + " '" + (isAdmin() ? "1" : "0") + "')");
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
    
    //Actualizar objeto de la BD
    public boolean editarUsuario(){
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
                             + " nombre_completo = '" + getNombre_completo() + "',"
                             + " contraseña = '" + getClave() + "',"
                             + " correo = '" + getCorreo() + "',"
                             + " admin = '" + (isAdmin() ? "1" : "0") + "'"
                             + " WHERE nombre = '" + getNombre() + "'");
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
    
    //Borrar objeto de la BD
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
