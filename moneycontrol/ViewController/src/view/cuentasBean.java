package view;

import java.math.BigDecimal;

import java.sql.Date;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

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
    private String nombreCuentaActual;
    private String fecha1;
    private String fecha2;
    private String ingresarFecha;
    private String ingresarTipo;
    private String ingresarCategoria;
    private int ingresarMonto;
    
    private Cuenta[] cuentas;
    private Cuenta cuentaActual;
    private String[] nombres;
    private boolean hayCuentas;
    private HtmlInputText agregarCuenta;
    private HtmlInputText agregarComentario;
    
    public cuentasBean() throws Exception {
        usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        this.fecha1 = "";
        this.fecha2 = "";
        actualizarLista();
        actualizarFechas();
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
        actualizarFechas();
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
    
    public void actualizarFechas(){
        SimpleDateFormat ds;
        Date inicio, fin;
        String[] formatStrings = {"dd/MM/yy","dd-MM-yy","ddMMyy",
                                  "dd/MM/yyyy","dd-MM-yyyy","ddMMyyyy","yyyy-dd-MM"};
        for(Cuenta c : cuentasList){
            for (String formatString : formatStrings){
                try{
                    ds = new SimpleDateFormat(formatString);
                    if(fecha1 == null || fecha1.equals(""))
                        inicio = new Date(0);
                    else
                        inicio = new Date(ds.parse(fecha1).getTime());
                    if(fecha2 == null || fecha2.equals(""))
                        fin = new Date(System.currentTimeMillis());
                    else
                        fin = new Date(ds.parse(fecha2).getTime());
                    
                    c.setTransacciones(inicio, fin);
                    //System.out.println("Se parsea a " + inicio + " " + fin);
                    break;
                }catch(Exception e){
                    System.out.println("No se pudo parsear " + fecha1 + " " + fecha2);
                }
            }
        }
    }
    
    public void agregarTransaccion(){
        SimpleDateFormat ds;
        Date fecha = new Date(System.currentTimeMillis());
        String[] formatStrings = {"dd/MM/yy","dd-MM-yy","ddMMyy",
                                  "dd/MM/yyyy","dd-MM-yyyy","ddMMyyyy","yyyy-dd-MM"};
        for(Cuenta c: cuentasList){
            if(c.getNombre().equals(nombreCuentaActual)){
                for (String formatString : formatStrings){
                    try{
                        ds = new SimpleDateFormat(formatString);
                        if(fecha != null && !fecha.equals(""))
                            fecha = new Date(ds.parse(ingresarFecha).getTime());
                        break;
                    }catch(Exception e){
                        System.out.println("No se pudo parsear " + fecha1 + " " + fecha2);
                    }
                }
                Categoria cat = new Categoria(ingresarCategoria, usuario.getNombre());
                cat.crearCategoria();
                Transaccion tx = new Transaccion(ingresarMonto, fecha, ingresarTipo, cat.getId() , c.getId());
                tx.crearTransaccion();
                break;
            }
        }
    }
    
    public void actualizarLista(){
        if (usuario.setCuentas()) {
            Cuenta[] cuentas = usuario.getCuentas();
            this.cuentasList = new ArrayList<Cuenta>();
            for (int i = 0; i < cuentas.length; ++i) {
                this.cuentasList.add(cuentas[i]);
                cuentasList.get(i).setTransacciones();
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
            this.nombreCuentaActual = cuentaActual.getNombre();
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
            this.nombreCuentaActual = nombreCuentaActual;
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

    public void setNombreCuentaActual(String nombreCuentaActual) {
        this.nombreCuentaActual = nombreCuentaActual;
    }

    public String getNombreCuentaActual() {
        return nombreCuentaActual;
    }

    public void setFecha1(String fecha1) {
        this.fecha1 = fecha1;
    }

    public String getFecha1() {
        return fecha1;
    }

    public void setFecha2(String fecha2) {
        this.fecha2 = fecha2;
    }

    public String getFecha2() {
        return fecha2;
    }

    public void setIngresarFecha(String ingresarFecha) {
        this.ingresarFecha = ingresarFecha;
    }

    public String getIngresarFecha() {
        return ingresarFecha;
    }

    public void setIngresarTipo(String ingresarTypo) {
        this.ingresarTipo = ingresarTypo;
    }

    public String getIngresarTipo() {
        return ingresarTipo;
    }

    public void setIngresarMonto(int ingresarMonto) {
        this.ingresarMonto = ingresarMonto;
    }

    public int getIngresarMonto() {
        return ingresarMonto;
    }

    public void setIngresarCategoria(String ingresarCategoria) {
        this.ingresarCategoria = ingresarCategoria;
    }

    public String getIngresarCategoria() {
        return ingresarCategoria;
    }
}
