package krd180000.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileOpResult implements Serializable {
    private boolean status;
    private String str;
    private ArrayList<String> files;
    private FileOperation operation;

    public FileOpResult(boolean status, String str, ArrayList<String> files, FileOperation operation) {
        this.status = status;
        this.str = str;
        this.files = files;
        this.operation = operation;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
}
