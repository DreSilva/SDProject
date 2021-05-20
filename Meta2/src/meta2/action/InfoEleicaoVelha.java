package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InfoEleicaoVelha extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> eleicoes;
    private String info, eleicao;
    private ArrayList<String> infos;
    @Override
    public void validate() {
        if (eleicoes == null) {
            ArrayList<String> s = null;
            try {
                s = this.getHeyBean().getEleicoesVelhas();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            int counter = 0;
            eleicoes = new ArrayList<>();
            for (String ele : s) {
                eleicoes.add(new radioOptions(String.valueOf(counter), ele));
                counter += 1;
            }
        }
        if (eleicao == null) {
            addFieldError("tError", "Eleicao é obrigatoria.");
        }
    }

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setEleicao(eleicao);
        info = this.getHeyBean().getInfoEleicaoVelhas();
        infos = new ArrayList<>();
        infos.addAll(Arrays.asList(info.split("\n")));
        return SUCCESS;
    }


    public ArrayList<String> getInfos() {
        return infos;
    }

    public void setEleicao(String eleicao) {
        this.eleicao = String.valueOf(Integer.parseInt(eleicao)-1);
    }

    public List<radioOptions> getEleicoes() {
        return eleicoes;
    }

    public String getInfo() {
        return info;
    }

    public HeyBean getHeyBean() throws RemoteException {
        if (!session.containsKey("heyBean"))
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
