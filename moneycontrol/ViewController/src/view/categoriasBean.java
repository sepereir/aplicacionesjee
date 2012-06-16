package view;

import java.sql.Connection;

import java.sql.SQLException;

import java.util.ArrayList;

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
    private String[] nombres;
    private boolean hayCategorias;
    private ArrayList<Categoria> categoriasList;
    private Categoria categoriaSeleccionada;

    public categoriasBean() throws Exception {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        usuario.setCategorias();
        this.categorias = usuario.getCategorias();
        if(setHayCategorias()){
            setNombres();
            categoriaActual = categorias[0];
        }
        actualizarLista();
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
    
    public void setCategoriaSeleccionada(Categoria categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }
    public Categoria getCategoriaSeleccionada() {
        return categoriaSeleccionada;
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
    
    public void setCuentasList(ArrayList<Categoria> categoriasList) {
        this.categoriasList = categoriasList;
    }
    public ArrayList<Categoria> getCategoriasList() throws Exception {
        return categoriasList;
    }
    
    public void actualizarLista(){
        
        this.categoriasList = new ArrayList<Categoria>();
        
        if (usuario.setCuentas()) {
            Categoria[] categorias = usuario.getCategorias();
            if(categorias != null) for (int i = 0; i < categorias.length; ++i) {
                this.categoriasList.add(categorias[i]);
            }
        }
        this.categorias = usuario.getCategorias();
    }
    
    public String borrarCategorias() {
        categoriasList.remove(categoriaSeleccionada);
        categoriaSeleccionada.borrarCategoria();
        return null;
    }

    public String editarCategorias() {
        categoriaSeleccionada.setEditable(true);
        return null;
    }
    
    public String guardarCategoria(){
        //get all existing value but set "editable" to false 
        for (Categoria c : categoriasList){
            if(c.isEditable()){
                c.editarCategoria();
                c.setEditable(false);
                System.out.println("se guardo " + c.getNombre() + "ID: " + c.getId());
            }
        }
        return null;
    }

    public String nuevaCategoria() {
        Categoria nueva;
        if(categoriasList != null)
            nueva = new Categoria("Categoria " + (categoriasList.size() + 1), usuario.getNombre());
        else
            nueva = new Categoria("Categoria 1", usuario.getNombre());
        
        nueva.crearCategoria();
        categoriasList.add(nueva);
        actualizarLista();
        return null;
    }
}
