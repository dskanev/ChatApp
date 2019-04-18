package kanev.denislav;

import java.io.*;
import java.net.Socket;

/**
 * Created by Denislav on 4/18/2019.
 */
public class ServerWorker extends Thread {

    private final Socket clientSocket;

    public ServerWorker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            if ("quit".equalsIgnoreCase(line)){
                break;
            }
            String message = "You typed: " + line + System.lineSeparator();
            outputStream.write(message.getBytes());
        }

        clientSocket.close();
    }
}
