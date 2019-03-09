package krd180000.client;

import krd180000.Lock;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MutExRunner{
    private MessageHandler messageHandler;
    private int me;
    private int N;
    private int sequenceNumber;
    private int highestSequenceNumber;
    private int outstandingReplyCount;
    private boolean requestingCriticalSection;
    private boolean[] deferredReply;
    private BlockingQueue<String> toSend;
    public MutExRunner(int me, int n, int port,MessageHandler messageHandler) throws IOException {
        this.me = me;
        N = n;
        sequenceNumber = 0;
        highestSequenceNumber = 0;
        this.toSend = new LinkedBlockingQueue<String>();
//        this.serverSocket = new ServerSocket(port);
        this.deferredReply = new boolean[N+1];
        this.messageHandler = messageHandler;
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
                messageHandler.sendRequest(me,i,sequenceNumber);
            }
        }
        synchronized (Lock.getLockObject()) {
            while (outstandingReplyCount == 0) {
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
                    messageHandler.sendReply(me,i);
                }
            }
        }
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
}
