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

    public FileOpResult handle(FileOperation op,int toServer,int fromClient) throws IOException, ClassNotFoundException {
        FileOpResult result= null;
        FileOpMessage opMessage = new FileOpMessage(op,"test.txt",fromClient+"@"+System.currentTimeMillis()+"\n");
        Socket socket = new Socket(serverIps[toServer].getIp(),serverIps[toServer].getPort());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(opMessage);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Object o = ois.readObject();
        result = (FileOpResult) o;
//        if(op== FileOperation.Read){
//            result = new FileOpResult(true,"You suck",null,FileOperation.Read);
//        }else if (op == FileOperation.Write){
//            result = result = new FileOpResult(true,null,null,FileOperation.Write);
//        }else {
//            result = result = new FileOpResult(true,null,new ArrayList<>(),FileOperation.Enquiry);
//        }
        return result;
    }
}
