package meta2.models;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import models.DepMesa;
import models.Eleicao;
import models.Lista;
import models.User;
import rmiserver.Notifications;
import rmiserver.Voto;
import uc.sd.apis.FacebookApi2;
import ws.WebSocketAnnotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class HeyBean extends UnicastRemoteObject implements Notifications {
    private static final Token EMPTY_TOKEN = null;
    private Voto server;
    private String username; // username and password supplied by the user
    private String password;
    private String CC;
    private int porto;
    private String serverAddress, clientAddress;
    private String titulo, descricao, tipo, departamento, contacto, morada;
    private Date dataInicial, dataFinal, dataValidade;
    private String lista, eleicao;
    private String nome;
    private String roomnumber;
    private String authorizationUrl;
    private String code;
    private int adminLogIn = 0;
    private int userLogIn = 0;
    private static final String apiKey = "1002478766825526";
    private static final String apiSecret = "c4fcf2bd00fa673e8c8c6c6e4cd35707";
    private OAuthService service = null;
    WebSocketAnnotation webSocketAnnotation;
    private Token token;
    private String FBid = null;


    public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream(fileName);
            // create Properties class object
            prop = new Properties();
            // load properties file into it
            prop.load(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fis.close();
        }

        return prop;
    }

    public void readDeps() throws IOException {
        String basePath = new File("").getAbsolutePath();
        basePath = basePath.concat("\\config.properties");
        System.out.println(basePath);
        Properties prop = readPropertiesFile(basePath);

        String portoInString = prop.getProperty("portoRMI");
        this.porto = Integer.parseInt(portoInString);
        serverAddress = prop.getProperty("RMIAddress");
        clientAddress = prop.getProperty("RMIAdminClient");
    }

    public HeyBean() throws RemoteException {
        super();
        try {
            //readDeps();  mudar isto
            clientAddress = "194.210.25.232";
            serverAddress = "194.210.25.232";
            porto = 7001;
            System.setProperty("java.rmi.server.hostname", clientAddress);
            Registry reg = LocateRegistry.getRegistry(serverAddress, porto);
            server = (Voto) reg.lookup("votacao");
            this.webSocketAnnotation = new WebSocketAnnotation();
            this.addNotifications();
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace(); // what happens *after* we reach this line?
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserLogIn(int userLogIn) {
        this.userLogIn = userLogIn;
    }

    public void setAdminLogIn(int adminLogIn) {
        this.adminLogIn = adminLogIn;
    }

    public int getAdminLogIn() {
        return adminLogIn;
    }

    public int getUserLogIn() {
        return userLogIn;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public OAuthService getService(String callback) {
        this.service = new ServiceBuilder()
                .apiKey(apiKey)
                .provider(FacebookApi2.class)
                .apiSecret(apiSecret)
                .callback(callback)
                .scope("public_profile")
                .build();
        return service;
    }
    public OAuthService getService() {
        return this.service;
    }

    public String getFBid() {
        return FBid;
    }

    public void setFBid(String FBid) {
        this.FBid = FBid;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getApiSecret() {
        return apiSecret;
    }

    public void setService(OAuthService service) {
        this.service = service;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCC(String CC) {
        this.CC = CC;
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

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    public void setEleicao(String eleicao) {
        this.eleicao = eleicao;
    }

    public boolean checkUserExists() throws RemoteException {
        return this.server.login(this.username, this.password, this.CC);
    }

    public ArrayList<String> getListas() throws RemoteException {
        return this.server.listarListasWEB();
    }

    public ArrayList<String> getEleicao() throws RemoteException {
        return this.server.listarEleicoesWEB();
    }

    public ArrayList<String> getListasEleicao() throws RemoteException {
        return this.server.listarListasEleicaoWEB(Integer.parseInt(this.eleicao));
    }

    public String getUserFBid(String cc) throws java.rmi.RemoteException{
        return this.server.getUserFBid(cc);
    }

    public void criarEleicao() throws RemoteException {
        Eleicao eleicao = new Eleicao(this.dataInicial, this.dataFinal, this.titulo, this.descricao, this.tipo);
        this.server.criarEleicao(eleicao);
    }

    public void registarPessoa() throws RemoteException {
        this.server.registoUser(this.username, this.password, this.departamento, this.contacto, this.tipo, this.morada, this.CC, this.dataValidade);
    }

    public ArrayList<String> getEleicaoInfo() throws RemoteException {
        return this.server.getEleicaoInfo(Integer.parseInt(this.eleicao));
    }

    public ArrayList<String> getUsers() throws RemoteException {
        return this.server.printUsersWEB();
    }

    public ArrayList<String> getUsersList() throws RemoteException {
        return this.server.getUserLista(Integer.parseInt(lista));
    }

    public boolean checkEleicaoLista(String eleicao, String lista) throws RemoteException {
        return this.server.checkListaEleicao(Integer.parseInt(eleicao), Integer.parseInt(lista));
    }

    public void addListaEleicao() throws RemoteException {
        Eleicao eleicao = this.server.getEleicao(Integer.parseInt(this.eleicao));
        Lista lista = this.server.getLista(Integer.parseInt(this.lista));
        this.server.addListaEleicao(eleicao, lista);
    }

    public void removerListaEleicao() throws RemoteException {
        Eleicao eleicao = this.server.getEleicao(Integer.parseInt(this.eleicao));
        this.server.removeListaEleicao(eleicao, Integer.parseInt(this.lista));
    }

    public void criarLista() throws RemoteException {
        Lista lista = new Lista(this.nome, this.tipo);
        this.server.addLista(lista);
    }

    public void removeLista() throws RemoteException {
        Lista l = this.server.getLista(Integer.parseInt(this.lista));
        this.server.removeLista(l);
    }

    public void addUserLista(int nUser) throws RemoteException {
        User user = this.server.getUser(nUser);
        Lista lista = this.server.getLista(Integer.parseInt(this.lista));
        this.server.addUserList(user, lista);
    }

    public void removerUserLista(int nUser) throws RemoteException {
        Lista lista = this.server.getLista(Integer.parseInt(this.lista));
        this.server.removeUserList(lista, nUser);
    }

    public void editarEleicao() throws RemoteException {
        Eleicao eleicao = this.server.getEleicao(Integer.parseInt(this.eleicao));
        this.server.changeEleicao(tipo, dataInicial, dataFinal, descricao, titulo, eleicao);
    }

    public ArrayList<String> listarMesas() throws RemoteException {
        return this.server.listaMaquinaWEB();
    }

    public void addMesaEleicao(int nMesa) throws RemoteException {
        DepMesa mesa = this.server.getMesa(nMesa);
        Eleicao eleicao = this.server.getEleicao(Integer.parseInt(this.eleicao));
        this.server.addMesaEleicao(mesa, eleicao);
    }

    public ArrayList<String> getMesasEleicao() throws RemoteException {
        return this.server.listaMaquinaEleicao(Integer.parseInt(this.eleicao));
    }

    public void removeMesaEleicao(int nMesa) throws RemoteException {
        Eleicao eleicao = this.server.getEleicao(Integer.parseInt(this.eleicao));
        this.server.removeMesaEleicao(nMesa, eleicao);
    }

    public ArrayList<String> getDepartamentos() throws RemoteException {
        return this.server.getDepartamentos();
    }

    public ArrayList<String> getEleicoesVelhas() throws RemoteException {
        return this.server.getEleicoesVelhasWeb();
    }

    public String getInfoEleicaoVelhas() throws RemoteException {
        return this.server.getInfoEleicaoVelha(Integer.parseInt(this.eleicao));
    }

    public void votar() throws RemoteException {
        this.server.votarOnline(Integer.parseInt(this.lista), this.CC, Integer.parseInt(this.eleicao));
    }

    public int checkVotar() throws RemoteException {
        return this.server.checkVotoWeb(this.CC, Integer.parseInt(eleicao));
    }

    public void addNotifications() throws RemoteException {
        this.server.subscribeAdmin(this);
    }

    public void estadoMesa(String estado, String dep, long num) throws java.rmi.RemoteException {
        webSocketAnnotation.sendMessage("Mesa: " + num + " Dep: " + dep + " Estado: " + estado, "0");
    }

    public void fimEleicao(String nome, String votos) throws java.rmi.RemoteException {
        webSocketAnnotation.sendMessage("Eleicao " + nome + " acabou", "0");
        webSocketAnnotation.sendMessage("Resultados", "0");
        String[] s = votos.split("\n");
        for (String s1 : s) {
            webSocketAnnotation.sendMessage(s1, "0");
        }
    }

    public void estadoUser(String user, String estado) throws RemoteException {
        webSocketAnnotation.sendMessage("User: " + user + " Estado: " + estado, "0");
    }

    @Override
    public void notVoto(String user, int eleicao, String mesa) throws RemoteException {
        webSocketAnnotation.sendMessage("User: " + user + " Mesa: " + mesa, String.valueOf(eleicao));
    }

    public ArrayList<String> getUsersOnline() throws java.rmi.RemoteException {
        return this.server.getUsersOnline();
    }

    public boolean AssociarFB(String token) throws java.rmi.RemoteException {
        return this.server.AssociarFB(token, this.CC);
    }

    public User LoginFB() throws java.rmi.RemoteException {
        return this.server.LoginFB(this.FBid);
    }

    public ArrayList<String> infoEleicao(int elec) throws RemoteException {
        return this.server.getFullEleicaoInfo(elec - 1);
    }

}
