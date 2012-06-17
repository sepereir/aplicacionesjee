package view;

import java.sql.SQLException;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "PrestamosBean")
@ViewScoped
public class PrestamosBean{
    
    // Usuario, solo por comodidad.
    private Usuario user;
    
    // Elementos seleccionados
    private Cuenta cuenta;
    private Categoria categoria;
    private Transaccion prestamo;
    
    // Monto a agregar
    private int monto;
    
    // Total de Prestamos
    private int total;
    private int totalGlobal;
    
    // Listados de elementos necesarios
    private ArrayList<Cuenta> cuentas;
    private ArrayList<Categoria> categorias;
    private ArrayList<Transaccion> prestamos;
    
    // Necesarios para SelectOneMenu, no tengo tiempo para aprender a usar converters :(
    private String nombreCuenta;
    private String nombreCategoria;
    private ArrayList<String> nombreCuentas;
    private ArrayList<String> nombreCategorias;
    
    
    // Constructor
    public PrestamosBean() throws SQLException, Exception {
        user = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        prestamos = new ArrayList<Transaccion>();
        setCuentas();
        setCategorias();
        setNombreCuentas();
        setNombreCategorias();
        categoria = categorias.get(0);
        nombreCategoria = categoria.getNombre();
        cuenta = cuentas.get(0);
        nombreCuenta = cuenta.getNombre();
        setTotalGlobal();
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

    public void setPrestamo(Transaccion prestamo){
        this.prestamo = prestamo;
    }
    public Transaccion getPrestamo(){
        return prestamo;
    }

    // Monto a agregar
    public void setMonto(String monto){
        this.monto = Integer.parseInt(monto);    
    }
    public String getMonto(){
        return Integer.toString(this.monto);
    }

    // Total de prestamos
    public void setTotal(){
        int total = 0;
        for(int i = 0; i < prestamos.size(); i++)
            total += prestamos.get(i).getMonto();
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
        for(Categoria ca : categorias)
            for(Cuenta cu : cuentas){
                categoria = ca;
                cuenta = cu;
                t = getPrestamos();
                for(int i = 0; i < prestamos.size(); i++)
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
    
    public void setPrestamos() throws SQLException {
        categoria.setPrestamos(cuenta);
        prestamos = categoria.getPrestamos();
    }
    public ArrayList<Transaccion> getPrestamos() throws Exception {
        return this.prestamos;
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
    
    public String nuevoPrestamo() throws Exception {
        Transaccion t = new Transaccion(monto, new java.sql.Date(new java.util.Date().getTime()),
                                        "PRESTAMO", categoria.getId(), cuenta.getId());
        t.crearTransaccion();
        refrescar();
        return null;
    }

    public String borrarPrestamo() {
        prestamos.remove(prestamo);
        prestamo.borrarTransaccion();
        return null;
    }

    public void refrescar() throws SQLException, Exception {
        setCuenta();
        setCategoria();
        setPrestamos();
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
    
    public String calcularTotalGlobal() throws Exception {
        setTotalGlobal();
        return Integer.toString(getTotalGlobal());
    }
    
}