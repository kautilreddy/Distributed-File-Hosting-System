package krd180000.client;

import krd180000.model.Address;
import krd180000.model.FileOpResult;
import krd180000.model.FileOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutExRunnerStore {
    private Map<String,MutExRunner> store;
    private int me;
    private int n;
    private MessageHandler messageHandler;
    public MutExRunnerStore(int me, int n,MessageHandler messageHandler){
        this.n = n;
        this.me = me;
        this.messageHandler = messageHandler;
        store = new HashMap<>();
    }

    public void init(List<String> files) throws IOException, ClassNotFoundException {
        for (String file : files) {
            store.put(file,new MutExRunner(me,n,messageHandler,file));
        }
    }

    public MutExRunner getMutexRunnerFor(String fileName){
        return store.get(fileName);
    }
}
