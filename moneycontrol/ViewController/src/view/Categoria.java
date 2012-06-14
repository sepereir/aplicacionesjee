package view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;

public class Categoria {
    private int id;
    private String nombre;
    private String usuario;
    private Transaccion[] gastos;
    public Categoria() {
        super();
    }

    public Categoria(int id, String nombre, String usuario) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
    }

    public Categoria(String nombre, String usuario) {
        super();
        this.id = -1;
        this.nombre = nombre;
        this.usuario = usuario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public Transaccion[] getGastos() {
        return gastos;
    }
    
    public boolean getCategoria(int x){
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
            rs = st.executeQuery("SELECT * FROM CATEGORIA WHERE idCategoria = " + x);
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
                setId(rs.getInt("idCategoria"));
                setNombre(rs.getString("nombre"));
                setUsuario(rs.getString("Usuario_nombre"));
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
    
    public boolean crearCategoria(Categoria c){
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
            st.executeUpdate("INSERT INTO CATEGORIA(nombre, Usuario_nombre) VALUES("
                             + " '" + c.getNombre() + "',"
                             + " '" + c.getUsuario() + "')");
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
    
    public boolean editarCategoria(Categoria c){
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
            st.executeUpdate("UPDATE CATEGORIA SET"
                             + " nombre = '" + c.getNombre() + "',"
                             + " Usuario_nombre = '" + c.getUsuario() + "'"
                             + " WHERE idCategoria = " + c.getId());
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
    
    public boolean borrarCategoria(int x){
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
            st.executeUpdate("DELETE FROM CATEGORIA WHERE idCategoria = " + x);
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
    
    public boolean setGastos(int x){
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
            rs = st.executeQuery("SELECT * FROM TRANSACCION WHERE Categoria.idCategoria = " + x + " AND tipo = 'GASTO'");
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
                gastos = new Transaccion[rs.getRow()];
                rs.beforeFirst();
                for(int i = 0; i < gastos.length && rs.next(); ++i){
                    gastos[i] = new Transaccion(
                        rs.getInt("idTransaccion"),
                        rs.getInt("monto"),
                        rs.getDate("fecha"),
                        rs.getString("tipo"),
                        x,
                        rs.getInt("idCuenta")
                    );
                }
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
    
    public boolean setGastos(int x, Date fecha_ini, Date fecha_fin){
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
            rs = st.executeQuery("SELECT * FROM TRANSACCION WHERE Categoria.idCategoria = " + x+ " AND tipo = 'GASTO'"
                                 + " AND fecha BETWEEN '" + fecha_ini.toString() + "' AND '" + fecha_fin.toString() + "'");
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
                gastos = new Transaccion[rs.getRow()];
                rs.beforeFirst();
                for(int i = 0; i < gastos.length && rs.next(); ++i){
                    gastos[i] = new Transaccion(
                        rs.getInt("idTransaccion"),
                        rs.getInt("monto"),
                        rs.getDate("fecha"),
                        rs.getString("tipo"),
                        x,
                        rs.getInt("idCuenta")
                    );
                }
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
}
