package rmiserver;

import java.rmi.Remote;

public interface Notifications extends Remote {
    /**
     * Função que notifica a admin console sobre o estado da mesa
     * @param estado estado da maquina, on ou off
     * @param dep departamento da maquina
     * @param num id da maquina
     */
    public void estadoMesa(String estado,String dep,long num) throws  java.rmi.RemoteException;

    /**
     * Função que notifica a admin console quando uma eleição termina
     * @param nome Nome da eleição
     * @param votos Votos que cada lista obteve
     */
    public void fimEleicao(String nome,String votos) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Indica estado de utilizador
     * @param user User
     * @param estado ON ou OFF
     */
    public void estadoUser(String user,String estado) throws java.rmi.RemoteException;

    /**
     * Notificacao de voto
     * @param user Utilizador que votou
     * @param eleicao Eleicao onde votou
     * @param mesa Mesa onde votou
     * @throws java.rmi.RemoteException
     */
    public void notVoto(String user,int eleicao,String mesa) throws java.rmi.RemoteException;
}
