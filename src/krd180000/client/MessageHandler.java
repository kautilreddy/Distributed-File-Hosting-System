package krd180000.client;

import krd180000.model.Address;
import krd180000.model.Message;
import krd180000.model.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class MessageHandler {
    private Address[] addresses;
    public MessageHandler(Address[] addresses) {
        this.addresses = addresses;
    }
    public void sendMessage(int toProcess, Message message){
        Socket socket=null;
        try {
            socket = new Socket(addresses[toProcess].getIp(),addresses[toProcess].getPort());
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(message);
            //socket.getOutputStream();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void sendReply(int fromProcess,int toProcess,String forFile){
        System.out.println("Sending reply to "+toProcess);
        sendMessage(toProcess,new Message(MessageType.Reply,fromProcess,forFile));
    }

    public void sendRequest(int fromProcess, int toProcess,int seqNumber,String forFile){
        System.out.println("Sending request to "+toProcess+" "+seqNumber);
        sendMessage(toProcess,new Message(MessageType.Request, seqNumber, fromProcess,forFile));
    }
}
