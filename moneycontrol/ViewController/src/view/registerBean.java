package view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class registerBean {
    private HtmlInputText nombre;
    private HtmlInputText nombre_completo;
    private HtmlInputText mail;
    private HtmlInputSecret clave;
    private HtmlOutputLabel estado;
    
    public registerBean() {
    }

    public void setNombre(HtmlInputText nombre) {
        this.nombre = nombre;
    }

    public HtmlInputText getNombre() {
        return nombre;
    }

    public void setNombre_completo(HtmlInputText nombreCompleto) {
        this.nombre_completo = nombreCompleto;
    }

    public HtmlInputText getNombre_completo() {
        return nombre_completo;
    }

    public void setMail(HtmlInputText correo) {
        this.mail = correo;
    }

    public HtmlInputText getMail() {
        return mail;
    }

    public void setClave(HtmlInputSecret clave) {
        this.clave = clave;
    }

    public HtmlInputSecret getClave() {
        return clave;
    }

    public void setEstado(HtmlOutputLabel estado) {
        this.estado = estado;
    }

    public HtmlOutputLabel getEstado() {
        return estado;
    }
    
    public Object registrar() {
        Usuario u = new Usuario((String)nombre.getValue(), 
                                (String)nombre_completo.getValue(), 
                                (String)clave.getValue(), 
                                (String)mail.getValue(), false);
        Conexion.crear();
        if (u.registrarUsuario()) {
            Conexion.cerrar();
            return "success";
        } else {
            estado.setValue("Error en el registro");
            Conexion.cerrar();
            return "error";
        }
    }
}
