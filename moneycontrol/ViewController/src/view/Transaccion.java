package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;

public class Transaccion {
    private int id;
    private int monto;
    private Date fecha;
    private String tipo;
    private int idCategoria;
    private String categoria;
    private int idCuenta;
    private String cuenta;
    private Date fecha_limite;
    public Transaccion() {
        super();
    }

    public Transaccion(int id, int monto, Date fecha, String tipo, int idCategoria,
                       int idCuenta) {
        super();
        this.id = id;
        this.monto = monto;
        this.fecha = fecha;
        this.tipo = tipo;
        this.idCategoria = idCategoria;
        this.idCuenta = idCuenta;
    }

    public Transaccion(int monto, Date fecha, String tipo, int idCategoria, int idCuenta) {
        super();
        this.id = -1;
        this.monto = monto;
        this.fecha = fecha;
        this.tipo = tipo;
        this.idCategoria = idCategoria;
        this.idCuenta = idCuenta;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public String getMonto() {
        return Integer.toString(monto);
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha.toString();
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setFecha_limite(Date fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public Date getFecha_limite() {
        return fecha_limite;
    }
    
    public boolean getTransaccion(int x){
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        Statement st;
        ResultSet rs, rs2;   
        boolean flag = false;
        
        try {
            st = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        try {
            rs = st.executeQuery("SELECT * FROM" +
                " (TRANSACCION AS T JOIN CATEGORIA AS CAT ON CAT.idCategoria = T.Categoria_idCategoria)" +
                " JOIN CUENTA AS C ON C.idCuenta = T.Cuenta_idCuenta" +
                " WHERE T.idTransaccion = " + x);
            rs2 = st.executeQuery("SELECT * FROM PRESTAMO WHERE Transaccion_idTransaccion = " + x);
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
                setId(rs.getInt("idTransaccion"));
                setMonto(rs.getInt("monto"));
                setFecha(rs.getDate("fecha"));
                setTipo(rs.getString("tipo"));
                setIdCategoria(rs.getInt("idCategoria"));
                setCategoria(rs.getString("CAT.nombre"));
                setIdCuenta(rs.getInt("idCuenta"));
                setCuenta(rs.getString("C.nombre"));
                if(rs2.next()) setFecha_limite(rs2.getDate("fecha_limite"));
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                rs.close();
                rs2.close();
                st.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flag;
    }
    
    public boolean crearTransaccion(Transaccion t){
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean flag = false;
        int id;
        
        try {
            st = con.prepareStatement("INSERT INTO TRANSACCION(monto, fecha, tipo, Categoria_idCategoria, Cuenta_idCuenta) VALUES("
                             + " " + t.getMonto() + ","
                             + " '" + t.getFecha() + "',"
                             + " '" + t.getTipo() + "',"
                             + " " + t.getIdCategoria() + ","
                             + " " + t.getIdCuenta() + ")", Statement.RETURN_GENERATED_KEYS);
            st.executeUpdate();
            rs = st.getGeneratedKeys();
            id = rs.getInt(1);
            rs.close();
            
            if(t.getTipo() == "PRESTAMO" && t.getFecha_limite() != null && rs.next()){
                st = con.prepareStatement("INSERT INTO PRESTAMO(fecha_limite, Transaccion_idTransaccion) VALUES( "
                                 + " '" + t.getFecha_limite() + "',"
                                 + " " + id + ")");
                st.executeUpdate();
            }
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try{
                if(rs != null) rs.close();
                if(st != null) st.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flag;
    }
    
    public boolean crearTransaccionInterna(Transaccion t1, Transaccion t2){
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        PreparedStatement st = null;
        ResultSet rs;
        rs = null;
        boolean flag = false;
        int id1, id2;
        
        try {
            st = con.prepareStatement("INSERT INTO TRANSACCION(monto, fecha, tipo, Categoria_idCategoria, Cuenta_idCuenta) VALUES("
                             + " " + t1.getMonto() + ","
                             + " '" + t1.getFecha() + "',"
                             + " '" + t1.getTipo() + "',"
                             + " " + t1.getIdCategoria() + ","
                             + " " + t1.getIdCuenta() + ")", Statement.RETURN_GENERATED_KEYS);
            st.executeUpdate();
            rs = st.getGeneratedKeys();
            id1 = rs.getInt(1);
            rs.close();
            
            if(t1.getTipo() == "PRESTAMO" && t1.getFecha_limite() != null && rs.next()){
                st = con.prepareStatement("INSERT INTO PRESTAMO(fecha_limite, Transaccion_idTransaccion) VALUES( "
                                 + " '" + t1.getFecha_limite() + "',"
                                 + " " + id1 + ")");
                st.executeUpdate();
            }
            
            st = con.prepareStatement("INSERT INTO TRANSACCION(monto, fecha, tipo, Categoria_idCategoria, Cuenta_idCuenta) VALUES("
                             + " " + t2.getMonto() + ","
                             + " '" + t2.getFecha() + "',"
                             + " '" + t2.getTipo() + "',"
                             + " " + t2.getIdCategoria() + ","
                             + " " + t2.getIdCuenta() + ")", Statement.RETURN_GENERATED_KEYS);
            st.executeUpdate();
            rs = st.getGeneratedKeys();
            id2 = rs.getInt(1);
            rs.close();
            
            if(t2.getTipo() == "PRESTAMO" && t2.getFecha_limite() != null && rs.next()){
                st = con.prepareStatement("INSERT INTO PRESTAMO(fecha_limite, Transaccion_idTransaccion) VALUES( "
                                 + " '" + t2.getFecha_limite() + "',"
                                 + " " + id2 + ")");
                st.executeUpdate();
            }
            
            st = con.prepareStatement("INSERT INTO TRANSACCION_INTERNA(Transaccion_idTransaccion, Cuenta_idCuenta) VALUES("
                             + " " + id1 + ","
                             + " " + t2.getCuenta() + ")");
            st.executeUpdate();
            st = con.prepareStatement("INSERT INTO TRANSACCION_INTERNA(Transaccion_idTransaccion, Cuenta_idCuenta) VALUES("
                             + " " + id2 + ","
                             + " " + t1.getCuenta() + ")");
            st.executeUpdate();
            
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try{
                rs.close();
                if(st != null) st.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flag;
    }
}
