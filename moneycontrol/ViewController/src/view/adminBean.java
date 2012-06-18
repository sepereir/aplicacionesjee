package view;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
@RequestScoped
public class adminBean {
    private Usuario usuario;
    private HtmlInputText inputMail;
    private HtmlInputText inputNombre;
    private HtmlInputSecret inputClave;
    private HtmlInputSecret claveActual;
    private HtmlOutputText errorText;
    private ArrayList<Usuario> usuariosList;
    private Usuario usuarioSeleccionado;

    public adminBean() {
        this.usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        if(usuario.isAdmin()) actualizarLista();
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

    public void setUsuariosList(ArrayList<Usuario> usuariosList) {
        this.usuariosList = usuariosList;
    }

    public ArrayList<Usuario> getUsuariosList() {
        return usuariosList;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }
    
    public void actualizarLista(){
        ArrayList<Usuario> aux = (ArrayList<Usuario>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaUsuarios");
        this.usuariosList = aux == null ? Usuario.getAll() : aux;
    }
    
    public Object borrarUsuario(){
        usuariosList.remove(usuarioSeleccionado);
        usuarioSeleccionado.borrarUsuario();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaUsuarios", usuariosList);
        return null;
    }
    
    public Object editarUsuario(){
        usuarioSeleccionado.setEditable(true);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaUsuarios", usuariosList);
        return null;
    }
    
    public Object guardarUsuarios(){
        for(Usuario u : usuariosList){
            if(u.isEditable()){
                System.out.println(u.getClave());
                u.editarUsuario();
                u.setEditable(false);
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("listaUsuarios");
        return null;
    }
}
