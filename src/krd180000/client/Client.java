package krd180000.client;

import krd180000.Address;
import krd180000.Lock;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client implements Runnable{
    private int totalClients;
    private static final int TOTAL_SERVERS = 3;
    private int port;
    private int clientId;
    private List<Socket> otherClients;
    private List<Socket> servers;
    private MutExRunner mutExRunner;
    private SocketReceiver receiver;
    public Client(int clientId,int totalClients) throws IOException {
        this.clientId = clientId;
        this.totalClients = totalClients;
        this.otherClients = new ArrayList<Socket>(totalClients-1);
        this.servers = new ArrayList<Socket>(TOTAL_SERVERS);
        this.mutExRunner = new MutExRunner(clientId,totalClients,port);
        this.receiver = new SocketReceiver(mutExRunner,port);
    }

    private List<Address> getIPs(String prompt, int count){
        List<Address> ips = new ArrayList<Address>(count);
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < count; i++) {
            System.out.println(prompt+i);
            Address address = new Address(in.nextLine(),in.nextInt());
            ips.add(address);
        }
        return ips;
    }
    @Override
    public void run(){
        System.out.println("Client started");
        System.out.println("Address is : ");
        List<Address> clientIps = getIPs("Enter the ip of the client ",totalClients-1);
        List<Address> serverIps = getIPs("Enter the ip of the servers ",TOTAL_SERVERS);
        receiver.start();
//        establishSockets(clientIps,serverIps);

    }

    private void establishSockets(List<Address> clientIps,List<Address> serverIPs) {
        for (Address address: clientIps) {
            otherClients.add(new Socket());
        }
    }

    public static void main(String[] args) throws IOException {
        if(args.length<2){
            System.out.println("Pass the client id and total number of clients");
            return;
        }
        Client client = new Client(Integer.valueOf(args[0]),Integer.valueOf(args[1]));
        new Thread(client).start();
    }
}

