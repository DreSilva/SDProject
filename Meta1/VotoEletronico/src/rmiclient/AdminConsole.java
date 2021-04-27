package rmiclient;

import models.DepMesa;
import models.Eleicao;
import models.Lista;
import models.User;
import rmiserver.Notifications;
import rmiserver.Voto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminConsole extends UnicastRemoteObject implements Notifications {
    int porto;
    String serverAddress,clientAddress;
    ArrayList<String> Departamentos = new ArrayList<>();

    /**
     * Cria objeto da consola para passar ao RMI Server
     */
    public AdminConsole() throws RemoteException {
        super();
    }

    /**
     * @inheritDoc
     */
    public void estadoMesa(String estado,String dep,long num) throws  java.rmi.RemoteException{
        System.out.println("Mesa: "+num+" Dep: "+dep+" Estado: "+estado);
    }

    /**
     * @inheritDoc
     */
    public void fimEleicao(String nome,String votos) throws  java.rmi.RemoteException{
        System.out.println("models.Eleicao "+nome+" acabou.\nResultados");
        System.out.println(votos);
    }


    /**
     * Abre o ficheiro de config para leitura
     * @param fileName ficheiro para abrir
     * @return propreties file
     */
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

            //e.printStackTrace();
        } catch (IOException e) {

           // e.printStackTrace();
        } finally {
            fis.close();
        }

        return prop;
    }

    /**
     * Le as configurações do ficheiro de config
     */
    public void readDeps() throws IOException {

        Properties prop = readPropertiesFile("config.properties");
        String portoInString = prop.getProperty("portoRMI");
        this.porto = Integer.parseInt(portoInString);
        String deps = prop.getProperty("departamento");
        String[] depsSplit = deps.split(",");
        Departamentos.addAll(Arrays.asList(depsSplit));
        serverAddress =  prop.getProperty("RMIAddress");
        clientAddress = prop.getProperty("RMIAdminClient");


    }

    /**
     * Esta função vai permitir ao admin fazer todas as ações necessárias para por as votações a funcionar. Permite
     * criar e remover eleições,utilizadores e listas.
     * @throws RemoteException excepção que pode ocorrer na execução de uma remote call
     * @throws NotBoundException Acontece caso o o objeto a procurar no registo nao exista
     */
    public static void main(String[] args) throws IOException, NotBoundException {
        Scanner readInput = new Scanner(System.in);
        AdminConsole admin = new AdminConsole();


        admin.readDeps();

        System.setProperty("java.rmi.server.hostname", admin.clientAddress);


        Registry reg = LocateRegistry.getRegistry(admin.serverAddress,admin.porto);

        Voto votoObj = (Voto) reg.lookup("votacao");

        ArrayList<String> opcoes =  new ArrayList<>();


        votoObj.subscribeAdmin((Notifications) admin);


        boolean accao = false;

        opcoes.add("Escolher Opcao");
        opcoes.add("1-Registar Pessoa");
        opcoes.add("2-Criar models.Eleicao");
        opcoes.add("3-Gerir models.Lista de Candidatos");
        opcoes.add("4-Gerir Mesas de rmiserver.Voto");
        opcoes.add("5-Alterar models.Eleicao");
        opcoes.add("6-Consultar Eleicoes Passadas");
        opcoes.add("7-Ver Eleitores em Votacao");
        opcoes.add("Para voltar atras nos seguinte submenus selecione qualquer letra");

        while(true){
            try {
                accao = false;
                System.out.println();
                for (String s : opcoes) {
                    System.out.println(s);
                }
                int n = Integer.parseInt(readInput.nextLine());
                int n3, n4;
                String input;
                Eleicao eleicao;
                Lista lista;
                User user;

                switch (n) {
                    case 1:
                        accao = true;
                        System.out.println("Insira Username: ");
                        String username = readInput.nextLine();
                        System.out.println("Insira Password: ");
                        String password = readInput.nextLine();
                        int counter = 1;
                        for (String depName: admin.Departamentos) {
                            System.out.println(counter+"- "+depName);
                            counter+=1;
                        }
                        boolean flagCorrect = false;
                        String departamento = "";
                        do {
                            n3 = Integer.parseInt(readInput.nextLine());
                            if(n3-1< admin.Departamentos.size()) {
                                departamento = admin.Departamentos.get(n3 - 1);
                                flagCorrect = true;
                            }
                            else{
                                System.out.println("Escolha um departamento listado!");
                            }
                        }while (!flagCorrect);
                        System.out.println("Insira Contacto: ");
                        String contacto = readInput.nextLine();
                        System.out.println("Insira Funcao: ");
                        System.out.println("1- Estudante");
                        System.out.println("2- Docente");
                        System.out.println("3- Funcionario");
                        n3 = Integer.parseInt(readInput.nextLine());
                        String funcao;
                        switch (n3) {
                            case 1:
                                funcao = "Estudante";
                                break;
                            case 2:
                                funcao = "Docente";
                                break;
                            case 3:
                                funcao = "Funcionario";
                                break;
                            default:
                                System.out.println("Por Default é Estudante");
                                funcao = "Estudante";
                        }
                        System.out.println("Insira Morada: ");
                        String morada = readInput.nextLine();
                        System.out.println("Insira Cartao de Cidadao: ");
                        String CC = readInput.nextLine();
                        System.out.println("Insira Data de Validade (dd/MM/yyyy): ");
                        boolean check = false;
                        do {
                            try {
                                String dataV = readInput.nextLine();
                                Date dataF = new SimpleDateFormat("dd/MM/yyyy").parse(dataV);
                                check = true;
                                user = new User(username, password, departamento, contacto, funcao, morada, CC, dataF);
                                input= votoObj.registo(user);
                                System.out.println(input);
                            } catch (ParseException e) {
                                System.out.println("Insira uma data valida no formato dd/MM/yyyy");
                            }
                        } while (!check);

                        break;

                    case 2:
                        accao = true;
                        System.out.println("Insira Data de Inicio (dd/MM/yyyy HH:MM)");
                        check = false;
                        Date dataEIF = null, dataEFF = null;
                        do {
                            try {
                                String dataEI = readInput.nextLine();
                                dataEIF = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataEI);
                                check = true;
                            } catch (ParseException e) {
                                System.out.println("Insira uma data valida no formato dd/MM/yyyy");
                            }
                        } while (!check);

                        check = false;
                        System.out.println("Insira Data Final (dd/MM/yyyy HH:MM): ");
                        do {
                            try {
                                String dataEF = readInput.nextLine();
                                dataEFF = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataEF);
                                check = true;

                                if (dataEFF.before(dataEIF)) {
                                    System.out.println("A data final devera ser depois da data inicial");
                                    check = false;
                                }
                            } catch (ParseException e) {
                                System.out.println("Insira uma data valida no formato dd/MM/yyyy");
                            }
                        } while (!check);

                        System.out.println("Insira Titulo: ");
                        String titulo = readInput.nextLine();
                        System.out.println("Insira Descricao: ");
                        String descricao = readInput.nextLine();
                        System.out.println("Insira Tipo de Eleicoes: ");
                        System.out.println("1- Estudante");
                        System.out.println("2- Docente");
                        System.out.println("3- Funcionario");
                        n3 = Integer.parseInt(readInput.nextLine());
                        switch (n3) {
                            case 1:
                                funcao = "Estudante";
                                break;
                            case 2:
                                funcao = "Docente";
                                break;
                            case 3:
                                funcao = "Funcionario";
                                break;
                            default:
                                System.out.println("Por Default é Estudante");
                                funcao = "Estudante";
                        }
                        eleicao = new Eleicao(dataEIF, dataEFF, titulo, descricao, funcao);
                        votoObj.criarEleicao(eleicao);
                        break;

                    case 3:
                        accao = true;
                        int n2;
                        do {
                            System.out.println("1-Adicionar models.Lista a models.Eleicao");
                            System.out.println("2-Remover models.Lista a models.Eleicao");
                            System.out.println("3-Criar models.Lista");
                            System.out.println("4-Remover models.Lista");
                            System.out.println("5-Adicionar Utilizador a models.Lista");
                            System.out.println("6-Remover Utilizador a models.Lista");
                            System.out.println("7-Ver Elementos da models.Lista");
                            n2 = Integer.parseInt(readInput.nextLine());
                        } while (n2 != 1 && n2 != 2 && n2 != 3 && n2 != 4 && n2!=5 && n2!=6 && n2!=7);

                        switch (n2) {
                            case 1:
                                System.out.println("Selecione models.Eleicao");
                                input = votoObj.listarEleicoes();
                                boolean flagRightInput = false;
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    do {
                                        n3 = Integer.parseInt(readInput.nextLine());
                                        Eleicao eleicao1 = votoObj.getEleicao(n3 - 1);
                                        if (eleicao1 != null) {
                                            input = votoObj.listarListas();
                                            System.out.println(input);
                                            boolean flagRightInput2=false;
                                            do {
                                                n4 = Integer.parseInt(readInput.nextLine());
                                                lista = votoObj.getLista(n4 - 1);
                                                if(lista!=null) {
                                                    votoObj.addListaEleicao(eleicao1, lista);
                                                    flagRightInput2=true;
                                                }
                                                else{
                                                    System.out.println("Selecione uma ocapListada");
                                                }
                                            }while (!flagRightInput2);
                                            flagRightInput = true;
                                        }
                                        else{
                                            System.out.println("Escolha uma opcao Listada!");
                                        }
                                    } while (!flagRightInput);
                                }
                                else{
                                    System.out.println("Nao ha eleicoes registadas!");
                                }
                                break;
                            case 2:
                                System.out.println("Selecione models.Eleicao");
                                input = votoObj.listarEleicoes();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    flagRightInput = false;
                                    do {
                                        n3 = Integer.parseInt(readInput.nextLine());
                                        eleicao = votoObj.getEleicao(n3 - 1);
                                        if(eleicao!=null) {
                                            input = votoObj.listarListasEleicao(n3 - 1);
                                            if (!input.equals("")) {
                                                System.out.println(input);
                                                n4 = Integer.parseInt(readInput.nextLine());
                                                votoObj.removeListaEleicao(eleicao, n4 - 1);
                                            } else {
                                                System.out.println("models.Eleicao nao tem listas inscritas");
                                            }
                                            flagRightInput=true;
                                        }
                                        else{
                                            System.out.println("Escolha uma opcao listada");
                                        }
                                    }while (!flagRightInput);
                                }
                                else{
                                    System.out.println("Nao ha eleicoes registadas!");
                                }
                                break;

                            case 3:
                                System.out.println("Indique nome da models.Lista");
                                input = readInput.nextLine();
                                System.out.println("Insira Tipo de Eleicoes: ");
                                System.out.println("1- Estudante");
                                System.out.println("2- Docente");
                                System.out.println("3- Funcionario");
                                n3 = Integer.parseInt(readInput.nextLine());
                                switch (n3) {
                                    case 1:
                                        funcao = "Estudante";
                                        break;
                                    case 2:
                                        funcao = "Docente";
                                        break;
                                    case 3:
                                        funcao = "Funcionario";
                                        break;
                                    default:
                                        System.out.println("Por Default é Estudante");
                                        funcao = "Estudante";
                                }
                                lista = new Lista(input,funcao);
                                votoObj.addLista(lista);
                                System.out.println("Adicione elementos a lista - Clique 0 quando quiser terminar");
                                input = votoObj.printUsers();
                                System.out.println(input);
                                do {
                                    if (n3 != 0) {
                                        n3 = Integer.parseInt(readInput.nextLine());
                                        user = votoObj.getUser(n3 - 1);
                                        if(user!=null) {
                                            input = votoObj.addUserList(user, lista);
                                            System.out.println(input);
                                        }
                                        else{
                                            System.out.println("Selecione um utilizador listado!");
                                        }
                                    }
                                } while (n3 != 0);

                                break;

                            case 4:
                                System.out.println("Selecione models.Lista");
                                input = votoObj.listarListas();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    flagRightInput=false;
                                    do {
                                        n3 = Integer.parseInt(readInput.nextLine());
                                        lista = votoObj.getLista(n3 - 1);
                                        if(lista!=null) {
                                            votoObj.removeLista(lista);
                                            flagRightInput=true;
                                        }
                                        else{
                                            System.out.println("Escolha uma opcao listada");
                                        }
                                    }while (!flagRightInput);
                                }
                                else{
                                    System.out.println("Nao ha listas registadas!");
                                }
                                break;
                            case 5:
                                System.out.println("Selecione models.Lista");
                                input = votoObj.listarListas();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    flagRightInput=false;
                                    do {
                                        n3 = Integer.parseInt(readInput.nextLine());
                                        lista = votoObj.getLista(n3 - 1);
                                        if(lista!=null) {
                                            input = votoObj.printUsers();
                                            System.out.println(input);
                                            flagRightInput=true;
                                            do {
                                                n3 = Integer.parseInt(readInput.nextLine());
                                                if (n3 != 0) {
                                                    user = votoObj.getUser(n3 - 1);
                                                    if (user != null) {
                                                        votoObj.addUserList(user, lista);
                                                    } else {
                                                        System.out.println("Selecione um utilizador listado!");
                                                    }
                                                }
                                            } while (n3 != 0);
                                        }
                                        else{
                                            System.out.println("Seleciona uma opcao listada");
                                        }
                                    }while (!flagRightInput);
                                }
                                else{
                                    System.out.println("Nao ha listas registadas!");
                                }
                                break;

                            case 6:
                                System.out.println("Selecione models.Lista");
                                input = votoObj.listarListas();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    flagRightInput = false;
                                    do {
                                        n3 = Integer.parseInt(readInput.nextLine());
                                        lista = votoObj.getLista(n3 - 1);
                                        if(lista!=null) {
                                            input = votoObj.printUsers();
                                            System.out.println(input);
                                            flagRightInput=true;
                                            do {
                                                n3 = Integer.parseInt(readInput.nextLine());
                                                if (n3 != 0) {
                                                    user = votoObj.getUser(n3 - 1);
                                                    votoObj.removeUserList(lista, user);
                                                }
                                            } while (n3 != 0);
                                        }
                                    }while (!flagRightInput);
                                }
                                else {
                                    System.out.println("Nao ha listas registadas!");
                                }
                                break;

                            case 7:
                                System.out.println("Selecione models.Lista");
                                input = votoObj.listarListas();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    flagRightInput=false;
                                    do {
                                        n3 = Integer.parseInt(readInput.nextLine());
                                        lista = votoObj.getLista(n3 - 1);
                                        if(lista!=null) {
                                            input = votoObj.listarLista(lista);
                                            System.out.println(input);
                                            flagRightInput=true;
                                        }
                                        else{
                                            System.out.println("Selecione uma opcao Listada!");
                                        }
                                    }while (!flagRightInput);
                                }
                                else {
                                    System.out.println("Nao ha listas registadas!");
                                }
                        }

                        break;

                    case 4:
                        accao = true;
                        System.out.println("Selecione models.Eleicao");
                        input = votoObj.listarEleicoes();
                        if(!input.equals("")) {
                            System.out.println(input);
                            flagCorrect=false;
                            do {
                                n3 = Integer.parseInt(readInput.nextLine());
                                eleicao = votoObj.getEleicao(n3 - 1);
                                if(eleicao!=null){
                                    flagCorrect=true;
                                }
                                else{
                                    System.out.println("Insira uma opcao listada!");
                                }
                            }while (!flagCorrect);
                            do {
                                System.out.println("1- Adicionar Mesa \n2- Remover mesa");
                                n3 = Integer.parseInt(readInput.nextLine());
                            }while (n3!=1 && n3!=2);
                            if(n3==1) {
                                System.out.println("Selecione Mesa a adicionar");
                                input = votoObj.listaMaquina();
                                if (!input.equals("")) {
                                    System.out.println(input);
                                    boolean flagCorrectInput = false;
                                    do {
                                        n3 = Integer.parseInt(readInput.nextLine());
                                        DepMesa mesa = votoObj.getMesa(n3 - 1);
                                        if(mesa!=null) {
                                            input = votoObj.addMesaEleicao(mesa, eleicao);
                                            System.out.println(input);
                                            flagCorrectInput = true;
                                        }
                                        else{
                                            System.out.println("Selecione uma mesa correta!");
                                        }
                                    }while (!flagCorrectInput);
                                } else {
                                    System.out.println("Nao ha maquinas no sistema");
                                }
                            }
                            else{
                                if(eleicao.maquinasVotacao.size()>0) {
                                    counter = 1;
                                    for (DepMesa mesa : eleicao.maquinasVotacao) {
                                        System.out.println(counter + "- " + mesa.departamento + " " + mesa.id);
                                        counter +=1;
                                    }
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    votoObj.removeMesaEleicao(n3-1, eleicao);
                                }
                                else{
                                    System.out.println("Nao ha maquinas para remover");
                                }

                            }
                        }
                        else {
                            System.out.println("Nao ha eleicoes a decorrer");
                        }
                        break;

                    case 5:
                        accao = true;
                        System.out.println("Selecione models.Eleicao");
                        input = votoObj.listarEleicoes();
                        if(!input.equals("")) {
                            System.out.println(input);
                            boolean flagCorrectOption = false;
                            do {
                                n3 = Integer.parseInt(readInput.nextLine());
                                eleicao = votoObj.getEleicao(n3 - 1);
                                if(eleicao!=null){
                                    flagCorrectOption=true;
                                }
                                else{
                                    System.out.println("Selecione uma eleicao listada!");
                                }
                            }while (!flagCorrectOption);
                            do {
                                System.out.println("1-Editar Titulo da models.Eleicao");
                                System.out.println("2-Editar Descricao da models.Eleicao");
                                System.out.println("3-Editar Datas");
                                System.out.println("4-Editar Tipo de models.Eleicao");
                                n2 = Integer.parseInt(readInput.nextLine());
                            } while (n2 != 1 && n2 != 2 && n2 != 3 && n2 != 4);

                            switch (n2) {
                                case 1:
                                    System.out.println("Indique novo titulo");
                                    input = readInput.nextLine();
                                    votoObj.setTitulo(input, eleicao);
                                    break;
                                case 2:
                                    System.out.println("Indique nova Descricao");
                                    input = readInput.nextLine();
                                    votoObj.setDescricao(input, eleicao);
                                    break;
                                case 3:
                                    System.out.println("Indique nova Data Inicio (dd/MM/yyyy HH:MM):");
                                    check = false;
                                    dataEFF=null;
                                    dataEIF=null;
                                    do {
                                        try {
                                            String dataEI = readInput.nextLine();
                                            dataEIF = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataEI);
                                            check = true;
                                        } catch (ParseException e) {
                                            System.out.println("Insira uma data valida no formato dd/MM/yyyy HH:mm");
                                        }
                                    } while (!check);

                                    check = false;
                                    System.out.println("Insira Data Final (dd/MM/yyyy HH:MM):");
                                    do {
                                        try {
                                            String dataEF = readInput.nextLine();
                                            dataEFF = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataEF);
                                            check = true;

                                            if (dataEFF.before(dataEIF)) {
                                                System.out.println("A data final devera ser depois da data inicial");
                                                check = false;
                                            }
                                        } catch (ParseException e) {
                                            System.out.println("Insira uma data valida no formato dd/MM/yyyy HH:mm");
                                        }
                                    } while (!check);
                                    votoObj.setDatas(dataEIF, dataEFF, eleicao);
                                    break;
                                case 4:
                                    System.out.println("Indique novo Tipo de Eleicoes: ");  //se é eleições de estudantes/docente/funcionarios
                                    System.out.println("1- Estudante");
                                    System.out.println("2- Docente");
                                    System.out.println("3- Funcionario");
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    switch (n3) {
                                        case 1:
                                            funcao = "Estudante";
                                            break;
                                        case 2:
                                            funcao = "Docente";
                                            break;
                                        case 3:
                                            funcao = "Funcionario";
                                            break;
                                        default:
                                            System.out.println("Por Default é Estudante");
                                            funcao = "Estudante";
                                    }
                                    votoObj.setTipo(funcao, eleicao);
                                    break;
                            }
                        }
                        else{
                            System.out.println("Nao ha eleicoes registadas!");
                        }
                        break;

                    case 6:
                        accao = true;
                        System.out.println("Escolha uma models.Eleicao");
                        input = votoObj.getEleicoesVelhas();
                        if(!input.equals("")) {
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            input = votoObj.getInfoEleicaoVelha(n3 - 1);
                            System.out.println(input);
                        }
                        else{
                            System.out.println("Nao ha eleicoes registadas!");
                        }
                        break;

                    case 7:
                        accao = true;
                        System.out.println("Escolha uma models.Eleicao");
                        input = votoObj.listarEleicoes();
                        if(!input.equals("")) {
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            n3 = votoObj.numVotantes(n3-1);
                            if(n3==-1){
                                System.out.println("Selecione uma eleicao listada");
                            }
                            else {
                                System.out.println(n3);
                            }
                        }
                        else{
                            System.out.println("Nao ha eleicoes registadas!");
                        }
                        break;

                    case 8: //remover isto depois, so para debug!
                        votoObj.writeFile();
                        break;

                    case 9: //debug
                        input = votoObj.printUsers();
                        System.out.println(input);
                        break;

                    default:
                        System.out.println("Escolha uma das opcoes");
                        break;
                }
            } catch (ConnectException e){
                Date now = new Date();
                Date after = new Date();
                after.setTime(now.getTime()+30000);
                boolean flag = false;
                while (now.before(after)) {
                    now = new Date();
                    try {
                        votoObj = (Voto) LocateRegistry.getRegistry(admin.serverAddress,admin.porto).lookup("votacao");
                        flag = true;
                        break;
                    }catch (ConnectException e1){
                        ;
                    }
                }
                if(!flag){
                    System.out.println("Erro nos servidores RMI!");
                    System.exit(0);
                }
                else{
                    if(accao){
                        System.out.println("Houve um problema com o ultimo comando, volte a efetua-lo");
                    }
                }
            } catch (NumberFormatException e){
                System.out.println("Insira um numero e repita o procedimento");
            }
            catch (NoSuchElementException e){

            }

        }
    }
}
