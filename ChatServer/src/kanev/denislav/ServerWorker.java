package kanev.denislav;

import java.io.*;
import java.net.Socket;
import java.util.List;


/**
 * Created by Denislav on 4/18/2019.
 */
public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private OutputStream outputStream;

    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
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
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(" ");
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd)) {
                    handleLogoff();
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

    private void handleLogoff() throws IOException {
        List<ServerWorker> workerList = server.getWorkerList();

        // send other online users current user's status
        String onlineMsg = "offline " + login + System.lineSeparator();
        for(ServerWorker worker : workerList) {
            if (!login.equals(worker.getLogin())) {
                worker.send(onlineMsg);
            }
        }
        clientSocket.close();
    }

    public String getLogin() {
        return login;
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


                    List<ServerWorker> workerList = server.getWorkerList();

                    // send current user all other online logins
                    for(ServerWorker worker : workerList) {

                            if (worker.getLogin() != null) {
                                if (!login.equals(worker.getLogin())) {
                                    String msg2 = "online " + worker.getLogin() + System.lineSeparator();
                                    send(msg2);
                                }
                            }

                    }

                    // send other online users current user's status
                    String onlineMsg = "online " + login + System.lineSeparator();
                    for(ServerWorker worker : workerList) {
                        if (!login.equals(worker.getLogin())) {
                            worker.send(onlineMsg);
                        }
                    }

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

    private void send(String msg) throws IOException {

            outputStream.write(msg.getBytes());

    }
}

