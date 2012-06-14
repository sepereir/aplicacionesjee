package view;

import java.sql.Connection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlOutputFormat;
import javax.faces.context.FacesContext;
import javax.swing.JPanel;

@ManagedBean
@RequestScoped
public class categoriasBean{
    private Usuario usuario;
    private Categoria categoriaActual;
    private Categoria[] categorias;
    private Transaccion[] transacciones;
    private String[] nombres;
    private boolean hayCategorias;

    public categoriasBean() throws Exception {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        usuario.setCategorias(usuario.getNombre());
        this.categorias = usuario.getCategorias();
        if(setHayCategorias()){
            setNombres();
            categoriaActual = categorias[0];
        }
    }

    public Categoria[] getCategorias(){
        return this.categorias;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setCategorias(Categoria[] categorias2) {
        this.categorias = categorias2;
    }
    
    public void setCategoriaActual(String nombreCategoriaActual) throws Exception {
        for(int i = 0; i < categorias.length ; i++){
            if(categorias[i].getNombre() == nombreCategoriaActual)
                categoriaActual = categorias[i];
                return;
            }
        throw new Exception("Se pide fijar como categoria actual una categoria inexistente");
    }
    
    public Categoria getCategoriaActual(){
        return categoriaActual;
    }
    
    public void setNombres() throws Exception {
        try{
            nombres = new String[categorias.length];
            for(int i = 0; i < categorias.length; i++ )
                nombres[i] = categorias[i].getNombre();
        }
        catch(Exception e){
            throw new Exception("Se intenta obtener nombres de cuentas cuando no hay cuentas");
        }
    }
    public String[] getNombres(){
        return nombres;
    }
    
    public boolean getHayCategorias(){
        return hayCategorias;
    }
    
    public boolean setHayCategorias(){
        if(categorias == null || categorias.length == 0)
            this.hayCategorias = false;
        else
            this.hayCategorias = true;
        return this.hayCategorias;
    }
    
    public void setTransacciones(Cuenta cuenta){
        categoriaActual.setGastos(cuenta);
    }
    
    public Transaccion[] getTransacciones(){
        return categoriaActual.getGastos();
    }
}
