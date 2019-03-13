package krd180000.server;

import krd180000.model.FileOpMessage;
import krd180000.model.FileOpResult;
import krd180000.model.FileOperation;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FileOpHandler {
    private String storagePath;

    public FileOpHandler(String storagePath) {
        this.storagePath = storagePath;
    }

    public FileOpResult handle(FileOpMessage op) throws IOException{
        FileOpResult result= null;
        boolean status = false;
        if(op.getOperation()== FileOperation.Read){
            String lastLine = "";
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(storagePath+"/"+op.getFileName()));
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null)
                {
                    lastLine = sCurrentLine;
                }
                status = true;
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(br!=null){
                    br.close();
                }
            }
            result = new FileOpResult(status,lastLine,null,FileOperation.Read);
        }else if (op.getOperation() == FileOperation.Write){
            BufferedWriter bw=null;
            try {
                bw = new BufferedWriter(new FileWriter(storagePath +"/"+ op.getFileName(),true));
                bw.append(op.getAppendStr());
                status = true;
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(bw!=null){
                    bw.close();
                }
            }
            result = new FileOpResult(status,null,null,FileOperation.Write);
        }else {
            ArrayList<String> files = new ArrayList<>();
            try (Stream<Path> paths = Files.walk(Paths.get(storagePath))) {
                paths
                        .filter(Files::isRegularFile)
                        .forEach((str)->files.add(str.getFileName().toString()));
                status=true;
            }catch (IOException e){
                e.printStackTrace();
            }
            result = new FileOpResult(status,null,files,FileOperation.Enquiry);
        }
        return result;
    }
}
