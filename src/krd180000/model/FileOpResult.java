package krd180000.model;

import java.io.Serializable;
import java.util.ArrayList;

public class FileOpResult implements Serializable {
    private boolean success;
    private String str;
    private ArrayList<String> files;
    private FileOperation operation;

    public FileOpResult(boolean success, String str, ArrayList<String> files, FileOperation operation) {
        this.success = success;
        this.str = str;
        this.files = files;
        this.operation = operation;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public FileOperation getOperation() {
        return operation;
    }

    public void setOperation(FileOperation operation) {
        this.operation = operation;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\nStatus: ");
        sb.append(success);
        if(!success){
            return sb.toString();
        }
        sb.append("\n");
        if(operation==FileOperation.Read) {
            sb.append(str);
        }else if(operation==FileOperation.Enquiry) {
            sb.append(String.join(", ",files));
        }
        return sb.toString();
    }
}
