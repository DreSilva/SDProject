package rmiserver;

import java.rmi.*;
import java.util.ArrayList;
import java.util.Date;
import models.*;

/**
 *
 */
public interface Voto extends Remote {
    /**
     * Cria registo de utilizador e adiciona-o a lista de utilizadores
     * @param user utilizador a registar
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String registo(User user) throws java.rmi.RemoteException;

    public String registoUser(String user, String password, String departamento,
                              String contacto, String tipo, String morada, String CC, Date validade) throws java.rmi.RemoteException;

    /**
     * Verifica se o user existe registado ou não, através da password e CC
     * @param user nome de utilizador
     * @param password password do utilizador
     * @param CC numero de cartao de cidadao do utilizador
     * @return retorna true caso o utilizador exista e false se nao existir
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public boolean login(String user,String password, String CC) throws java.rmi.RemoteException;

    /**
     * Cria eleicao
     * @param eleicao eleicao a criar
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void criarEleicao(Eleicao eleicao) throws java.rmi.RemoteException;

    /**
     * associar token do fb ao user
     * @param token token fornecido pelo fb
     * @param CC user que vai ser associado o token
     */
    public void AssociarFB(String token,String CC);

    /**
     * login com FB
     * @param token token do fb
     * @param CC user que deseja fazer login
     * @return se existe user com o token ou nao
     */

    public boolean LoginFB(String token,String CC);


    /**
     * Verifica se o user com o CC dado existe
     * @param CC Cartao de Cidadao do utilizador
     * @return True se existe False se não existe
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public boolean identificarLeitor(String CC) throws java.rmi.RemoteException;

    /**
     * Função que vai efetuar o voto do utilizador
     * @param opcao Lista a votar
     * @param CC Cartao de Cidadao do utilizador
     * @param nEleicao Eleição a selecionar
     * @param mesa Mesa de voto onde se votou
     * @return String com informação sobre o estado do voto, se foi efetuado ou se houve algum problema
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String votar(int opcao,String CC,int nEleicao, DepMesa mesa) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Lista todas as eleições diponiveis para votar
     * @return String com as eleições
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String listarEleicoes() throws java.rmi.RemoteException;

    /**
     * Retorna Eleicao selecionada
     * @param n numero da opção a retornar
     * @return eleicao selecionada
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public Eleicao getEleicao(int n) throws java.rmi.RemoteException;

    /**
     * Lista todas as listas guardadas na BD
     * @return String com todas as listas guardads
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String listarListas() throws java.rmi.RemoteException;

    /**
     * Retorna Lista selecionada
     * @param n numero da opção a retornar
     * @return lista selecionada
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public Lista getLista(int n) throws java.rmi.RemoteException;

    /**
     * Adiciona Lista a BD
     * @param lista lista a adicionar
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void addLista(Lista lista)throws java.rmi.RemoteException;

    /**
     * Adiciona uma lista a uma eleição para ser possivel votar nela
     * @param eleicao eleicao onde se quer adicionar lista
     * @param lista lista a adicionar a eleicao
     * @return  informarcao sobre o sucesso da função
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String addListaEleicao(Eleicao eleicao, Lista lista)throws java.rmi.RemoteException;

    /**
     * Remove uma lista a uma eleição
     * @param eleicao eleição onde se quer remover lista
     * @param opcao opcao selecionada
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void removeListaEleicao(Eleicao eleicao, int opcao)throws java.rmi.RemoteException;

    /**
     * Funcao para dar print aos users
     * @return String com informação dos utilizadores
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String printUsers() throws java.rmi.RemoteException;

    /**
     * Funcao para dar print aos users
     * @return String com informação dos utilizadores
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> printUsersWEB() throws java.rmi.RemoteException;

    /**
     * Retorna utilizador na posição n da BD
     * @param pos posiçao que se quer obter
     * @return Utilizador Selecionado
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public User getUser(int pos) throws java.rmi.RemoteException;

    /**
     * Adiciona um utilizador a uma lista de eleições
     * @param user User a adicionar
     * @param lista lista a adicionar utilizador
     * @return Mensagem sobre sucesso ou falha de adicionar o user
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String addUserList(User user, Lista lista) throws java.rmi.RemoteException;

    /**
     * Remover lista da BD do servidor
     * @param lista lista a remover
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void removeLista(Lista lista)throws java.rmi.RemoteException;

    /**
     * Remover um utilizador de uma lista
     * @param lista Lista selecionada para remover
     * @param nUser user para remover da lista
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void removeUserList(Lista lista, int nUser) throws java.rmi.RemoteException;

    /**
     * Lista todas as eleições que ja pasaram
     * @return String com informação sobre as eleições passadas
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String getEleicoesVelhas() throws  java.rmi.RemoteException;

    /**
     * Retorna informação sobre eleição antiga selecionada
     * @param pos Eleição a selecionar
     * @return String com informação sobre essa eleição
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String getInfoEleicaoVelha(int pos)throws java.rmi.RemoteException;

    /**
     * Vai guardar no ficheiro os users,listas e eleições atualmente no server
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void writeFile() throws java.rmi.RemoteException;

    /**
     * Mudar o titulo de uma eleição
     * @param titulo Novo titulo
     * @param eleicao Eleição que se quer mudar
     * @throws java.rmi.RemoteException  excepção que pode ocorrer na execução de uma remote call
     */
    public void setTitulo(String titulo, Eleicao eleicao) throws java.rmi.RemoteException;

