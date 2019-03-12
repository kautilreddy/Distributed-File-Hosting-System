package krd180000.client;

import krd180000.Lock;

import java.io.IOException;
import java.util.Arrays;

public class MutExRunner{
    private MessageHandler messageHandler;
    private int me;
    private int N;
    private int sequenceNumber;
    private int highestSequenceNumber;
    private int outstandingReplyCount;
    private boolean requestingCriticalSection;
    private boolean[] deferredReply;
    private boolean[] shouldIRequest;
    private String forFile;
    public MutExRunner(int me, int n, MessageHandler messageHandler,String forFile){
        this.me = me;
        N = n;
        sequenceNumber = 0;
        highestSequenceNumber = 0;
        this.deferredReply = new boolean[N+1];
        this.shouldIRequest = new boolean[N+1];
        Arrays.fill(shouldIRequest,true);
        this.messageHandler = messageHandler;
        this.forFile = forFile;
    }

    public void execute(Runnable criticalCode) throws InterruptedException {
        synchronized (Lock.getLockObject()) {
            requestingCriticalSection = true;
            sequenceNumber = highestSequenceNumber+1;
            outstandingReplyCount= N-1;
        }
        for (int i = 1; i <= N; i++) {
            if(i!=me){
                //send request message to i
                synchronized (Lock.getLockObject()){
                    if(!shouldIRequest[i]){
                        //the Roucairol-Carvalho optimization
                        --outstandingReplyCount;
                        continue;
                    }
                }
                messageHandler.sendRequest(me,i,sequenceNumber,forFile);
            }
        }
        synchronized (Lock.getLockObject()) {
            while (outstandingReplyCount != 0) {
                Lock.getLockObject().wait();//wait until outstandingReplyCount = 0
            }
        }

        criticalCode.run();

        synchronized (Lock.getLockObject()){
            requestingCriticalSection = false;
            for (int i = 1; i <= N; i++) {
                if(deferredReply[i]){
                    deferredReply[i] = false;
                    //send reply message to i
                    sendReplyAndMarkAsSent(i);
                }
            }
        }
    }

    public void sendReplyAndMarkAsSent(int toProcess){
        //marking that a reply was sent to this node (part of the Roucairol-Carvalho optimization)
        synchronized (Lock.getLockObject()){
            shouldIRequest[toProcess] = true;
        }
        messageHandler.sendReply(me,toProcess,forFile);
    }

    public int getHighestSequenceNumber() {
        return highestSequenceNumber;
    }

    public void setHighestSequenceNumber(int highestSequenceNumber) {
        this.highestSequenceNumber = highestSequenceNumber;
    }

    public boolean isRequestingCriticalSection() {
        return requestingCriticalSection;
    }

    public int getMe() {
        return me;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getOutstandingReplyCount() {
        return outstandingReplyCount;
    }

    public void setOutstandingReplyCount(int outstandingReplyCount) {
        this.outstandingReplyCount = outstandingReplyCount;
    }

    public boolean[] isDeferredReply() {
        return deferredReply;
    }

    public boolean[] getShouldIRequest() {
        return shouldIRequest;
    }

    public void setShouldIRequest(boolean[] shouldIRequest) {
        this.shouldIRequest = shouldIRequest;
    }
}
