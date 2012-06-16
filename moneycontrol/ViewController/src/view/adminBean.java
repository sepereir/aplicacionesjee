package view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class adminBean {
    private Usuario usuario;
    private HtmlInputText inputMail;
    private HtmlInputText inputNombre;
    private HtmlInputSecret inputClave;
    private HtmlInputSecret claveActual;
    private HtmlOutputText errorText;

    public adminBean() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
    }

    public void setInputMail(HtmlInputText inputMail) {
        this.inputMail = inputMail;
    }

    public HtmlInputText getInputMail() {
        return inputMail;
    }

    public void setInputNombre(HtmlInputText inputNombre) {
        this.inputNombre = inputNombre;
    }

    public HtmlInputText getInputNombre() {
        return inputNombre;
    }

    public void setInputClave(HtmlInputSecret inputClave) {
        this.inputClave = inputClave;
    }

    public HtmlInputSecret getInputClave() {
        return inputClave;
    }

    public void setClaveActual(HtmlInputSecret claveActual) {
        this.claveActual = claveActual;
    }

    public HtmlInputSecret getClaveActual() {
        return claveActual;
    }

    public void setErrorText(HtmlOutputText errorText) {
        this.errorText = errorText;
    }

    public HtmlOutputText getErrorText() {
        return errorText;
    }

    public Object cambiarDatos() {
        errorText.setValue("");
        if(!claveActual.getValue().toString().equals(usuario.getClave())){
            errorText.setValue("Contraseña actual incorrecta");
            return null;
        }
        
        usuario.setNombre_completo(inputNombre.getValue().toString());
        usuario.setCorreo(inputMail.getValue().toString());
        usuario.setClave(inputClave.getValue().toString());
        if(!usuario.editarUsuario()){
            errorText.setValue("Error al conectar con la base de datos.");
            return null;
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuario);
        return "gotomain";
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