    /**
     * Mudar a descrição de uma eleição
     * @param Descricao Nova Descrição
     * @param eleicao Eleição que ser quer mudar
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void setDescricao(String Descricao, Eleicao eleicao) throws java.rmi.RemoteException;

    /**
     * Mudar as datas de uma eleição
     * @param dataI Data inicio
     * @param dataf Data fim
     * @param eleicao Eleicao a mudar
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void setDatas(Date dataI, Date dataf, Eleicao eleicao) throws java.rmi.RemoteException;

    /**
     * Mudar o tipo de uma eleição
     * @param Tipo Tipo a mudar
     * @param eleicao Eleição a mudar
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void setTipo(String Tipo, Eleicao eleicao) throws java.rmi.RemoteException;


    /**
     * Listar elementos de uma lista
     * @param lista lista a listar elementos
     * @return Lista dos elemtnos
     * @throws java.rmi.RemoteException
     */
    public String listarLista(Lista lista) throws  java.rmi.RemoteException;


    /**
     * Vai obter todas as listas inscritas numa eleição
     * @param n Eleição a escolher
     * @return String com a lista de candidatos
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String listaCandidatos(int n) throws java.rmi.RemoteException;

    /**
     * Adiciona a class do Admin Console para ser possivel enviar notificações
     * @param nAdmin admin console para a qual se quer enviar notificações
     * @throws RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void subscribeAdmin(Notifications nAdmin) throws java.rmi.RemoteException;


    /**
     * Adicionar mesa de voto
     * @param mesa Mesa a adicionar
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String addMesa(DepMesa mesa) throws java.rmi.RemoteException;

    /**
     * Obtem a mesa de voto correta
     * @param opcao opcao selecionada
     * @throws java.rmi.RemoteException  excepção que pode ocorrer na execução de uma remote call
     */
    public DepMesa getMesa(int opcao) throws java.rmi.RemoteException;

    /**
     * Adiciona uma mesa de voto a uma eleicao
     * @param Mesa Mesa a adicionar
     * @param eleicao Eleicao a ser adicionada mesa
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String addMesaEleicao(DepMesa Mesa, Eleicao eleicao) throws  java.rmi.RemoteException;

    /**
     * Remove uma mesa de voto a uma eleicao
     * @param opcao Mesa a adicionar
     * @param eleicao Eleicao a ser adicionada mesa
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void removeMesaEleicao(int opcao, Eleicao eleicao) throws  java.rmi.RemoteException;

    /**
     * Lista as mesas nos departamentos
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     * @return Lista das mesas
     */
    public String listaMaquina() throws java.rmi.RemoteException;

