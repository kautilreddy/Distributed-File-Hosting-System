package krd180000.model;

import java.io.Serializable;

public class FileOpMessage implements Serializable {
    private FileOperation operation;
    private String fileName;
    private String appendStr;

    public FileOpMessage(FileOperation operation, String fileName, String appendStr) {
        this.operation = operation;
        this.fileName = fileName;
        this.appendStr = appendStr;
    }

    public FileOperation getOperation() {
        return operation;
    }

    public void setOperation(FileOperation operation) {
        this.operation = operation;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAppendStr() {
        return appendStr;
    }

    public void setAppendStr(String appendStr) {
        this.appendStr = appendStr;
    }
}

