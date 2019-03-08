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
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            socket.getOutputStream();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendReply(int fromProcess,int toProcess){
        sendMessage(toProcess,new Message(MessageType.Reply,fromProcess));
    }

    public void sendRequest(int fromProcess, int toProcess,int seqNumber){
        sendMessage(toProcess,new Message(MessageType.Request, seqNumber, fromProcess));
    }
}