    /**
     * Obtem o numero de votantes numa eleição
     * @param opcao eleição selecionada
     * @return numero de votantes numa eleição
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public int numVotantes(int opcao) throws java.rmi.RemoteException;

    /**
     * remove mesa da lista de mesas e notifica a admin console
     * @param mesa Mesa a eliminar
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void removeMesa(DepMesa mesa) throws java.rmi.RemoteException;

    /**
     * Obtem as listas da eleição selecionad
     * @param n Eleicção selecionamda
     * @return String com as listas da eleição
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public String listarListasEleicao(int n) throws java.rmi.RemoteException;


    /**
     * Obtem as listas da eleição selecionad
     * @param n Eleicção selecionamda
     * @return String com as listas da eleição
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> listarListasEleicaoWEB(int n) throws java.rmi.RemoteException;


    /**
     * Obtem as eleicoes para a web
     * @return ArrayLista com Strings
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> listarEleicoesWEB() throws java.rmi.RemoteException;


    /**
     * Lista todas as listas guardadas na BD para a web
     * @return ArrayLista com todas as listas guardadas
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> listarListasWEB() throws java.rmi.RemoteException;

    /**
     * Lista users de uma lista
     * @return ArrayLista com todas as listas guardadas
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> getUserLista(int n) throws java.rmi.RemoteException;

    /**
     * Retorna informacao sobre eleição selecionada
     * @return ArrayLista com as informações da eleição
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> getEleicaoInfo(int n) throws java.rmi.RemoteException;


    /**
     * Checa se a lista esta na eleição ou nao
     * @param nEleicao indice da eleicao a checar
     * @param nLista indice da lista a verificar
     * @return Se existe ou nao
     */
    public boolean checkListaEleicao(int nEleicao,int nLista) throws RemoteException;


    /**
     * Alterar Eleicao
     * @param Tipo novo tipo da eleicao
     * @param dataI nova data inicial
     * @param dataf nova data final
     * @param Descricao nova descricao
     * @param titulo novo titulo
     * @param eleicao eleicao a mudar
     * @throws java.rmi.RemoteException java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void changeEleicao(String Tipo,Date dataI, Date dataf,String Descricao,String titulo,Eleicao eleicao) throws java.rmi.RemoteException;


    /**
     * Listar Maquinas existentes para a WEB
     * @return array de listas para a web
     * @throws java.rmi.RemoteException java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> listaMaquinaWEB() throws java.rmi.RemoteException;

    /**
     * Listar Maquinas existentes a uma eleicao
     * @return array de maquias pertencentes a eleicao
     * @throws java.rmi.RemoteException java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> listaMaquinaEleicao(int n) throws java.rmi.RemoteException;

    /**
     *  Obter os departamentos
     * @return Retorna os departamentos
     * @throws java.rmi.RemoteException java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> getDepartamentos() throws java.rmi.RemoteException;

    /**
     * Lista todas as eleições que ja pasaram
     * @return String com informação sobre as eleições passadas
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> getEleicoesVelhasWeb() throws  java.rmi.RemoteException;

    /**
     * Checa se o user pode votar ou nao na eleicao
     * @param CC CC do user
     * @param nEleicao eleicao a verificar
     * @return Identificador de erro
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public int checkVotoWeb(String CC, int nEleicao) throws java.rmi.RemoteException;


    /**
     * Votar online
     * @param opcao lista que se vota
     * @param CC CC do user que se vota
     * @param nEleicao Numero da Eleicao a votar
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void votarOnline(int opcao, String CC, int nEleicao) throws java.rmi.RemoteException, InterruptedException;

    /**
     * Obter utilizadores online
     * @return ArrayLista com Nome dos utilziadores loggedin
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> getUsersOnline() throws java.rmi.RemoteException;

    /**
     * Obter Iormação sobre uma lista
     * @param n Eleicao a obter info
     * @return Array List com toda a Informação de uma lista
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public ArrayList<String> getFullEleicaoInfo(int n) throws java.rmi.RemoteException;
}
