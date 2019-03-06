package krd180000.client;

import krd180000.Lock;

public class RequestHandler extends Thread{
    private Message message;
    private MutExRunner mutExRunner;
    private boolean deferIt;

    public RequestHandler(Message message,MutExRunner mutExRunner) {
        this.message = message;
        this.mutExRunner = mutExRunner;
        this.deferIt = false;
    }

    @Override
    public void run(){
        if(message.getType()== MessageType.Request){
            int seqNum = message.getSequenceNumber();
            int processId = message.getFromProcess();
            int highestSequenceNumber = Math.max(mutExRunner.getHighestSequenceNumber(),seqNum);
            synchronized (Lock.getLockObject()){
                mutExRunner.setHighestSequenceNumber(highestSequenceNumber);
                deferIt = mutExRunner.isRequestingCriticalSection()
                        &&((seqNum>mutExRunner.getSequenceNumber())||(seqNum==mutExRunner.getSequenceNumber()&&processId>mutExRunner.getMe()));
                if(deferIt){
                    mutExRunner.isDeferredReply()[processId] = true;
                }
            }
            if(!deferIt){
                //send reply to processId
            }
        }else {
            synchronized (Lock.getLockObject()){
                mutExRunner.setOutstandingReplyCount(mutExRunner.getOutstandingReplyCount()-1);
            }
        }
    }
}
