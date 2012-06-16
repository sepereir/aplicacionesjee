package view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.sql.Date;

public class Cuenta {
    
    //Variables de instancia
    private int id;
    private String nombre;
    private String comentario;
    private int saldo;
    private String usuario;
    private int saldo_acumulado;
    private boolean editable;
    private ArrayList<Transaccion> transacciones;
    
    //Constructores
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
        this.transacciones = new ArrayList<Transaccion>();
    }

    public Cuenta(String nombre, String comentario, String usuario) {
        super();
        this.id = -1;
        this.nombre = nombre;
        this.comentario = comentario;
        this.saldo = 0;
        this.usuario = usuario;
        this.editable = false;
        this.transacciones = new ArrayList<Transaccion>();
    }
    
    //Setters & Getters
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
    
    public ArrayList<Transaccion> getTransacciones() {
        return transacciones;
    }
    //Fin de Setters & Getters
    
    //Obtener objeto de la BD
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
    
    //Insertar objeto en la BD
    public boolean crearCuenta(){
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
                             + " '" + getNombre() + "',"
                             + " '" + getComentario() + "',"
                             + " 0,"
                             + " '" + getUsuario() + "')");
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
    public boolean editarCuenta(){
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
                             + " nombre = '" + getNombre() + "',"
                             + " comentario = '" + getComentario() + "',"
                             + " saldo = " + getSaldo() + ","
                             + " Usuario_nombre = '" + getUsuario() + "'"
                             + " WHERE idCuenta = " + getId());
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
    public boolean borrarCuenta(){
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
            st.executeUpdate("DELETE FROM CUENTA WHERE idCuenta = " + getId());
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
    
    //Obtener el saldo acumulado en una determinada fecha
    public boolean setSaldo_acumulado(Date fecha){
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
            rs = st.executeQuery("SELECT T.tipo, SUM(T.monto) AS suma"
                + " FROM CUENTA AS C JOIN TRANSACCION AS T ON C.idCuenta = T.Cuenta_idCuenta"
                + " WHERE C.idCuenta = " + getId()
                + " AND T.fecha < '" + fecha.toString() + "'"
                + " GROUP BY T.tipo"
                + " ORDER BY T.tipo");
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
                int gasto = rs.getInt("suma");
                rs.next();
                int ingreso = rs.getInt("suma");
                rs.next();
                int prestamo = rs.getInt("suma");
                this.saldo_acumulado = ingreso - gasto + prestamo;
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
    
    public boolean setTransacciones(){
        return setTransacciones(new Date(0), new Date(System.currentTimeMillis()));
    }
    public boolean setTransacciones(Date fechaInicio, Date fechaFin){
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
            rs = st.executeQuery("SELECT *"
                + " FROM CUENTA JOIN TRANSACCION ON CUENTA.idCuenta = TRANSACCION.Cuenta_idCuenta"
                + " WHERE CUENTA.idCuenta = " + getId()
                + " AND TRANSACCION.fecha < to_date('" + fechaFin.toString() + "','yyyy/mm/dd')"
                + " AND TRANSACCION.fecha > to_date('" + fechaInicio.toString() + "','yyyy/mm/dd')"
                + " ORDER BY TRANSACCION.tipo");
            
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
            transacciones = new ArrayList<Transaccion>();
            while(rs.next()){
                transacciones.add(new Transaccion(rs.getInt("monto"), 
                                        rs.getDate("fecha"),
                                        rs.getString("tipo"),
                                        rs.getInt("Categoria_idCategoria"),
                                        rs.getInt("idCuenta")));
                
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
