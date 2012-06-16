package view;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
@RequestScoped
public class transaccionesBean{
    private Cuenta cuenta;
    private Categoria categoria;
    private Transaccion transaccionActual;
    private boolean hayTransacciones;
    private HtmlInputText agregarCuenta;
    private HtmlInputText agregarComentario;
    private ArrayList<Transaccion> transacciones;
    
    public transaccionesBean(){
        this.hayTransacciones = false;
    }
    
    public void setCuenta(Cuenta cuenta){
        this.cuenta = cuenta;
    }
    
    public Cuenta getCuenta(){
        return this.cuenta;
    }
    
    public void setCategoria(Categoria categoria){
        this.categoria = categoria;
    }
    
    public Categoria getCategoria(){
        return this.categoria;
    }
               
    public void setHayTransacciones(Cuenta cuenta){
        this.hayTransacciones = true;    
    }
    
    public boolean getHayTransacciones(){
        return this.hayTransacciones;
    }

    public void setTransacciones() {
        this.transacciones = this.cuenta.getTransacciones();
    }

    public ArrayList<Transaccion> getTransacciones() {
        return transacciones;
    }
}
