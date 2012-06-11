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
    private int saldo_acumulado;
    private int idCategoria;
    private String categoria;
    private int idCuenta;
    private String cuenta;
    private Date fecha_limite;
    public Transaccion() {
        super();
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

    public int getMonto() {
        return monto;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setSaldo_acumulado(int saldo_acumulado) {
        this.saldo_acumulado = saldo_acumulado;
    }

    public int getSaldo_acumulado() {
        return saldo_acumulado;
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
                setSaldo_acumulado(rs.getInt("saldo_acumulado"));
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
        PreparedStatement st;
        ResultSet rs;
        rs = null;
        boolean flag = false;
        int id;
        
        try {
            st = con.prepareStatement("INSERT INTO TRANSACCION(monto, fecha, tipo, saldo_acumulado, Categoria_idCategoria, Cuenta_idCuenta) VALUES("
                             + " " + t.getMonto() + ","
                             + " '" + t.getFecha() + "',"
                             + " '" + t.getTipo() + "',"
                             + " " + t.getSaldo_acumulado() + ","
                             + " " + t.getIdCategoria() + ","
                             + " " + t.getIdCuenta() + ")", Statement.RETURN_GENERATED_KEYS);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try {
            rs = st.getGeneratedKeys();
            if(t.getTipo() == "Prestamo" && t.getFecha_limite() != null && rs.next()){
                st = con.prepareStatement("INSERT INTO PRESTAMO(fecha_limite, Transaccion_idTransaccion) VALUES( "
                                 + " '" + t.getFecha_limite() + "',"
                                 + " " + rs.getInt(1));
                st.executeUpdate();
            }
            flag = true;
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
