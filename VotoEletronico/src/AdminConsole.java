import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class AdminConsole {
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, ParseException {
        Voto votoObj = (Voto) LocateRegistry.getRegistry(7000).lookup("votacao");
        Scanner readInput = new Scanner(System.in);
        ArrayList<String> opcoes =  new ArrayList<>();
        opcoes.add("Escolher Opcao");
        opcoes.add("1-Registar Pessoa");
        opcoes.add("2-Criar Eleicao");
        opcoes.add("3-Gerir Lista de Candidatos");
        opcoes.add("4-Gerir Mesas de Voto");
        opcoes.add("5-Alterar Eleicao");
        opcoes.add("6-Consultar Eleicoes Passadas");
        // notificações em relacao a mesa de voto ? Q notificações, sobre oq?
        // o admin escolhe q eleicao quer receber notificação ou recebe de todas ao mesmo tempo?
        // termino da eleicao e´automatico? assim que acabar é suposto notificar a admin console?
        while(true){
            for (String s: opcoes) {
                System.out.println(s);
            }
            int n = Integer.parseInt(readInput.nextLine());
            int n3,n4;
            String input;
            Eleicao eleicao;
            Lista lista;
            User user;

            switch (n){
                case 1:
                    System.out.println("Insira Username: ");
                    String username = readInput.nextLine();
                    System.out.println("Insira Password: ");
                    String password = readInput.nextLine();
                    System.out.println("Insira Departamento: "); // so aceita departamentos do dei?
                    String departamento =  readInput.nextLine();
                    System.out.println("Insira Faculdade: "); // so aceita faculdades do dei?
                    String faculdade = readInput.nextLine();
                    System.out.println("Insira Contacto: ");
                    String contacto = readInput.nextLine();
                    System.out.println("Insira Funcao: "); // só docentes/estudantes/funcionario?
                    String funcao = readInput.nextLine();
                    System.out.println("Insira Morada: ");
                    String morada = readInput.nextLine();
                    System.out.println("Insira Cartao de Cidadao: ");
                    String CC = readInput.nextLine();
                    System.out.println("Insira Data de Validade: ");
                    String dataV = readInput.nextLine();
                    Date dataF = new SimpleDateFormat("dd/MM/yyyy").parse(dataV) ;
                    user = new User(username,password,departamento,faculdade,contacto,funcao,morada,CC,dataF);
                    votoObj.registo(user);
                    break;

                case 2:
                    System.out.println("Insira Data de Inicio: ");
                    String dataEI = readInput.nextLine();
                    Date dataEIF = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataEI);
                    System.out.println("Insira Data Final: ");
                    String dataEF = readInput.nextLine();
                    Date dataEFF = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataEF);
                    System.out.println("Insira Titulo: ");
                    String titulo = readInput.nextLine();
                    System.out.println("Insira Descricao: ");
                    String descricao = readInput.nextLine();
                    System.out.println("Insira Nucleo: "); // para q nucleo se vota
                    String nucleo = readInput.nextLine();
                    System.out.println("Insira Tipo de Eleicoes: ");  //se é eleições de estudantes/docente/funcionarios
                    String tipo = readInput.nextLine();
                    eleicao = new Eleicao(dataEIF,dataEFF,titulo,descricao,nucleo,tipo);
                    votoObj.criarEleicao(eleicao);
                    break;

                case 3:
                    int n2 ;
                    do {
                        System.out.println("1-Adicionar Lista a Eleicao");
                        System.out.println("2-Remover Lista a Eleicao");
                        System.out.println("3-Criar Lista");
                        System.out.println("4-Remover Lista");
                        System.out.println("5-Adicionar Utilizador a Lista");
                        System.out.println("6-Remover Utilizador a Lista");
                        n2 = Integer.parseInt(readInput.nextLine());
                    }while (n2!=1 && n2!=2 && n2!=3 && n2!=4);

                    switch (n2){
                        case 1:
                            System.out.println("Selecione Eleicao");
                            input= votoObj.listarEleicoes();
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            Eleicao eleicao1 = votoObj.getEleicao(n3-1);
                            input = votoObj.listarListas();
                            System.out.println(input);
                            n4 = Integer.parseInt(readInput.nextLine());
                            lista = votoObj.getLista(n4-1);
                            votoObj.addListaEleicao(eleicao1,lista);
                            break;
                        case 2:
                            System.out.println("Selecione Eleicao");
                            input = votoObj.listarEleicoes();
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            eleicao = votoObj.getEleicao(n3-1);
                            input= votoObj.listarListas();
                            System.out.println(input);
                            n4 = Integer.parseInt(readInput.nextLine());
                            lista = votoObj.getLista(n4-1);
                            votoObj.removeListaEleicao(eleicao,lista);
                            break;

                        case 3:
                            System.out.println("Indique nome da Lista");
                            votoObj.listarEleicoes();
                            input = readInput.nextLine();
                            lista = new Lista(input);
                            votoObj.addLista(lista);
                            System.out.println("Adicione elementos á lista - Clique 0 quando quiser terminar");
                            input= votoObj.printUsers();
                            System.out.println(input);
                            do{
                                n3 = Integer.parseInt(readInput.nextLine());
                                if(n3!=0) {
                                    user = votoObj.getUser(n3 - 1);
                                    votoObj.addUserList(user, lista);
                                }
                            }while (n3!=0);
                            break;

                        case 4:
                            System.out.println("Selecione Lista");
                            input = votoObj.listarListas();
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            lista = votoObj.getLista(n3-1);
                            votoObj.removeLista(lista);
                            break;
                        case 5:
                            System.out.println("Selecione Lista");
                            input = votoObj.listarListas();
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            lista = votoObj.getLista(n3-1);
                            input= votoObj.printUsers();
                            System.out.println(input);
                            do{
                                n3 = Integer.parseInt(readInput.nextLine());
                                if(n3!=0) {
                                    user = votoObj.getUser(n3 - 1);
                                    votoObj.addUserList(user, lista);
                                }
                            }while (n3!=0);
                            break;

                        case 6:
                            System.out.println("Selecione Lista");
                            input = votoObj.listarListas();
                            System.out.println(input);
                            n3 = Integer.parseInt(readInput.nextLine());
                            lista = votoObj.getLista(n3-1);
                            input= votoObj.printUsers();
                            System.out.println(input);
                            do{
                                n3 = Integer.parseInt(readInput.nextLine());
                                if(n3!=0) {
                                    user = votoObj.getUser(n3 - 1);
                                    votoObj.removeUserList(lista,user);
                                }
                            }while (n3!=0);
                            break;
                    }

                    break;

                case 4:
                    //fica por enqnt como debug pq eu n sei como fazer isto yet
                    input= votoObj.printUsers();
                    System.out.println(input);
                    break;

                case 5:
                    System.out.println("Selecione Eleicao");
                    input = votoObj.listarEleicoes();
                    System.out.println(input);
                    n3 = Integer.parseInt(readInput.nextLine());
                    eleicao = votoObj.getEleicao(n3-1);
                    do {
                        System.out.println("1-Editar Titulo da Eleicao");
                        System.out.println("2-Editar Descricao da Eleicao");
                        System.out.println("3-Editar Datas");
                        System.out.println("3-Editar Tipo de Eleicao");
                        n2 = Integer.parseInt(readInput.nextLine());
                    }while (n2!=1 && n2!=2 && n2!=3 && n2!=4);

                    switch (n2){
                        case 1:
                            System.out.println("Indique novo titulo");
                            input = readInput.nextLine();
                            eleicao.setTitulo(input);
                            break;
                        case 2:
                            System.out.println("Indique nova Descricao");
                            input = readInput.nextLine();
                            eleicao.setDescicao(input);
                            break;
                        case 3:
                            System.out.println("Indique nova Data Inicio");
                            input = readInput.nextLine();
                            eleicao.setTitulo(input);
                            dataEIF = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(input);
                            System.out.println("Insira nova Data Final");
                            dataEF = readInput.nextLine();
                            dataEFF = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataEF);
                            eleicao.setInicio(dataEIF);
                            eleicao.setFim(dataEFF);
                            break;
                        case 4:
                            System.out.println("Indique novo Tipo");
                            input = readInput.nextLine();
                            eleicao.setTipo(input);
                            break;
                    }
                    break;

                case 6:
                    System.out.println("Escolha uma Eleicao");
                    input = votoObj.getEleicoesVelhas();
                    System.out.println(input);
                    n3 = Integer.parseInt(readInput.nextLine());
                    input = votoObj.getInfoEleicaoVelha(n3-1);
                    System.out.println(input);
                    break;

                case 7:
                    votoObj.writeFile();
                    return;

                default:
                    System.out.println("Escolha uma das opções");
                    break;
            }

        }
    }
}
