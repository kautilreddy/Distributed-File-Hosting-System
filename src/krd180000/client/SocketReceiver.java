package krd180000.client;

import krd180000.model.Message;

import java.io.IOException;
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
                ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
                Object messageObj = stream.readObject();
                Message message = (Message) messageObj;
                new RequestHandler(message,mutExRunner,messageHandler).start();
                stream.close();
                socket.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
