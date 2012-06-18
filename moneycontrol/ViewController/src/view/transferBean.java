package view;

import java.sql.Date;

import java.util.ArrayList;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class transferBean{
    private Usuario usuario;
    private String nombreCuentaDesde, nombreCuentaHacia, nombreCategoria;
    private ArrayList<Cuenta> cuentasList;
    private ArrayList<Categoria> categoriasList;
    private ArrayList<String> nombreCuentasList, nombreCategoriasList;
    private int monto;
    private HtmlOutputLabel estado;

    public transferBean(){
        usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        usuario.setCuentas();
        usuario.setCategorias();
        cuentasList = new ArrayList<Cuenta>();
        categoriasList = new ArrayList<Categoria>();
        nombreCuentasList = new ArrayList<String>();
        nombreCategoriasList = new ArrayList<String>();
        Cuenta[] auxCu = usuario.getCuentas();
        Categoria[] auxCat = usuario.getCategorias();
        
        if (auxCu != null) for(Cuenta c: usuario.getCuentas()){
            cuentasList.add(c);
            nombreCuentasList.add(c.getNombre());
        }
        if (auxCat != null) for(Categoria c: usuario.getCategorias()){
            categoriasList.add(c);
            nombreCategoriasList.add(c.getNombre());
        }
    }
    
    public void transferir(){
        Cuenta c1 = new Cuenta();
        Cuenta c2 = new Cuenta();
        Categoria cat = new Categoria();
        for(Cuenta c: cuentasList){
            if(c.getNombre().equals(nombreCuentaDesde))
                c1 = c;
            if(c.getNombre().equals(nombreCuentaHacia))
                c2 = c;
        }
        for(Categoria c: categoriasList){
            if(c.getNombre().equals(nombreCategoria)){
                cat = c;
                break;
            }
        }
        Transaccion t1 = new Transaccion(monto, new Date(System.currentTimeMillis()), "GASTO", cat.getId(), c2.getId());
        Transaccion t2 = new Transaccion(monto, new Date(System.currentTimeMillis()), "INGRESO", cat.getId(), c1.getId());
        if(Transaccion.crearTransaccionInterna(t2,t1)){
            cuentasBean bean = (cuentasBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cuentasBean");
            bean.actualizarLista();
            this.estado.setValue("Transferencia exitosa");
        }else{
            this.estado.setValue("Fallo en la transferencia");
        }
    }

    public void setNombreCuentaDesde(String nombreCuentaDesde) {
        this.nombreCuentaDesde = nombreCuentaDesde;
    }

    public String getNombreCuentaDesde() {
        return nombreCuentaDesde;
    }

    public void setNombreCuentaHacia(String nombreCuentaHacia) {
        this.nombreCuentaHacia = nombreCuentaHacia;
    }

    public String getNombreCuentaHacia() {
        return nombreCuentaHacia;
    }

    public void setNombreCuentasList(ArrayList<String> nombreCuentasList) {
        this.nombreCuentasList = nombreCuentasList;
    }

    public ArrayList<String> getNombreCuentasList() {
        return nombreCuentasList;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public int getMonto() {
        return monto;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoriasList(ArrayList<String> nombreCategoriasList) {
        this.nombreCategoriasList = nombreCategoriasList;
    }

    public ArrayList<String> getNombreCategoriasList() {
        return nombreCategoriasList;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setCuentasList(ArrayList<Cuenta> cuentasList) {
        this.cuentasList = cuentasList;
    }

    public ArrayList<Cuenta> getCuentasList() {
        return cuentasList;
    }

    public void setCategoriasList(ArrayList<Categoria> categoriasList) {
        this.categoriasList = categoriasList;
    }

    public ArrayList<Categoria> getCategoriasList() {
        return categoriasList;
    }

    public void setEstado(HtmlOutputLabel estado) {
        this.estado = estado;
    }

    public HtmlOutputLabel getEstado() {
        return estado;
    }
}
