import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminConsole extends UnicastRemoteObject implements Notifications {
    int porto;
    String serverAddress;
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
        System.out.println("Eleicao "+nome+" acabou.\nResultados");
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
        Voto votoObj = (Voto) LocateRegistry.getRegistry(admin.serverAddress,admin.porto).lookup("votacao");
        ArrayList<String> opcoes =  new ArrayList<>();

        votoObj.subscribeAdmin((Notifications) admin);


        boolean accao = false;

        opcoes.add("Escolher Opcao");
        opcoes.add("1-Registar Pessoa");
        opcoes.add("2-Criar Eleicao");
        opcoes.add("3-Gerir Lista de Candidatos");
        opcoes.add("4-Gerir Mesas de Voto");
        opcoes.add("5-Alterar Eleicao");
        opcoes.add("6-Consultar Eleicoes Passadas");
        opcoes.add("7- Ver Eleitores em Votacao");

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
                        n3 = Integer.parseInt(readInput.nextLine());
                        String departamento = admin.Departamentos.get(n3-1);
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
                                votoObj.registo(user);
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
                            System.out.println("1-Adicionar Lista a Eleicao");
                            System.out.println("2-Remover Lista a Eleicao");
                            System.out.println("3-Criar Lista");
                            System.out.println("4-Remover Lista");
                            System.out.println("5-Adicionar Utilizador a Lista");
                            System.out.println("6-Remover Utilizador a Lista");
                            n2 = Integer.parseInt(readInput.nextLine());
                        } while (n2 != 1 && n2 != 2 && n2 != 3 && n2 != 4);

                        switch (n2) {
                            case 1:
                                System.out.println("Selecione Eleicao");
                                input = votoObj.listarEleicoes();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    Eleicao eleicao1 = votoObj.getEleicao(n3 - 1);
                                    input = votoObj.listarListas();
                                    System.out.println(input);
                                    n4 = Integer.parseInt(readInput.nextLine());
                                    lista = votoObj.getLista(n4 - 1);
                                    votoObj.addListaEleicao(eleicao1, lista);
                                }
                                else{
                                    System.out.println("Nao ha eleicoes registadas!");
                                }
                                break;
                            case 2:
                                System.out.println("Selecione Eleicao");
                                input = votoObj.listarEleicoes();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    eleicao = votoObj.getEleicao(n3 - 1);
                                    input = votoObj.listarListasEleicao(n3-1);
                                    if(!input.equals("")) {
                                        System.out.println(input);
                                        n4 = Integer.parseInt(readInput.nextLine());
                                        votoObj.removeListaEleicao(eleicao, n4-1);
                                    }
                                    else{
                                        System.out.println("Eleicao nao tem listas inscritas");
                                    }
                                }
                                else{
                                    System.out.println("Nao ha eleicoes registadas!");
                                }
                                break;

                            case 3:
                                System.out.println("Indique nome da Lista");
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
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    if (n3 != 0) {
                                        user = votoObj.getUser(n3 - 1);
                                        input=votoObj.addUserList(user, lista);
                                        System.out.println(input);
                                    }
                                } while (n3 != 0);

                                break;

                            case 4:
                                System.out.println("Selecione Lista");
                                input = votoObj.listarListas();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    lista = votoObj.getLista(n3 - 1);
                                    votoObj.removeLista(lista);
                                }
                                else{
                                    System.out.println("Nao ha listas registadas!");
                                }
                                break;
                            case 5:
                                System.out.println("Selecione Lista");
                                input = votoObj.listarListas();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    lista = votoObj.getLista(n3 - 1);
                                    input = votoObj.printUsers();
                                    System.out.println(input);
                                    do {
                                        n3 = Integer.parseInt(readInput.nextLine());
                                        if (n3 != 0) {
                                            user = votoObj.getUser(n3 - 1);
                                            votoObj.addUserList(user, lista);
                                        }
                                    } while (n3 != 0);
                                }
                                else{
                                    System.out.println("Nao ha listas registadas!");
                                }
                                break;

                            case 6:
                                System.out.println("Selecione Lista");
                                input = votoObj.listarListas();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    lista = votoObj.getLista(n3 - 1);
                                    input = votoObj.printUsers();
                                    System.out.println(input);
                                    do {
                                        n3 = Integer.parseInt(readInput.nextLine());
                                        if (n3 != 0) {
                                            user = votoObj.getUser(n3 - 1);
                                            votoObj.removeUserList(lista, user);
                                        }
                                    } while (n3 != 0);
                                }
                                else {
                                    System.out.println("Nao ha listas registadas!");
                                }
                                break;
                        }

                        break;

                    case 4:
                        accao = true;
                        System.out.println("Selecione Eleicao");
                        input = votoObj.listarEleicoes();
                        if(!input.equals("")) {
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            eleicao = votoObj.getEleicao(n3 - 1);
                            do {
                                System.out.println("1- Adicionar Mesa \n2- Remover mesa");
                                n3 = Integer.parseInt(readInput.nextLine());
                            }while (n3!=1 && n3!=2);
                            if(n3==1) {
                                System.out.println("Selecione Mesa a adicionar");
                                input = votoObj.listaMaquina();
                                if (!input.equals("")) {
                                    System.out.println(input);
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    DepMesa mesa = votoObj.getMesa(n3 - 1);
                                    input = votoObj.addMesaEleicao(mesa,eleicao);
                                    System.out.println(input);
                                } else {
                                    System.out.println("Nao ha maquinas no sistema");
                                }
                            }
                            else{
                                if(eleicao.maquinasVotacao.size()>0) {
                                    counter = 1;
                                    for (DepMesa mesa : eleicao.maquinasVotacao) {
                                        System.out.println(counter + "- " + mesa.departamento + " " + mesa.id);
                                    }
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    DepMesa mesa = votoObj.getMesa(n3-1);
                                    votoObj.removeMesaEleicao(mesa,eleicao);
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
                        System.out.println("Selecione Eleicao");
                        input = votoObj.listarEleicoes();
                        if(!input.equals("")) {
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            eleicao = votoObj.getEleicao(n3 - 1);
                            do {
                                System.out.println("1-Editar Titulo da Eleicao");
                                System.out.println("2-Editar Descricao da Eleicao");
                                System.out.println("3-Editar Datas");
                                System.out.println("4-Editar Tipo de Eleicao");
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
                        System.out.println("Escolha uma Eleicao");
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
                        System.out.println("Escolha uma Eleicao");
                        input = votoObj.listarEleicoes();
                        if(!input.equals("")) {
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            n3 = votoObj.numVotantes(n3-1);
                            System.out.println(n3);
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
