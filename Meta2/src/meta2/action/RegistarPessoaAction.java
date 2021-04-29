package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.model.HeyBean;
import meta2.model.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RegistarPessoaAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> tipos;
    private List<radioOptions> departamentos;
    private String user=null,password=null,tipo=null,departamento=null,contacto=null,morada=null,CC=null;
    private Date dataValidade=null;

    @Override
    public void validate(){
        if(tipos==null){
            tipos = new ArrayList<radioOptions>();
            tipos.add( new radioOptions("Estudante", "Estudante") );
            tipos.add( new radioOptions("Docente", "Docente") );
            tipos.add( new radioOptions("Funcionario", "Funcionario") );
        }

        if(departamentos==null){
            departamentos = new ArrayList<radioOptions>();
            departamentos.add(new radioOptions("DEI", "DEI") );
            departamentos.add(new radioOptions("DEEC", "DEEC") );
            departamentos.add(new radioOptions("DEM", "DEM") );
        }

        if(user.equals("") ){
            addFieldError("tError", "Username é obrigatorio.");
        }
        if(password.equals("")){
            addFieldError("descricao", "Password é obrigatoria.");
        }
        if(tipo==null){
            addFieldError("hourI", "Hora Inicial é obrigatoria.");

        }
        if(departamento==null){
            addFieldError("tipo", "Departamento é obrigatoria.");
        }
        if(contacto.equals("")){
            addFieldError("hourF", "Contacto é obrigatoria.");

        }
        if(morada.equals("")){
            addFieldError("hourF", "Morada é obrigatoria.");

        }
        if(CC.equals("")){
            addFieldError("hourF", "CC é obrigatoria.");

        }
        if(dataValidade==null){
            addFieldError("hourF", "Data de validade é obrigatoria.");
        }

    }

    @Override
    public String execute(){
        this.getHeyBean().setCC(CC);
        this.getHeyBean().setContacto(contacto);
        this.getHeyBean().setMorada(morada);
        this.getHeyBean().setDepartamento(departamento);
        this.getHeyBean().setTipo(tipo);
        this.getHeyBean().setDataValidade(dataValidade);
        this.getHeyBean().setUsername(user);
        this.getHeyBean().setPassword(password);
        return SUCCESS;
    }

    public HeyBean getHeyBean(){
        if(!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }


    public void setTipos(List<radioOptions> tipos) {
        this.tipos = tipos;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDepartamentos(List<radioOptions> departamentos) {
        this.departamentos = departamentos;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public void setCC(String CC) {
        this.CC = CC;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public List<radioOptions> getDepartamentos() {
        return departamentos;
    }

    public List<radioOptions> getTipos() {
        return tipos;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

}
