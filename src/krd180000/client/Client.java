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
    public final int clientId;
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
        in.nextInt();
        while (true) {
            int op = in.nextInt();
            if(op>2){
                break;
            }
            try {
                mutExRunner.execute(() -> {
                    FileOperation operation = FileOperation.Enquiry;
                    if(op==1){
                        operation = FileOperation.Read;
                    }else if(op==2){
                        operation = FileOperation.Write;
                    }
                    FileOpResult opResult = null;
                    try {
                        opResult = opHandler.handle(operation,1,clientId);
                        System.out.println("opResult = " + opResult);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
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
        Properties properties = PropertyReader.read("src/resources/client.properties");
        int totalClients = Integer.parseInt(properties.getProperty("totalClients"));
        int currentClientId = Integer.parseInt(args[0]);
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

