package view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Date;

public class Categoria {
    
    //Variables de instancia
    private int id;
    private String nombre;
    private String usuario;
    private ArrayList<Transaccion> gastos;
    private ArrayList<Transaccion> ingresos;
    private boolean editable;
    
    //Constructores
    public Categoria() {
        super();
    }

    public Categoria(int id, String nombre, String usuario) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.editable = false;
    }

    public Categoria(String nombre, String usuario) {
        super();
        this.id = -1;
        this.nombre = nombre;
        this.usuario = usuario;
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

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public ArrayList<Transaccion> getGastos() {
        return gastos;
    }
    
    public ArrayList<Transaccion> getIngresos(){
        return ingresos;
    }
    //Fin de Setters & Getters
    
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }
    
    //Obtener objeto de la BD
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
    
    //Insertar objeto en la BD
    public boolean crearCategoria(){
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
                             + " '" + getNombre() + "',"
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
    public boolean editarCategoria(){
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
                             + " nombre = '" + getNombre() + "',"
                             + " Usuario_nombre = '" + getUsuario() + "'"
                             + " WHERE idCategoria = " + getId());
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
    public boolean borrarCategoria(){
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
            st.executeUpdate("DELETE FROM CATEGORIA WHERE idCategoria = " + getId());
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
    
    //Obtener gastos de esta cuenta
    public boolean setGastos(Cuenta c){
        gastos = new ArrayList<Transaccion>();
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
            rs = st.executeQuery("SELECT * FROM TRANSACCION WHERE Categoria_idCategoria = " + getId()
                                    + " AND Cuenta_idCuenta = " + c.getId()
                                    + " AND tipo = 'GASTO'");
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

            while (rs.next())
                gastos.add(new Transaccion(rs.getInt("idTransaccion"),
                                           rs.getInt("monto"),
                                           rs.getDate("fecha"),
                                           rs.getString("tipo"),
                                           this.id, getId()));

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
    
    //Obtener gastos de una cuenta entre 2 fechas
    public boolean setGastos(Cuenta c, Date fecha_ini, Date fecha_fin){
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
            rs = st.executeQuery("SELECT * FROM TRANSACCION WHERE Categoria.idCategoria = " + getId()
                                 + " AND Cuenta.idCuenta = " + c.getId()
                                 + " AND tipo = 'GASTO'"
                                 + " AND fecha BETWEEN '" + fecha_ini.toString()
                                 + "' AND '" + fecha_fin.toString() + "'");
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
                gastos = new ArrayList<Transaccion>();
                rs.beforeFirst();
                
                while(rs.next())
                    gastos.add(new Transaccion(
                        rs.getInt("idTransaccion"),
                        rs.getInt("monto"),
                        rs.getDate("fecha"),
                        rs.getString("tipo"),
                        this.id,
                        getId()
                    ));
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
    
    //Obtener ingresos de esta cuenta
    public boolean setIngresos(Cuenta c) {
        ingresos = new ArrayList<Transaccion>();
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
            rs = st.executeQuery("SELECT * FROM TRANSACCION WHERE Categoria_idCategoria = " + getId()
                                    + " AND Cuenta_idCuenta = " + c.getId()
                                    + " AND tipo = 'INGRESO'");
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

            while (rs.next())
                ingresos.add(new Transaccion(rs.getInt("idTransaccion"),
                                           rs.getInt("monto"),
                                           rs.getDate("fecha"),
                                           rs.getString("tipo"),
                                           this.id, getId()));

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
