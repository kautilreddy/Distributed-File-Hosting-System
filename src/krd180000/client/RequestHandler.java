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
            System.out.println("Request from "+message.getFromProcess());
            int seqNum = message.getSequenceNumber();
            int processId = message.getFromProcess();
            int highestSequenceNumber = Math.max(mutExRunner.getHighestSequenceNumber(),seqNum);
            synchronized (Lock.getLockObject()){
                mutExRunner.setHighestSequenceNumber(highestSequenceNumber);
                deferIt = mutExRunner.isRequestingCriticalSection()
                        &&((seqNum>mutExRunner.getSequenceNumber())||(seqNum==mutExRunner.getSequenceNumber()&&processId>mutExRunner.getMe()));
                if(deferIt){
                    System.out.println("deferred request from = " + message.getFromProcess());
                    mutExRunner.isDeferredReply()[processId] = true;
                }
            }
            if(!deferIt){
                //send reply to processId
                mutExRunner.sendReplyAndMarkAsSent(processId);
            }
        }else {
            System.out.println("Reply from "+message.getFromProcess());
            synchronized (Lock.getLockObject()){
                mutExRunner.getShouldIRequest()[message.getFromProcess()] = false;
                int getOutstandingReplyCount = mutExRunner.getOutstandingReplyCount();
                mutExRunner.setOutstandingReplyCount(getOutstandingReplyCount-1);
                System.out.println("getOutstandingReplyCount = " + (getOutstandingReplyCount-1));
//                if(mutExRunner.getOutstandingReplyCount()==0){
                    Lock.getLockObject().notifyAll();
//                }
            }
        }
    }
}
