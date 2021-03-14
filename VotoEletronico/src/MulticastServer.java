import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private long SLEEP_TIME = 5000;

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
    }

    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null, socketR = null;
        long counter = 0;
        System.out.println(this.getName() + " running...");
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            socketR = new MulticastSocket(PORT); // server is gonna receive its own messages
            InetAddress groupR = InetAddress.getByName(MULTICAST_ADDRESS);
            socketR.joinGroup(groupR);
            while (true) {
                //falta fazer aqui a selecção do cliente

                //recebe mensagem do cliente
                byte[] bufferR = new byte[256];
                DatagramPacket packetR = new DatagramPacket(bufferR, bufferR.length);
                socketR.receive(packetR);

                System.out.println("Received packet from " + packetR.getAddress().getHostAddress() + ":" + packetR.getPort() + " with message:");
                String messageR = new String(packetR.getData(), 0, packetR.getLength());
                System.out.println(messageR);
                //tratamento da resposta
                if (true/*ver se a mensagem é de um cliente e não de si próprio. Tem de ser de um cliente selecionado tbm*/) {
                    String[] arrOfStr = messageR.split("[|;]");
                    if (arrOfStr[0] == "login") {
                        if (true/*check if person is present in storage through the rmi server*/) {
                            //resposta
                            String message = "type|status;logged|on;msg|Welcome to eVoting\n";
                            byte[] buffer = message.getBytes();

                            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        }
                    } else {
                        //send message u are not logged in
                    }

                } else {
                    //sum
                }

                try {
                    sleep((long) (Math.random() * SLEEP_TIME));
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            socketR.close();
        }
    }
}
