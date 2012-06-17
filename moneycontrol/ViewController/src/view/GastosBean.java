package view;

import java.io.PrintStream;

import java.sql.SQLException;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "GastosBean")
@ViewScoped
public class GastosBean{
    
    // Usuario, solo por comodidad.
    private Usuario user;
    
    // Elementos seleccionados
    private Cuenta cuenta;
    private Categoria categoria;
    private Transaccion gasto;
    
    // Monto a agregar
    private int monto;
    
    // Total de Gastos
    private int total;
    
    // Listados de elementos necesarios
    private ArrayList<Cuenta> cuentas;
    private ArrayList<Categoria> categorias;
    private ArrayList<Transaccion> gastos;
    
    // Necesarios para SelectOneMenu, no tengo tiempo para aprender a usar converters :(
    private String nombreCuenta;
    private String nombreCategoria;
    private ArrayList<String> nombreCuentas;
    private ArrayList<String> nombreCategorias;
    
    
    // Constructor
    public GastosBean() throws SQLException, Exception {
        user = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        gastos = new ArrayList<Transaccion>();
        setCuentas();
        setCategorias();
        setNombreCuentas();
        setNombreCategorias();
        categoria = categorias.get(0);
        nombreCategoria = categoria.getNombre();
        cuenta = cuentas.get(0);
        nombreCuenta = cuenta.getNombre();
        refrescar();
    }
    
    
    // Get & Set (en el mismo orden anterior)
    
    // Elementos seleccionados
    public void setCuenta() throws SQLException, Exception {
        for(Cuenta c : cuentas)
            if(c.getNombre().equals(nombreCuenta)){
                cuenta = c;
                return;
            }
    }
    public Cuenta getCuenta(){
        return this.cuenta;
    }
    
    public void setCategoria() throws SQLException {
        for(Categoria c : categorias)
            if(c.getNombre().equals(nombreCategoria)){
                categoria = c;
                break;
            }
    }
    public Categoria getCategoria(){
        return this.categoria;
    }

    public void setGasto(Transaccion gasto){
        this.gasto = gasto;
    }
    public Transaccion getGasto(){
        return gasto;
    }

    // Monto a agregar
    public void setMonto(String monto){
        this.monto = Integer.parseInt(monto);    
    }
    public String getMonto(){
        return Integer.toString(this.monto);
    }

    // Total de gastos
    public void setTotal(){
        int total = 0;
        for(int i = 0; i < gastos.size(); i++)
            total += gastos.get(i).getMonto();
        this.total = total;
    }
    public int getTotal(){
        return this.total;
    }
    
    // Listados de elementos necesarios
    public void setCuentas(){
        user.setCuentas();
        cuentas = user.getCuentasList();
    } 
    public ArrayList<Cuenta> getCuentas(){
        return cuentas;
    }
    
    public void setCategorias(){
        user.setCategorias();
        categorias = user.getCategoriasList();
    }
    public ArrayList<Categoria> getCategorias(){
        return categorias;
    }
    
    public void setGastos() throws SQLException {
        categoria.setGastos(cuenta);
        gastos = categoria.getGastos();
    }
    public ArrayList<Transaccion> getGastos() throws Exception {
        return this.gastos;
    }
    
    // Necesarios para SelectOneMenu, no tengo tiempo para aprender a usar converters :(
    public void setNombreCuenta(String nombre) throws SQLException {
        this.nombreCuenta = nombre;
    }
    public String getNombreCuenta(){
        return nombreCuenta;
    }
    
    public void setNombreCategoria(String nombre) {
        this.nombreCategoria = nombre;
    }
    public String getNombreCategoria(){
        return this.nombreCategoria;
    }
  
    public void setNombreCuentas(){
        nombreCuentas = new ArrayList<String>();
        for(Cuenta c : cuentas) nombreCuentas.add(c.getNombre());
    }
    public ArrayList<String> getNombreCuentas(){
        return nombreCuentas;
    }

    public void setNombreCategorias(){
        nombreCategorias = new ArrayList<String>();
        for(Categoria c : categorias) nombreCategorias.add(c.getNombre());
    } 
    public ArrayList<String> getNombreCategorias(){
        return nombreCategorias;
    }


    
    // Funciones
    
    public String nuevoGasto() throws Exception {
        Transaccion t = new Transaccion(monto, new java.sql.Date(new java.util.Date().getTime()),
                                        "GASTO", categoria.getId(), cuenta.getId());
        t.crearTransaccion();
        refrescar();
        return null;
    }

    public String borrarGasto() {
        gastos.remove(gasto);
        gasto.borrarTransaccion();
        return null;
    }

    public void refrescar() throws SQLException, Exception {
        setCuenta();
        setCategoria();
        setGastos();
        setTotal();
    }
    
    public void setNewCategoria(ValueChangeEvent e) throws SQLException, Exception {
        setNombreCategoria(e.getNewValue().toString());
        refrescar();
    }
    
    public void setNewCuenta(ValueChangeEvent e) throws SQLException, Exception {
        setNombreCuenta(e.getNewValue().toString());
        refrescar();
    }
    
    
}
