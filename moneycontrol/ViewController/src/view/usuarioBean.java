import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;

import view.Usuario;

@ManagedBean(name = "usuarioBean")
@SessionScoped
public class usuarioBean{

    private Usuario usuario;
    private boolean tieneCuentas;
    private boolean tieneCategorias;
    
    public usuarioBean(){
        usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        setTieneCuentas();
        setTieneCategorias();
    }

    public void setTieneCuentas(){
        usuario.setCuentas();
        try{
            tieneCuentas = (usuario.getCuentas().length > 0);
        }
        catch(Exception e){
            tieneCuentas = false;
        }
    }
    public boolean getTieneCuentas(){
        setTieneCuentas();
        return tieneCuentas;
    }

    public void setTieneCategorias(){
        usuario.setCategorias();
        try{
            tieneCategorias = (usuario.getCategorias().length > 0);
        }
        catch(Exception e){
            tieneCategorias = false;
        }
    }
    public boolean getTieneCategorias(){
        setTieneCategorias();
        return tieneCategorias;
    }
}