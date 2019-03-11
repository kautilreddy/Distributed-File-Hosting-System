package krd180000.client;

import krd180000.common.PropertyReader;
import krd180000.model.Address;
import krd180000.model.FileOpResult;
import krd180000.model.FileOperation;

import java.io.IOException;
import java.net.*;
import java.util.Properties;
import java.util.Scanner;

public class Client implements Runnable{
    private MessageHandler messageHandler;
    private int totalClients;
    private static final int TOTAL_SERVERS = 3;
    private int port;
    private int clientId;
    private MutExRunner mutExRunner;
    private SocketReceiver receiver;
    private Address[] serverIps;

    public Client(int clientId,int totalClients,Address[] clientIps,Address[] serverIps) throws IOException {
        this.clientId = clientId;
        this.totalClients = totalClients;
        this.messageHandler = new MessageHandler(clientIps);
        this.port = clientIps[clientId].getPort();
        this.mutExRunner = new MutExRunner(clientId,totalClients,port,messageHandler);
        this.receiver = new SocketReceiver(mutExRunner,port,messageHandler);
        this.serverIps = serverIps;
        receiver.start();
    }

    @Override
    public void run(){
        Scanner in = new Scanner(System.in);
        ServerOpHandler opHandler = new ServerOpHandler(serverIps);
        while (true) {
            int op = in.nextInt();
            try {
                mutExRunner.execute(() -> {
                    FileOpResult opResult = opHandler.handle(FileOperation.Read,1);

                });
//                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Client has started");
        System.out.println("Address is : "+getMyIp());
        Properties properties = PropertyReader.read();
        int totalClients = Integer.parseInt(properties.getProperty("totalClients"));
        int currentClientId = Integer.parseInt(properties.getProperty("currentClientId"));
        Address[] clientIps = getIPs(properties,"client",totalClients);
        Address[] serverIps = getIPs(properties,"server",TOTAL_SERVERS);
        Client client = new Client(currentClientId,totalClients,clientIps,serverIps);
        new Thread(client).start();
    }

    public static String getMyIp(){
        String ip;
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        }catch (SocketException | UnknownHostException e){
            throw new RuntimeException(e);
        }
        return ip;
    }
    private static Address[] getIPs(Properties properties,String keyPrefix,int totalCount){
        Address[] ips = new Address[totalCount+1];
        for (int i = 1; i <= totalCount; i++) {
            int port = Integer.parseInt(properties.getProperty(keyPrefix+i+"_port"));
            Address address = new Address(properties.getProperty(keyPrefix+i+"_ip"), port);
            ips[i] = address;
        }
        return ips;
    }
}

