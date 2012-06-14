package view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;

public class Cuenta {
    private int id;
    private String nombre;
    private String comentario;
    private int saldo;
    private String usuario;
    private int saldo_acumulado;
    private boolean editable;
    public Cuenta() {
        super();
    }

    public Cuenta(int id, String nombre, String comentario, int saldo, String usuario) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.comentario = comentario;
        this.saldo = saldo;
        this.usuario = usuario;
        this.editable = false;
    }

    public Cuenta(String nombre, String comentario, String usuario) {
        super();
        this.id = -1;
        this.nombre = nombre;
        this.comentario = comentario;
        this.saldo = 0;
        this.usuario = usuario;
        this.editable = false;
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

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }
    
    public boolean getCuenta(int x){
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
            rs = st.executeQuery("SELECT * FROM CUENTA WHERE idCuenta = " + x);
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
                setId(rs.getInt("idCuenta"));
                setNombre(rs.getString("nombre"));
                String aux = rs.getString("comentario");
                setComentario(aux == null ? "" : aux);
                setSaldo(rs.getInt("saldo"));
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
    
    public boolean crearCuenta(Cuenta c){
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
            st.executeUpdate("INSERT INTO CUENTA(nombre, comentario, saldo, Usuario_nombre) VALUES("
                             + " '" + c.getNombre() + "',"
                             + " '" + c.getComentario() + "',"
                             + " 0,"
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
    
    public boolean editarCuenta(Cuenta c){
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
            st.executeUpdate("UPDATE CUENTA SET"
                             + " nombre = '" + c.getNombre() + "',"
                             + " comentario = '" + c.getComentario() + "',"
                             + " saldo = " + c.getSaldo() + ","
                             + " Usuario_nombre = '" + c.getUsuario() + "'"
                             + " WHERE idCuenta = " + c.getId());
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
    
    public boolean borrarCuenta(int x){
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
            st.executeUpdate("DELETE FROM CUENTA WHERE idCuenta = " + x);
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
    
    public boolean setSaldo_acumulado(int x, Date fecha){
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
            rs = st.executeQuery("SELECT SUM(monto)"
                + " FROM CUENTA AS C JOIN TRANSACCION AS T ON C.idCuenta = T.Cuenta_idCuenta"
                + " WHERE C.idCuenta = " + x
                + " AND T.fecha < '" + fecha.toString() + "'");
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
                this.saldo_acumulado = rs.getInt(1);
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

    public int getSaldo_acumulado() {
        return saldo_acumulado;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }
}
