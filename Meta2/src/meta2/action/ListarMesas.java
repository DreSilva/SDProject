package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListarMesas extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> mesas;

    @Override
    public String execute() throws Exception {
        mesas = new ArrayList<>();
        ArrayList<String> s = this.getHeyBean().listarMesas();
        int count=0;
        for (String mesa: s) {
            mesas.add(new radioOptions(String.valueOf(count),mesa));
            count+=1;
        }
        return SUCCESS;
    }

    public List<radioOptions> getMesas() {
        return mesas;
    }


    public HeyBean getHeyBean(){
        if(!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }


    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
