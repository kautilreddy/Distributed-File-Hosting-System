package krd180000;

import java.io.IOException;
import java.net.ServerSocket;

public class OpenPorts {
    public static void main(String[] args) throws IOException {
        int count = 0;
        ServerSocket[] sockets = new ServerSocket[6];
        for (int port = 49152; port < 65535; port++) {
            try {
                ServerSocket s = new ServerSocket(port);
                sockets[count] = s;
                System.out.println("listening on port: " + s.getLocalPort());
                count++;
                if (count == 6) {
                    break;
                }

            } catch (IOException ex) {
                System.err.println("no available ports");
            }
        }
        for (ServerSocket socket:sockets) {
            socket.close();
        }
    }
}
