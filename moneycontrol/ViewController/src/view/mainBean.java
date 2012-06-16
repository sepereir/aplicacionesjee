package view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class mainBean{
    private HtmlInputText nombre;
    private HtmlInputSecret clave;
    private HtmlOutputLabel estado;
    private boolean logeado;

    public mainBean() {
        this.logeado = false;
    }

    public void setNombre(HtmlInputText nombre) {
        this.nombre = nombre;
    }

    public HtmlInputText getNombre() {
        return nombre;
    }

    public void setClave(HtmlInputSecret clave) {
        this.clave = clave;
    }

    public HtmlInputSecret getClave() {
        return clave;
    }

    public Object entrar() {
        // Add event code here...
        this.estado.setValue("");
        Conexion.crear();
        Usuario u = new Usuario();
        if (u.getUsuario((String)nombre.getValue(), (String)clave.getValue())){
            // Guardar usuario en la sesión
            this.logeado = true;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", u);
            return "gotomain";
        }
        else{
            //estado.setValue("Usuario y claves incorrectas");
            Conexion.cerrar();
            this.estado.setValue("Contraseña Incorrecta");
            return null;   
        }
    }
    
    public Object logout() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        Conexion.cerrar();
        return "gotologin";
    }

    public void setEstado(HtmlOutputLabel estado) {
        this.estado = estado;
    }

    public HtmlOutputLabel getEstado() {
        return estado;
    }

    public void setLogeado(boolean logeado) {
        this.logeado = logeado;
    }

    public boolean isLogeado() {
        return logeado;
    }
}
