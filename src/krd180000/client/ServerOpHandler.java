package krd180000.client;

import krd180000.model.Address;
import krd180000.model.FileOpMessage;
import krd180000.model.FileOpResult;
import krd180000.model.FileOperation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerOpHandler {
    private Address[] serverIps;

    public ServerOpHandler(Address[] serverIps) {
        this.serverIps = serverIps;
    }

    public FileOpResult handle(FileOperation op,int toServer,int fromClient,String fileName) throws IOException, ClassNotFoundException {
        if(op == FileOperation.Write){
            return handleWrite(fromClient,fileName);
        }
        FileOpResult result= null;
        FileOpMessage opMessage = new FileOpMessage(op,fileName,fromClient+"@"+System.currentTimeMillis()+"\n");
        Socket socket = new Socket(serverIps[toServer].getIp(),serverIps[toServer].getPort());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(opMessage);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Object o = ois.readObject();
        result = (FileOpResult) o;
        return result;
    }
    public FileOpResult handleWrite(int fromClient,String fileName) throws IOException, ClassNotFoundException {
        FileOpResult result = null;
        String message = fromClient+"@"+System.currentTimeMillis()+"\n";
        for (int i = 1; i <= Client.TOTAL_SERVERS; i++) {
            FileOpMessage opMessage = new FileOpMessage(FileOperation.Write,fileName,message);
            Socket socket = new Socket(serverIps[i].getIp(),serverIps[i].getPort());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(opMessage);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object o = ois.readObject();
            result = (FileOpResult) o;
            socket.close();
        }
        return result;
    }
}
