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
    Usuario usuario;
    private Categoria[] categorias;

    public categoriasBean() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        usuario.setCategorias(usuario.getNombre());
        this.categorias = usuario.getCategorias();
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
}
