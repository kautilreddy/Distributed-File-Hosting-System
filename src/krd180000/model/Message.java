package krd180000.model;

import java.io.Serializable;

public class Message implements Serializable {
    private MessageType type;
    private int sequenceNumber;
    private int fromProcess;
    private String forFile;

    public Message(MessageType type, int sequenceNumber, int fromProcess,String forFile) {
        this.type = type;
        this.sequenceNumber = sequenceNumber;
        this.fromProcess = fromProcess;
        this.forFile = forFile;
    }
    public Message(MessageType type,int fromProcess,String forFile){
        this(type,-1,fromProcess,forFile);
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getFromProcess() {
        return fromProcess;
    }

    public void setFromProcess(int fromProcess) {
        this.fromProcess = fromProcess;
    }

    public String getForFile() {
        return forFile;
    }

    public void setForFile(String forFile) {
        this.forFile = forFile;
    }
}

