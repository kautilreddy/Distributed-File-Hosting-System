package krd180000;

public class LockTest {
    public static void main(String[] args) {

    }
}
class Var extends Thread{
    public static int i=0;
    public void run(){
        synchronized (Lock.getLockObject()) {
            System.out.println("started");
            while (i == 0) {
                try {
                    Lock.getLockObject().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Invoked");
            for (int j = 0; j < 20; j++) {
                System.out.print(j);
            }
            System.out.println("Heloooooo");
        }
    }
}
class Var2 extends Thread{
    public void run(){
        synchronized (Lock.getLockObject()){
            System.out.println("started2");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Notifying");
            Var.i++;
            Lock.getLockObject().notifyAll();
            for (int i = 0; i < 20; i++) {
                System.out.print("  ");
            }
            System.out.println("Notified");
        }
    }
}