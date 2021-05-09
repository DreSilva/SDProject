package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoRegistarPessoaAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> tipos;
    private List<radioOptions> departamentos;

    @Override
    public String execute() throws RemoteException {
        tipos = new ArrayList<radioOptions>();
        tipos.add( new radioOptions("Estudante", "Estudante") );
        tipos.add( new radioOptions("Docente", "Docente") );
        tipos.add( new radioOptions("Funcionario", "Funcionario") );
        departamentos=new ArrayList<>();
        ArrayList<String> s = this.getHeyBean().getDepartamentos();
        for (String dep: s) {
            departamentos.add(new radioOptions(dep,dep));
        }
        return SUCCESS;
    }


    public HeyBean getHeyBean() throws RemoteException {
        if(!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
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
