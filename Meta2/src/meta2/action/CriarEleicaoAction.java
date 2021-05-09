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


public class CriarEleicaoAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String titulo= null,descricao= null,tipo= null,hourI=null,hourF=null;
    private Date dataInicial=null,dataFinal=null;
    private List<radioOptions> tipos;

    @Override
    public void validate(){
        if(tipos==null){
            tipos = new ArrayList<radioOptions>();
            tipos.add( new radioOptions("Estudante", "Estudante") );
            tipos.add( new radioOptions("Docente", "Docente") );
            tipos.add( new radioOptions("Funcionario", "Funcionario") );
        }

        if(titulo.equals("") ){
            addFieldError("tError", "Titulo é obrigatorio.");
        }
        if(descricao.equals("")){
            addFieldError("descricao", "Descricao é obrigatoria.");
        }
        if(hourF.equals("")){
            addFieldError("hourI", "Hora Inicial é obrigatoria.");

        }
        if(tipo.equals("")){
            addFieldError("tipo", "Tipo é obrigatoria.");
        }
        if(hourI.equals("")){
            addFieldError("hourF", "Hora Final é obrigatoria.");

        }
        if(dataInicial!=null && dataFinal!=null){
            if(!hourI.equals("") && !hourF.equals("")){
                String[] horaI = hourI.split(":");
                long hourMs = Long.parseLong(horaI[0])*3600000;
                long minMs = Long.parseLong(horaI[1])*60000;
                String[] horaF = hourF.split(":");
                long hourMsF = Long.parseLong(horaF[0])*3600000;
                long minMsF = Long.parseLong(horaF[1])*60000;
                dataInicial.setTime(hourMs+minMs+dataInicial.getTime());
                dataFinal.setTime(hourMsF+minMsF+dataFinal.getTime());
                if(dataInicial.after(dataFinal)){
                    addFieldError("dataInicial", "Data Inicial deve ser antes que a Final.");
                }
            }
        }
        if(dataInicial==null){
            addFieldError("dataInicial", "Data Inicial é obrigatorio.");

        }
        if(dataFinal==null){
            addFieldError("dataFinal", "Data Final é obrigatoria.");
        }
    }

    @Override
    public String execute() throws RemoteException {
        this.getHeyBean().setDataFinal(dataFinal);
        this.getHeyBean().setDataInicial(dataInicial);
        this.getHeyBean().setDescricao(descricao);
        this.getHeyBean().setTipo(tipo);
        this.getHeyBean().setTitulo(titulo);
        this.getHeyBean().criarEleicao();
        return SUCCESS;
    }


    public void setTipos(List<radioOptions> tipos) {
        this.tipos = tipos;
    }


    public List<radioOptions> getTipos() {
        return tipos;
    }

    public HeyBean getHeyBean() throws RemoteException {
        if(!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }

    public void setHourF(String hourF) {
        this.hourF = hourF;
    }

    public void setHourI(String hourI) {
        this.hourI = hourI;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
