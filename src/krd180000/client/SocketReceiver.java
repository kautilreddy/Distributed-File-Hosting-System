package krd180000.client;

import krd180000.model.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketReceiver extends Thread{
    private MessageHandler messageHandler;
    private ServerSocket serverSocket;
    private MutExRunner mutExRunner;

    public SocketReceiver(MutExRunner mutExRunner,int port,MessageHandler messageHandler) throws IOException {
        this.mutExRunner = mutExRunner;
        this.serverSocket = new ServerSocket(port);
        this.messageHandler = messageHandler;
    }

    @Override
    public void run(){
        while (true){
            try {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream stream = new ObjectInputStream(inputStream);
                Object messageObj = stream.readObject();
                Message message = (Message) messageObj;
                new RequestHandler(message,mutExRunner,messageHandler).start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
