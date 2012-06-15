package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;

public class Transaccion {
    
    //Variables de instancia
    private int id;
    private int monto;
    private Date fecha;
    private String tipo;
    private int idCategoria;
    private String categoria;
    private int idCuenta;
    private String cuenta;
    private Date fecha_limite;
    
    //Constructores
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
    
    //Setters & Getters
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
    //Fin de Setters & Getters
    
    //Obtener objeto de la BD
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
    
    //Insertar objeto en la BD
    public boolean crearTransaccion(){
        Connection con = Conexion.getSessionConn();
        if(con == null) return false;
        PreparedStatement st = null;
        ResultSet rs;
        rs = null;
        boolean flag = false;
        int id;
        
        try {
            st = con.prepareStatement("INSERT INTO TRANSACCION(monto, fecha, tipo, Categoria_idCategoria, Cuenta_idCuenta) VALUES("
                             + " " + getMonto() + ","
                             + " '" + getFecha() + "',"
                             + " '" + getTipo() + "',"
                             + " " + getIdCategoria() + ","
                             + " " + getIdCuenta() + ")", Statement.RETURN_GENERATED_KEYS);
            st.executeUpdate();
            rs = st.getGeneratedKeys();
            id = rs.getInt(1);
            rs.close();
            
            if(getTipo() == "PRESTAMO" && getFecha_limite() != null && rs.next()){
                st = con.prepareStatement("INSERT INTO PRESTAMO(fecha_limite, Transaccion_idTransaccion) VALUES( "
                                 + " '" + getFecha_limite() + "',"
                                 + " " + id + ")");
                st.executeUpdate();
            }
            
            rs = st.executeQuery("SELECT tipo, sum(monto) AS suma FROM TRANSACCION"
                                 + " WHERE idCuenta = " + getIdCuenta()
                                 + " GROUP BY tipo"
                                 + " ORDER BY tipo");
            if(rs.next()){
                int gasto = rs.getInt("suma");
                rs.next();
                int ingreso = rs.getInt("suma");
                rs.next();
                int prestamo = rs.getInt("suma");
                st.executeUpdate("UPDATE CUENTA SET saldo = " + (ingreso - gasto + prestamo) + " WHERE idCuenta = " + getIdCuenta());
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
    
    //Insertar 2 transacciones entre cuentas de la BD
    static public boolean crearTransaccionInterna(Transaccion t1, Transaccion t2){
        if((t1.getTipo() == "INGRESO" && t2.getTipo() != "GASTO")
            || (t1.getTipo() == "GASTO" && t2.getTipo() != "INGRESO"))
            return false;
        
        
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
            
            rs = st.executeQuery("SELECT tipo, sum(monto) AS suma FROM TRANSACCION WHERE idCuenta = " + t1.getIdCuenta() +
                 " GROUP BY tipo ORDER BY tipo");
            if (rs.next()) {
                int gasto = rs.getInt("suma");
                rs.next();
                int ingreso = rs.getInt("suma");
                rs.next();
                int prestamo = rs.getInt("suma");
                st.executeUpdate("UPDATE CUENTA SET saldo = " + (ingreso - gasto + prestamo) + " WHERE idCuenta = " +
                                 t1.getIdCuenta());
            }
            rs.close();


            rs = st.executeQuery("SELECT tipo, sum(monto) AS suma FROM TRANSACCION WHERE idCuenta = " + t2.getIdCuenta() +
                 " GROUP BY tipo ORDER BY tipo");
            if (rs.next()) {
                int gasto = rs.getInt("suma");
                rs.next();
                int ingreso = rs.getInt("suma");
                rs.next();
                int prestamo = rs.getInt("suma");
                st.executeUpdate("UPDATE CUENTA SET saldo = " + (ingreso - gasto + prestamo) + " WHERE idCuenta = " +
                                 t2.getIdCuenta());
            }
            
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try{
                if(rs != null)rs.close();
                if(st != null) st.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flag;
    }
}
