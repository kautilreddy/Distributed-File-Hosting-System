package krd180000.model;

public class Message {
    private MessageType type;
    private int sequenceNumber;
    private int fromProcess;

    public Message(MessageType type, int sequenceNumber, int fromProcess) {
        this.type = type;
        this.sequenceNumber = sequenceNumber;
        this.fromProcess = fromProcess;
    }
    public Message(MessageType type,int fromProcess){
        this(type,-1,fromProcess);
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
}

