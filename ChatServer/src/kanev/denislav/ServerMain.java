package kanev.denislav;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Denislav on 4/18/2019.
 */
public class ServerMain {
    public static void main(String[] args){
        int port = 8810;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true) {
                System.out.println("About to accept connection:...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                ServerWorker worker = new ServerWorker(clientSocket);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
