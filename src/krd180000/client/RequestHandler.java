package krd180000.client;

import krd180000.Lock;
import krd180000.model.Message;
import krd180000.model.MessageType;

public class RequestHandler extends Thread{
    private final MessageHandler messageHandler;
    private Message message;
    private MutExRunner mutExRunner;
    private boolean deferIt;

    public RequestHandler(Message message,MutExRunner mutExRunner,MessageHandler messageHandler) {
        this.message = message;
        this.mutExRunner = mutExRunner;
        this.deferIt = false;
        this.messageHandler = messageHandler;
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
                messageHandler.sendReply(mutExRunner.getMe(),processId);
            }
        }else {
            synchronized (Lock.getLockObject()){
                int getOutstandingReplyCount = mutExRunner.getOutstandingReplyCount();
                mutExRunner.setOutstandingReplyCount(getOutstandingReplyCount-1);
                if(mutExRunner.getOutstandingReplyCount()==0){
                    notify();
                }
            }
        }
    }
}
