
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

/**
 * This class implements java Socket server
 * @author pankaj
 *
 */
public class Server {

    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 6789;


    public static void main(String args[]) throws ClassNotFoundException {
        //create the socket server object

        //keep listens indefinitely until receives 'exit' call or program terminates
        int i =0;
        Thread thread = null;
        try {
            server = new ServerSocket(port);

            while(true){
                System.out.println("Waiting for the client request");
                Socket socket = server.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                String message = (String) ois.readObject();
                System.out.println("Message Received: " + message);
                Random rand = new Random();
                int newPort = rand.nextInt(9000)+1000;
               
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(""+newPort);

                ois.close();
                oos.close();
                socket.close();
                i++;

            }
        } catch (IOException ex) {
            try {
                server.close();
            } catch (IOException e) {
                System.err.println("ERROR closing socket: " + e.getMessage());
            }
        }
    }

}
