package krd180000;

public class Lock {
    private static Object lock;
    static {
        lock = new Object();
    }

    public static synchronized Object getLockObject(){
        return lock;
    }
}
