package kanev.denislav;

import java.io.*;
import java.net.Socket;


/**
 * Created by Denislav on 4/18/2019.
 */
public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private String login = null;

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
            String[] tokens = line.split(" ");
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("quit".equalsIgnoreCase(cmd)) {
                    break;
                }else if("login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                } else {
                    String msg = "unknown " + cmd + System.lineSeparator();
                    outputStream.write(msg.getBytes());
                 }
            }
        }

        clientSocket.close();
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) {
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];

            if ((login.equals("guest") && password.equals("guest")) || (login.equals("jim") && password.equals("jim"))) {
                String msg = "ok login" + System.lineSeparator();
                try {
                    outputStream.write(msg.getBytes());
                    this.login = login;
                    System.out.println("User logged in successfully: " + login);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                    String msg = "error login" + System.lineSeparator();
                    try {
                        outputStream.write(msg.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

