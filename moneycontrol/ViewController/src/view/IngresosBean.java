package view;

import java.sql.SQLException;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "IngresosBean")
@ViewScoped
public class IngresosBean{
    
    // Usuario, solo por comodidad.
    private Usuario user;
    
    // Elementos seleccionados
    private Cuenta cuenta;
    private Categoria categoria;
    private Transaccion ingreso;
    
    // Monto a agregar
    private int monto;
    
    // Total de Ingresos
    private int total;
    private int totalGlobal;
    
    // Listados de elementos necesarios
    private ArrayList<Cuenta> cuentas;
    private ArrayList<Categoria> categorias;
    private ArrayList<Transaccion> ingresos;
    
    // Necesarios para SelectOneMenu, no tengo tiempo para aprender a usar converters :(
    private String nombreCuenta;
    private String nombreCategoria;
    private ArrayList<String> nombreCuentas;
    private ArrayList<String> nombreCategorias;
    
    
    // Constructor
    public IngresosBean() throws SQLException, Exception {
        user = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        ingresos = new ArrayList<Transaccion>();
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

    public void setIngreso(Transaccion ingreso){
        this.ingreso = ingreso;
    }
    public Transaccion getIngreso(){
        return ingreso;
    }
    

    // Monto a agregar
    public void setMonto(String monto){
        this.monto = Integer.parseInt(monto);    
    }
    public String getMonto(){
        return Integer.toString(this.monto);
    }

    // Total de Ingresos
    public void setTotal(){
        int total = 0;
        for(int i = 0; i < ingresos.size(); i++)
            total += ingresos.get(i).getMonto();
        this.total = total;
    }
    public int getTotal(){
        return this.total;
    }
    
    public void setTotalGlobal() throws Exception {
        Categoria categoria_backup = categoria;
        Cuenta cuenta_backup = cuenta;
    
        ArrayList<Transaccion> t;
        totalGlobal = 0;
        System.out.println("AL MENOS EXISTO");
        for(Categoria ca : categorias)
            for(Cuenta cu : cuentas){
                System.out.println("!!!! ---> " + totalGlobal);
                categoria = ca;
                cuenta = cu;
                t = getIngresos();
                for(int i = 0; i < ingresos.size(); i++)
                    totalGlobal += t.get(i).getMonto();
            }
        cuenta = cuenta_backup;
        categoria = categoria_backup;
    }    
    public int getTotalGlobal(){
        return totalGlobal;
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
    
    public void setIngresos() throws SQLException {
        categoria.setIngresos(cuenta);
        ingresos = categoria.getIngresos();
    }
    public ArrayList<Transaccion> getIngresos() throws Exception {
        return this.ingresos;
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
    
    public String nuevoIngreso() throws Exception {
        Transaccion t = new Transaccion(monto, new java.sql.Date(new java.util.Date().getTime()),
                                        "INGRESO", categoria.getId(), cuenta.getId());
        t.crearTransaccion();
        refrescar();
        return null;
    }

    public String borrarIngreso() {
        ingresos.remove(ingreso);
        ingreso.borrarTransaccion();
        return null;
    }

    public void refrescar() throws SQLException, Exception {
        setCuenta();
        setCategoria();
        setIngresos();
        setTotal();
        setTotalGlobal();
    }
    
    public void setNewCategoria(ValueChangeEvent e) throws SQLException, Exception {
        setNombreCategoria(e.getNewValue().toString());
        refrescar();
    }
    
    public void setNewCuenta(ValueChangeEvent e) throws SQLException, Exception {
        setNombreCuenta(e.getNewValue().toString());
        refrescar();
    }
    
    public String calcularTotalGlobal() throws Exception {
        setTotalGlobal();
        return Integer.toString(getTotalGlobal());
    }
    
}
