package krd180000.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketReceiver extends Thread{
    private ServerSocket serverSocket;
    private MutExRunner mutExRunner;

    public SocketReceiver(MutExRunner mutExRunner,int port) throws IOException {
        this.mutExRunner = mutExRunner;
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run(){
        while (true){
            try {
                Socket socket = serverSocket.accept();
                Object messageObj = new ObjectInputStream(socket.getInputStream()).readObject();
                Message message = (Message) messageObj;
                new RequestHandler(message,mutExRunner).start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
