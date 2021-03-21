import java.rmi.*;
import java.util.Date;

public interface Voto extends Remote {
    public void registo(User user) throws java.rmi.RemoteException;
    public boolean login(String user,String password, String CC) throws java.rmi.RemoteException;
    public void criarEleicao(Eleicao eleicao) throws java.rmi.RemoteException;
    public void gerirMesas(DepMesa cliente, String opcao, Eleicao eleicao) throws RemoteException;
    public String listarVotacoes() throws java.rmi.RemoteException;
    public boolean identificarLeitor(String CC) throws java.rmi.RemoteException;
    public String votar(int opcao,String CC,int nEleicao, DepMesa mesa) throws java.rmi.RemoteException;
    public String listarEleicoes() throws java.rmi.RemoteException;
    public Eleicao getEleicao(int n) throws java.rmi.RemoteException;
    public void criarLista(Lista lista) throws java.rmi.RemoteException;
    public String listarListas() throws java.rmi.RemoteException;
    public Lista getLista(int n) throws java.rmi.RemoteException;
    public void addLista(Lista lista)throws java.rmi.RemoteException;
    public void addListaEleicao(Eleicao eleicao,Lista lista)throws java.rmi.RemoteException;
    public void removeListaEleicao(Eleicao eleicao,Lista lista)throws java.rmi.RemoteException;
    public String printUsers() throws java.rmi.RemoteException;
    public User getUser(int pos) throws java.rmi.RemoteException;
    public void addUserList(User user,Lista lista) throws java.rmi.RemoteException;
    public void removeEleicao(Eleicao eleicao) throws java.rmi.RemoteException;
    public void removeLista(Lista lista)throws java.rmi.RemoteException;
    public void removeUserList(Lista lista,User user) throws java.rmi.RemoteException;
    public String getEleicoesVelhas() throws  java.rmi.RemoteException;
    public String getInfoEleicaoVelha(int pos)throws java.rmi.RemoteException;
    public void writeFile() throws java.rmi.RemoteException; //debug
    public void setTitulo(String titulo,Eleicao eleicao) throws java.rmi.RemoteException;
    public void setDescricao(String Descricao,Eleicao eleicao) throws java.rmi.RemoteException;
    public void setDatas(Date dataI,Date dataf,Eleicao eleicao) throws java.rmi.RemoteException;
    public void setTipo(String Tipo,Eleicao eleicao) throws java.rmi.RemoteException;
    public String listaCandidatos(int n) throws java.rmi.RemoteException;
    public void subscribeAdmin(AdminConsole adminConsole) throws java.rmi.RemoteException;
}
