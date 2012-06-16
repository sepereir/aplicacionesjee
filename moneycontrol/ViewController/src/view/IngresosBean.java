package view;

import java.io.PrintStream;

import java.sql.SQLException;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "IngresosBean")
@RequestScoped
public class IngresosBean{
    private Cuenta cuenta;
    private String nombreCuenta;
    private Categoria categoria;
    private String nombreCategoria;
    private int monto;
    private int total;
    private Categoria[] categorias;
    private Cuenta[] cuentas;
    private ArrayList<String> nombreCategorias;
    private ArrayList<String> nombreCuentas;
    private ArrayList<Transaccion> ingresos;
    
    // Constructor
    public IngresosBean(){
        Usuario user = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        ingresos = new ArrayList<Transaccion>();
        
        user.setCategorias();
        categorias = user.getCategorias();
        nombreCategorias = new ArrayList<String>();
        if(categorias != null && categorias.length >= 0){
            categoria = categorias[0];
            for(Categoria c : categorias)
                nombreCategorias.add(c.getNombre());
        }
        
        user.setCuentas();
        cuentas = user.getCuentas();
        nombreCuentas = new ArrayList<String>();
        if(cuentas != null && cuentas.length >= 0){
            cuenta = cuentas[0];
            for(Cuenta c : cuentas)
                nombreCuentas.add(c.getNombre());
        }
    }
    
    // cuenta
    public void setCuenta(){
        for(Cuenta c : cuentas)
            if(c.getNombre() == this.nombreCuenta){
                this.cuenta = c;
                return;
            }
    } 
    public Cuenta getCuenta(){
        return this.cuenta;
    }
    
    // nombreCuenta
    public void setNombreCuenta(String nombre){
        System.out.println("Se est seteado cuenta");
        this.nombreCuenta = nombre;
        setCuenta();
    }
    public String getNombreCuenta(){
        return nombreCuenta;
    }

    // categoria
    public void setCategoria(){
        for(Categoria c : categorias)
            if(c.getNombre() == this.nombreCategoria){
                this.categoria = c;
                return;
            }
    }
    public Categoria getCategoria(){
        return this.categoria;
    }
    
    // nombreCategoria
    public void setNombreCategoria(String nombre){
        this.nombreCategoria = nombre;
        setCategoria();
    }
    public String getNombreCategoria(){
        return this.nombreCategoria;
    }
    
    // monto
    public void setMonto(String monto){
        this.monto = Integer.parseInt(monto);    
    }
    public String getMonto(){
        return Integer.toString(this.monto);
    }
    
    // total
    public void setTotal(){
        int total = 0;
        for(int i = 0; i < ingresos.size(); i++)
            total += ingresos.get(i).getMonto();
        this.total = total;
    }
    public int getTotal(){
        return this.total;
    }
    
    // nombreCategorias
    public void setNombreCategorias(ArrayList<String> nombres){
        this.nombreCategorias = nombres;
    }
    public ArrayList<String> getNombreCategorias(){
        return nombreCategorias;
    }
    
    // nombreCuentas
    public void setNombreCuentas(ArrayList<String> nombres){
        this.nombreCuentas = nombres;
    }
    public ArrayList<String> getNombreCuentas(){
        return nombreCuentas;
    }
    
    // transacciones
    public void setIngresos() throws SQLException {
        categoria.setIngresos(cuenta);
        ingresos = categoria.getIngresos();
        setTotal();
    }
    public ArrayList<Transaccion> getIngresos() throws Exception {
        return this.ingresos;
    }
    
    public String nuevoIngreso() throws Exception {
        if(categoria==null || cuenta == null){
            if(nombreCategoria == null)
                throw new Exception("Problema es con la pagina");
            throw new Exception("El problema es con los benas");
        }
        Transaccion t = new Transaccion(monto,
                                        new java.sql.Date(new java.util.Date().getTime()),
                                        "INGRESO",
                                        categoria.getId(),
                                        cuenta.getId()
                                        );
        if(!t.crearTransaccion())
            throw new Exception("No se creó la conexion");
        actualizarIngresos();
        return "success";
    }
    
    public String actualizarIngresos() throws SQLException {
        setIngresos();
        return null;
    }
}