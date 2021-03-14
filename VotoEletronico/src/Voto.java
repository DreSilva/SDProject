import java.rmi.*;
import java.util.Date;

public interface Voto extends Remote {
    public void registo(User user) throws java.rmi.RemoteException;
    public boolean login(String user,String password, String CC) throws java.rmi.RemoteException;
    public void criarEleicao(Eleicao eleicao) throws java.rmi.RemoteException;
    public void gerirMesas(DepMesa cliente, String opcao, Eleicao eleicao) throws RemoteException;
    public String listarVotacoes() throws java.rmi.RemoteException;
    public boolean identificarLeitor(String CC) throws java.rmi.RemoteException;
    public void votar(int opcao,User user,Eleicao eleicao, DepMesa mesa) throws java.rmi.RemoteException;

}
