package view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
@RequestScoped
public class mainBean{
    private HtmlInputText nombre;
    private HtmlInputSecret clave;
    private HtmlOutputLabel estado;

    public mainBean() {
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
        Usuario u = new Usuario();
        if (u.getUsuario((String)nombre.getValue(), (String)clave.getValue())){
            // Guardar usuario en la sesión
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", u);
                        
            return "main2";
        }
        else{
            estado.setValue("Usuario y claves incorrectas");
            return null;   
        }
    }

    public void limpiar(ActionEvent actionEvent) {
        // Add event code here...
        this.nombre.setValue("");
        this.clave.setValue("");
        estado.setValue("");
    }

    public void setEstado(HtmlOutputLabel estado) {
        this.estado = estado;
    }

    public HtmlOutputLabel getEstado() {
        return estado;
    }
}
