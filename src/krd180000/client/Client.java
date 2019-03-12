package krd180000.client;

import krd180000.common.PropertyReader;
import krd180000.model.Address;
import krd180000.model.FileOpResult;
import krd180000.model.FileOperation;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class Client implements Runnable{
    private MessageHandler messageHandler;
    private int totalClients;
    public static final int TOTAL_SERVERS = 3;
    private int port;
    public final int clientId;
    private List<String> files;
    private MutExRunnerStore mutExRunnerStore;
    private SocketReceiver receiver;
    private Address[] serverIps;
    private Random random;

    public Client(int clientId,int totalClients,Address[] clientIps,Address[] serverIps) throws IOException {
        this.clientId = clientId;
        this.totalClients = totalClients;
        this.messageHandler = new MessageHandler(clientIps);
        this.port = clientIps[clientId].getPort();
        this.mutExRunnerStore = new MutExRunnerStore(clientId,totalClients,messageHandler);
        this.receiver = new SocketReceiver(mutExRunnerStore,port,messageHandler);
        receiver.start();
        this.serverIps = serverIps;
        random = new Random();
    }

    @Override
    public void run(){
        Scanner in = new Scanner(System.in);
        ServerOpHandler opHandler = new ServerOpHandler(serverIps);
        try {
            FileOpResult opResult = opHandler.handle(FileOperation.Enquiry,1,clientId);
            files = opResult.getFiles();
            mutExRunnerStore.init(files);
        } catch (IOException|ClassNotFoundException|RuntimeException e) {
            System.out.println("Init failed!");
            receiver.interrupt();
            e.printStackTrace();
        }
        System.out.println("Enter: ");
        System.out.println("\t Enquiry files 0");
        System.out.println("\t Read file 1");
        System.out.println("\t Write file 2");
        System.out.println("\t Exit anything else");
        while (true) {
            String file = files.get(random.nextInt(files.size()));
            int op = in.nextInt();
            if(op>2){
                break;
            }
            try {
                if(op==0){
                    try {
                        FileOpResult opResult = opHandler.handle(FileOperation.Enquiry,1,clientId);
                        System.out.println("Result = " + opResult);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                mutExRunnerStore.getMutexRunnerFor(file).execute(() -> {
                    FileOperation operation = FileOperation.Write;
                    if(op==1){
                        operation = FileOperation.Read;
                    }
                    FileOpResult opResult = null;
                    try {
                        int toServer = 1 + random.nextInt(3);
                        opResult = opHandler.handle(operation,toServer,clientId);
                        System.out.println("Result = " + opResult);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
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

