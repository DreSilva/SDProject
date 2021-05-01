package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class AdicionarListaEleicao extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;


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
