package krd180000.client;

import krd180000.model.Address;
import krd180000.model.FileOpMessage;
import krd180000.model.FileOpResult;
import krd180000.model.FileOperation;

import java.util.ArrayList;

public class ServerOpHandler {
    private Address[] serverIps;

    public ServerOpHandler(Address[] serverIps) {
        this.serverIps = serverIps;
    }

    public FileOpResult handle(FileOperation op,int toServer){
        FileOpResult result= null;
        if(op== FileOperation.Read){
            result = new FileOpResult(true,"You suck",null,FileOperation.Read);
        }else if (op == FileOperation.Write){
            result = result = new FileOpResult(true,null,null,FileOperation.Write);
        }else {
            result = result = new FileOpResult(true,null,new ArrayList<>(),FileOperation.Enquiry);
        }
        return result;
    }
}
