import java.rmi.*;
import java.util.Date;

public interface Voto extends Remote {
    public void registo(User user) throws java.rmi.RemoteException;
    public int login(String user,String password) throws java.rmi.RemoteException;
    public void criarEleicao(Eleicao eleicao) throws java.rmi.RemoteException;
    public void gerirMesas(RMIClient cliente, String opcao) throws RemoteException;
    public String listarVotacoes() throws java.rmi.RemoteException;
    public void escolherVotacao(int opcao) throws java.rmi.RemoteException;
    public int identificarLeitor(String CC) throws java.rmi.RemoteException;
    public int votar(int opcao,User user) throws java.rmi.RemoteException;

}
