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
public class cuentasBean{
    private Usuario usuario;
    private Cuenta[] cuentas;
    private HtmlInputText agregarCuenta;
    private HtmlInputText agregarComentario;

    public cuentasBean(){
        usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        usuario.setCuentas(usuario.getNombre());
        this.cuentas = usuario.getCuentas();
    }
    
    public void agregarCuenta(ActionEvent actionEvent){
        int i;
        if(this.cuentas == null){
            this.cuentas = new Cuenta[1];
            this.cuentas[0] = new Cuenta((String)this.agregarCuenta.getValue(), (String)this.agregarComentario.getValue(), usuario.getNombre());
            return;
        }
        Cuenta[] cuentas2 = new Cuenta[cuentas.length+1];
        for(i = 0; i<cuentas2.length-1;++i){
            cuentas2[i] = this.cuentas[i];
        }
        cuentas2[i] = new Cuenta((String)this.agregarCuenta.getValue(), (String)this.agregarComentario.getValue(), usuario.getNombre());
        cuentas2[i].crearCuenta(cuentas2[i]);
        this.cuentas = cuentas2;
        
    }

    public void setCuentas(Cuenta[] cuentas) {
        this.cuentas = cuentas;
    }

    public Cuenta[] getCuentas() {
        return cuentas;
    }

    public void setAgregarCuenta(HtmlInputText agregarCuenta) {
        this.agregarCuenta = agregarCuenta;
    }

    public HtmlInputText getAgregarCuenta() {
        return agregarCuenta;
    }

    public void setAgregarComentario(HtmlInputText agregarComentario) {
        this.agregarComentario = agregarComentario;
    }

    public HtmlInputText getAgregarComentario() {
        return agregarComentario;
    }
}
