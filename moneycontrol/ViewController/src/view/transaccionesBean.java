package view;

import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Date;

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
    private String nombreCuenta;
    private Categoria categoria;
    private String nombreCategoria;
    private int monto;
    private int total;
    private Categoria[] categorias;
    private Cuenta[] cuentas;
    private ArrayList<String> nombreCategorias;
    private ArrayList<String> nombreCuentas;
    private ArrayList<Transaccion> transacciones;
    
    // Constructor
    public transaccionesBean(){
        Usuario user = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        
        user.setCategorias(user.getNombre());
        categorias = user.getCategorias();
        categoria = categorias[0];
        nombreCategorias = new ArrayList<String>();
        for(Categoria c : categorias) nombreCategorias.add(c.getNombre());
        
        user.setCuentas(user.getNombre());
        cuentas = user.getCuentas();
        cuenta = cuentas[0];
        nombreCuentas = new ArrayList<String>();
        for(Cuenta c : cuentas) nombreCuentas.add(c.getNombre());
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
        System.out.println("hola");
        return Integer.toString(this.monto);
        //return this.monto;
    }
    
    // total
    public void setTotal(){
        int total = 0;
        for(int i = 0; i < transacciones.size(); i++)
            total += Integer.parseInt(transacciones.get(i).getMonto());
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
    public void setTransacciones(ArrayList<Transaccion> transacciones){
        this.transacciones = transacciones;
    }
    public ArrayList<Transaccion> getTransacciones(){
        return this.transacciones;
    }
    
    public String nuevoGasto() throws Exception {
        if(categoria==null || cuenta == null){
            if(nombreCategoria == null)
                throw new Exception("Problema es con la pagina");
            throw new Exception("El problema es con los benas");
        }
        Transaccion t = new Transaccion(monto, new Date(), "GASTO", categoria.getId(), cuenta.getId());
        if(!t.crearTransaccion(t))
            throw new Exception("No se creó la conexion");
        actualizarLista();
        return "success";
    }

    public void actualizarLista(){
        categoria.setGastos(cuenta);
        transacciones = categoria.getGastos();
        setTotal();
    }
}