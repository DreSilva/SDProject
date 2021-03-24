import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class AdminConsole extends UnicastRemoteObject implements Notifications {

    /**
     * Cria objeto da consola para passar ao RMI Server
     */
    public AdminConsole() throws RemoteException {
        super();
    }

    /**
     * @inheritDoc
     */
    public void estadoMesa() throws  java.rmi.RemoteException{
        System.out.println("Idk");
    }

    /**
     * @inheritDoc
     */
    public void fimEleicao(String nome,String votos) throws  java.rmi.RemoteException{
        System.out.println("Eleicao "+nome+" acabou.\nResultados");
        System.out.println(votos);
    }

    /**
     * @inheritDoc
     */
    public void novoEleitor() throws  java.rmi.RemoteException{
        System.out.println("Novo Eleitor");
    }

    /**
     * Esta função vai permitir ao admin fazer todas as ações necessárias para por as votações a funcionar. Permite
     * criar e remover eleições,utilizadores e listas.
     * @throws RemoteException excepção que pode ocorrer na execução de uma remote call
     * @throws NotBoundException Acontece caso o o objeto a procurar no registo nao exista
     */
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Scanner readInput = new Scanner(System.in);
        AdminConsole admin = new AdminConsole();
        Voto votoObj = (Voto) LocateRegistry.getRegistry(7000).lookup("votacao");
        ArrayList<String> opcoes =  new ArrayList<>();
        votoObj.subscribeAdmin((Notifications) admin);
        opcoes.add("Escolher Opcao");
        opcoes.add("1-Registar Pessoa");
        opcoes.add("2-Criar Eleicao");
        opcoes.add("3-Gerir Lista de Candidatos");
        opcoes.add("4-Gerir Mesas de Voto");
        opcoes.add("5-Alterar Eleicao");
        opcoes.add("6-Consultar Eleicoes Passadas");

        while(true){
            try {
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
                        System.out.println("Insira Username: ");
                        String username = readInput.nextLine();
                        System.out.println("Insira Password: ");
                        String password = readInput.nextLine();
                        System.out.println("Insira Departamento: ");
                        System.out.println("1- DARQ");
                        System.out.println("2- DCT");
                        System.out.println("3- DCV");
                        System.out.println("4- DEI");
                        System.out.println("5- DEEC");
                        System.out.println("6- DEM");
                        System.out.println("7- DEQ");
                        System.out.println("8- DF");
                        System.out.println("9- DEC");
                        System.out.println("10- DM");
                        System.out.println("11- DQ");
                        System.out.println("12- FLUC");
                        System.out.println("13- FDUC");
                        System.out.println("14- FCDEF");
                        System.out.println("15- FPCE");
                        System.out.println("16- FEUC");
                        System.out.println("17- FFUC");
                        System.out.println("17- FMUC");
                        n3 = Integer.parseInt(readInput.nextLine());
                        String departamento;
                        switch (n3) {
                            case 1:
                                departamento = "DARQ";
                                break;
                            case 2:
                                departamento = "DCT";
                                break;
                            case 3:
                                departamento = "DCV";
                                break;
                            case 4:
                                departamento = "DEI";
                                break;
                            case 5:
                                departamento = "DEEC";
                                break;
                            case 6:
                                departamento = "DEM";
                                break;
                            case 7:
                                departamento = "DEQ";
                                break;
                            case 8:
                                departamento = "DF";
                                break;
                            case 9:
                                departamento = "DEC";
                                break;
                            case 10:
                                departamento = "DM";
                                break;
                            case 11:
                                departamento = "DQ";
                                break;
                            case 12:
                                departamento = "FLUC";
                                break;
                            case 13:
                                departamento = "FDUC";
                                break;
                            case 14:
                                departamento = "FCDEF";
                                break;
                            case 15:
                                departamento = "FPCE";
                                break;
                            case 16:
                                departamento = "FEUC";
                                break;
                            case 17:
                                departamento = "FFUC";
                                break;
                            case 18:
                                departamento = "FMUC";
                                break;
                            default:
                                departamento = "DEI";
                                System.out.println("Por default é DEI");
                        }

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
                                    System.out.println("Não ha eleicoes registadas!");
                                }
                                break;
                            case 2:
                                System.out.println("Selecione Eleicao");
                                input = votoObj.listarEleicoes();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    eleicao = votoObj.getEleicao(n3 - 1);
                                    input = votoObj.listarListas();
                                    System.out.println(input);
                                    n4 = Integer.parseInt(readInput.nextLine());
                                    lista = votoObj.getLista(n4 - 1);
                                    votoObj.removeListaEleicao(eleicao, lista);
                                }
                                else{
                                    System.out.println("Não ha eleicoes registadas!");
                                }
                                break;

                            case 3:
                                System.out.println("Indique nome da Lista");
                                input=votoObj.listarEleicoes();
                                if(!input.equals("")) {
                                    System.out.println(input);
                                    input = readInput.nextLine();
                                    lista = new Lista(input);
                                    votoObj.addLista(lista);
                                    System.out.println("Adicione elementos á lista - Clique 0 quando quiser terminar");
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
                                    System.out.println("Não ha listas registadas!");
                                }
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
                        System.out.println("Selecione Eleicao");
                        input = votoObj.listarEleicoes();
                        if(!input.equals("")) {
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            eleicao = votoObj.getEleicao(n3 - 1);
                            do {
                                System.out.println("1- Adicionar Mesa \n 2- Remover mesa");
                                n3 = Integer.parseInt(readInput.nextLine());
                            }while (n3!=1 && n3!=2);
                            if(n3==1) {
                                System.out.println("Selecione Mesa a adicionar");
                                input = votoObj.listaMaquina();
                                if (!input.equals("")) {
                                    System.out.println(input);
                                    n3 = Integer.parseInt(readInput.nextLine());
                                    DepMesa mesa = votoObj.getMesa(n3 - 1);
                                    votoObj.addMesaEleicao(mesa,eleicao);
                                } else {
                                    System.out.println("Não há maquinas no sistema");
                                }
                            }
                            else{
                                if(eleicao.maquinasVotacao.size()>0) {
                                    int counter = 1;
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
                            System.out.println("Não há eleições a decorrer");
                        }
                        break;

                    case 5:
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
                            System.out.println("Não ha eleicoes registadas!");
                        }
                        break;

                    case 6:
                        System.out.println("Escolha uma Eleicao");
                        input = votoObj.getEleicoesVelhas();
                        if(!input.equals("")) {
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            input = votoObj.getInfoEleicaoVelha(n3 - 1);
                            System.out.println(input);
                        }
                        else{
                            System.out.println("Não ha eleicoes registadas!");
                        }
                        break;

                    case 7: //remover isto depois, so para debug!
                        votoObj.writeFile();
                        break;

                    default:
                        System.out.println("Escolha uma das opcoes");
                        break;
                }
            } catch (ConnectException e){
                votoObj = (Voto) LocateRegistry.getRegistry(7000).lookup("votacao");
            }

        }
    }
}
