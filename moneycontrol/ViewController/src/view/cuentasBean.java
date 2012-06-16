package view;

import java.math.BigDecimal;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class cuentasBean{
    private Usuario usuario;
    private ArrayList<Cuenta> cuentasList;
    private Cuenta cuentaSeleccionada;
    
    private Cuenta[] cuentas;
    private Cuenta cuentaActual;
    private String[] nombres;
    private boolean hayCuentas;
    private HtmlInputText agregarCuenta;
    private HtmlInputText agregarComentario;
    
    public cuentasBean() throws Exception {
        usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        actualizarLista();
    }
    
    public String guardarCuenta(){
        //get all existing value but set "editable" to false 
        for (Cuenta c : cuentasList){
            if(c.isEditable()){
                c.editarCuenta();
                c.setEditable(false);
                System.out.println("se guardo " + c.getNombre() + "ID: " + c.getId());
            }
        }
        return null;
    }

    public String nuevaCuenta() {
        Cuenta nueva = new Cuenta("Cuenta " + (cuentasList.size() + 1), "", usuario.getNombre());
        nueva.crearCuenta();
        cuentasList.add(nueva);
        actualizarLista();
        return null;
    }
    
    public String borrarCuenta() {
        cuentasList.remove(cuentaSeleccionada);
        cuentaSeleccionada.borrarCuenta();
        return null;
    }

    public String editarCuenta() {
        cuentaSeleccionada.setEditable(true);
        return null;
    }
    
   

    public void setCuentasList(ArrayList<Cuenta> cuentasList) {
        this.cuentasList = cuentasList;
    }

    public ArrayList<Cuenta> getCuentasList() {
        return cuentasList;
    }
    
    public void actualizarLista(){
        if (usuario.setCuentas()) {
            Cuenta[] cuentas = usuario.getCuentas();
            this.cuentasList = new ArrayList<Cuenta>();
            for (int i = 0; i < cuentas.length; ++i) {
                this.cuentasList.add(cuentas[i]);
            }
        }
        this.cuentas = usuario.getCuentas();
        if(setHayCuentas()){
            try{
                setNombres();
            }catch (Exception e) {
                return;
            }
            cuentaActual = cuentas[0];
        }
        
    }
    
    
    
    
    
    
    public void setCuentas(Cuenta[] cuentas) {
            this.cuentas = cuentas;
        }

        public Cuenta[] getCuentas() {
            return cuentas;
        }

        public void setAgregarCuenta(HtmlInputText agregarCuenta) {
            this.agregarCuenta = agregarCuenta;
        }

        public HtmlInputText getAgregarCuenta() {
            return agregarCuenta;
        }

        public void setAgregarComentario(HtmlInputText agregarComentario) {
            this.agregarComentario = agregarComentario;
        }

        public HtmlInputText getAgregarComentario() {
            return agregarComentario;
        }
        
        public boolean setHayCuentas(){
            if(cuentas != null && cuentas.length > 0)
                this.hayCuentas = true;
            else
                this.hayCuentas = false;
            return this.hayCuentas;
        }
            
        public boolean getHayCuentas(){
            return hayCuentas;
        }
        
        public void setCuentaActual(String nombreCuentaActual) throws Exception {
            for(int i = 0; i < cuentas.length ; i++){
                if(cuentas[i].getNombre() == nombreCuentaActual)
                    cuentaActual = cuentas[i];
                    return;
                }
            throw new Exception("Se pide fijar como cuenta actual una cuenta inexistente");
        }
        
        public Cuenta getCuentaActual(){
            return this.cuentaActual;
        }
        
        public void setNombres() throws Exception {
            try{
                nombres = new String[cuentas.length];
                for(int i = 0; i < cuentas.length; i++ )
                    nombres[i] = cuentas[i].getNombre();
            }
            catch(Exception e){
                throw new Exception("Se intenta obtener nombres de cuentas cuando no hay cuentas");
            }
        }

        public String[] getNombres(){
            return nombres;
        }

    public void setCuentaSeleccionada(Cuenta cuentaSeleccionada) {
        this.cuentaSeleccionada = cuentaSeleccionada;
    }

    public Cuenta getCuentaSeleccionada() {
        return cuentaSeleccionada;
    }
}
