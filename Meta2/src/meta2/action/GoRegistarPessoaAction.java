package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoRegistarPessoaAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> tipos;
    private List<radioOptions> departamentos;

    @Override
    public String execute(){
        tipos = new ArrayList<radioOptions>();
        tipos.add( new radioOptions("Estudante", "Estudante") );
        tipos.add( new radioOptions("Docente", "Docente") );
        tipos.add( new radioOptions("Funcionario", "Funcionario") );
        //MUDAR ISTO PARA LER DO FICHEIRO
        departamentos = new ArrayList<radioOptions>();
        departamentos.add(new radioOptions("DEI", "DEI") );
        departamentos.add(new radioOptions("DEEC", "DEEC") );
        departamentos.add(new radioOptions("DEM", "DEM") );
        return SUCCESS;
    }


    public List<radioOptions> getTipos() {
        return tipos;
    }

    public List<radioOptions> getDepartamentos() {
        return departamentos;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
