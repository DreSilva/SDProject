package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EditarEleicao extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> tipos;
    private String tipo,titulo,descricao,horaI,horaF;
    private Date dataInicial,dataFinal;

    @Override
    public void validate() {
        if (tipos == null) {
            tipos = new ArrayList<radioOptions>();
            tipos.add(new radioOptions("Estudante", "Estudante"));
            tipos.add(new radioOptions("Docente", "Docente"));
            tipos.add(new radioOptions("Funcionario", "Funcionario"));
        }
        if(tipo==null){
            addFieldError("tError", "Tipo de Eleição é Obrigatorio.");
        }
        if(titulo.equals("")){
            addFieldError("tError", "Titulo da Eleição é Obrigatorio.");
        }
        if(descricao.equals("")){
            addFieldError("tError", "Decrição da Eleição é Obrigatorio.");
        }
        if(horaF.equals("")){
            addFieldError("tError", "Hora Final da Eleição é Obrigatorio.");
        }
        if(horaI.equals("")){
            addFieldError("tError", "Hora Inicial da Eleição é Obrigatorio.");
        }
        if(dataFinal==null){
            addFieldError("tError", "Data Final da Eleição é Obrigatorio.");
        }
        if(dataInicial==null){
            addFieldError("tError", "Data Inicial da Eleição é Obrigatorio.");
        }
        if(dataInicial!=null && dataFinal!=null){
            if(!horaI.equals("") && !horaF.equals("")){
                String[] horaI = this.horaI.split(":");
                long hourMs = Long.parseLong(horaI[0])*3600000;
                long minMs = Long.parseLong(horaI[1])*60000;
                String[] horaF = this.horaF.split(":");
                long hourMsF = Long.parseLong(horaF[0])*3600000;
                long minMsF = Long.parseLong(horaF[1])*60000;
                dataInicial.setTime(hourMs+minMs+dataInicial.getTime());
                dataFinal.setTime(hourMsF+minMsF+dataFinal.getTime());
                if(dataInicial.after(dataFinal)){
                    addFieldError("dataInicial", "Data Inicial deve ser antes que a Final.");
                }
            }
        }
    }

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setTipo(tipo);
        this.getHeyBean().setTitulo(titulo);
        this.getHeyBean().setDataInicial(dataInicial);
        this.getHeyBean().setDataFinal(dataFinal);
        this.getHeyBean().setDescricao(descricao);
        this.getHeyBean().editarEleicao();
        return SUCCESS;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public void setHoraF(String horaF) {
        this.horaF = horaF;
    }

    public void setHoraI(String horaI) {
        this.horaI = horaI;
    }

    public HeyBean getHeyBean() throws RemoteException {
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
