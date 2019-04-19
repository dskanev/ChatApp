package kanev.denislav;

/**
 * Created by Denislav on 4/18/2019.
 */
public class ServerMain {
    public static void main(String[] args){

        int port = 8810;
        Server server = new Server(port);
        server.start();
    }


}
