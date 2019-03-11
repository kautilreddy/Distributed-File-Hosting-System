package krd180000.server;

import krd180000.model.FileOpMessage;
import krd180000.model.FileOpResult;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientRequestHandler extends Thread {
    private Socket socket;
    private FileOpHandler handler;

    public ClientRequestHandler(String storagePath, Socket socket) {
        this.socket = socket;
        this.handler = new FileOpHandler(storagePath);
    }

    @Override
    public void run(){
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object message = ois.readObject();
            FileOpMessage op = (FileOpMessage) message;
            FileOpResult result = handler.handle(op);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(result);
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
