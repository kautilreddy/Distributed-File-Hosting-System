package krd180000.client;

import krd180000.model.Address;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements Runnable{
    private MessageHandler messageHandler;
    private int totalClients;
    private static final int TOTAL_SERVERS = 3;
    private int port;
    private int clientId;
    private List<Socket> otherClients;
    private List<Socket> servers;
    private MutExRunner mutExRunner;
    private SocketReceiver receiver;
    public Client(int clientId,int totalClients,Address[] clientIps) throws IOException {
        this.clientId = clientId;
        this.totalClients = totalClients;
        this.otherClients = new ArrayList<Socket>(totalClients-1);
        this.servers = new ArrayList<Socket>(TOTAL_SERVERS);
        this.messageHandler = new MessageHandler(clientIps);
        this.mutExRunner = new MutExRunner(clientId,totalClients,port,messageHandler);
        this.receiver = new SocketReceiver(mutExRunner,port,messageHandler);
    }

    @Override
    public void run(){
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
        System.out.println("Client started");
        System.out.println("Address is : ");
        int totalClients = Integer.valueOf(args[1]);
        int currentClientId = Integer.valueOf(args[0]);
        Address[] clientIps = getIPs("Enter the ip of the client ",currentClientId,totalClients, true);
        Address[] serverIps = getIPs("Enter the ip of the servers ",currentClientId,TOTAL_SERVERS, false);
        Client client = new Client(currentClientId,totalClients,clientIps);
        new Thread(client).start();
    }
    private static Address[] getIPs(String prompt, int currentClientId, int count, boolean clientPrompt){
        Address[] ips = new Address[count];
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < count; i++) {
            if(i==currentClientId&&clientPrompt) {
                ips[i] = null;
                continue;
            }
            System.out.println(prompt + i);
            Address address = new Address(in.nextLine(), in.nextInt());
            ips[i] = address;

        }
        return ips;
    }
}

