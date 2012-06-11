package view;

import java.sql.Connection;

import java.sql.SQLException;

import java.util.Map;

import javax.faces.context.FacesContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

public class Conexion {
    static public Connection getSessionConn(){
        return (Connection) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("conexion");
    }
    
    static public boolean crear(){     
        Connection con = getSessionConn();
        if(con != null) return true;
        
        try {
            Context ctx = new InitialContext();
            con = ((DataSource)ctx.lookup("jdbc/cc5604g11")).getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("conexion", con);
        return true;
    }
    
    static public void cerrar(){
        Connection con = getSessionConn();
        if(con != null)
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
