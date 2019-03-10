package krd180000.server;

import krd180000.common.PropertyReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

public class Server extends Thread{
    private String storagePath;
    private int port;
    private ServerSocket serverSocket;
    public Server(String storagePath,int port) throws IOException {
        this.storagePath = storagePath;
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }
    public static void main(String[] args) {
        Properties properties = PropertyReader.read();

    }
}
