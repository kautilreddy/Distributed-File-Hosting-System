package krd180000.client;

import krd180000.model.Address;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public Client(int clientId,int totalClients,Address[] clientIps) throws IOException {
        this.clientId = clientId;
        this.totalClients = totalClients;
        this.messageHandler = new MessageHandler(clientIps);
        this.port = clientIps[clientId].getPort();
        this.mutExRunner = new MutExRunner(clientId,totalClients,port,messageHandler);
        this.receiver = new SocketReceiver(mutExRunner,port,messageHandler);
        receiver.start();
    }

    @Override
    public void run(){
//        if(clientId!=1){
            Scanner in = new Scanner(System.in);
            String is = in.nextLine();
//            return;
        //}
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            try {
                mutExRunner.execute(()->{
                    System.out.println(clientId+" is in critical section @"+System.currentTimeMillis());
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
        Properties properties = readProperties();
        int totalClients = Integer.parseInt(properties.getProperty("totalClients"));
        int currentClientId = Integer.parseInt(properties.getProperty("currentClientId"));
        Address[] clientIps = getIPs(properties,"client",totalClients,currentClientId);
        Client client = new Client(currentClientId,totalClients,clientIps);
        new Thread(client).start();
    }
    public static Properties readProperties(){
        Properties prop = new Properties();
        Scanner in = new Scanner(System.in);
        InputStream is = null;
        String fileName = in.nextLine();
        try {
            is = new FileInputStream(fileName);
            prop.load(is);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return prop;
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
    private static Address[] getIPs(Properties properties,String keyPrefix,int totalCount,int currentId){
        Address[] ips = new Address[totalCount+1];
        for (int i = 1; i <= totalCount; i++) {
            int port = Integer.parseInt(properties.getProperty(keyPrefix+i+"_port"));
            Address address = new Address(properties.getProperty(keyPrefix+i+"_ip"), port);
            ips[i] = address;
        }
        return ips;
    }
}

