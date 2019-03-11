package krd180000.server;

import krd180000.common.PropertyReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server extends Thread{
    private String storagePath;
    private ServerSocket serverSocket;
    public Server(String storagePath,int port) throws IOException {
        this.storagePath = storagePath;
        this.serverSocket = new ServerSocket(port);
    }
    @Override
    public void run(){
        while (true){
            Socket socket;
            try {
                socket = serverSocket.accept();
                new ClientRequestHandler(storagePath, socket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException{
        int serverNumber = Integer.valueOf(args[0]);
        Properties properties = PropertyReader.read();
        new Server(properties.getProperty("server"+serverNumber+"_storagePath")
                ,Integer.parseInt(properties.getProperty("server"+serverNumber+"_port"))).start();
    }
}
