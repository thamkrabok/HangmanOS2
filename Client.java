import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
public class Client {

    private static int LP = 5;
    private static int InitPort = 6789;
	private static int IsWin = 0;
    private static int IsLose = 0;
	private static int MissCount = 0;
    private static String HOST = "127.0.0.1";
    private static Socket socket = null;
    private static String SecretWord = "";
    private static String Miss = "";
	private static ObjectOutputStream SocketOutput = null;
    private static ObjectInputStream SocketInput = null;


    public static void main( String[] args ) {
        Scanner keyboard;
		
        System.out.println("Connect to server " + HOST + ":" + InitPort);
        try {
          
            socket = new Socket(HOST, InitPort);
     
            SocketOutput = new ObjectOutputStream(socket.getOutputStream());
            SocketOutput.writeObject("Start");

        
            SocketInput = new ObjectInputStream(socket.getInputStream());
            String newPort = (String) SocketInput.readObject();
            System.out.println("new Port: " + newPort);
            keyboard = new Scanner(System.in);

           
            socket = new Socket(HOST, Integer.parseInt(newPort));
            SocketOutput = new ObjectOutputStream(socket.getOutputStream());
            SocketInput = new ObjectInputStream(socket.getInputStream());
  
            GetStatus();

            while (IsWin == 0 && IsLose ==0) {


                
                System.out.println("**********************************************\n");
                System.out.println("You have Life Point : " + (LP - MissCount));
                System.out.println("Word:\t" + SecretWord);
                System.out.println("Misses: " + Miss);
				System.out.println("\n ");

				System.out.println(Human[LP-MissCount]);

                System.out.print("\nGuess: ");

                
                sendUserInput(keyboard.nextLine());

               
                GetStatus();
            }

            System.out.println("**********************************************");

            if (IsWin==1){
                System.out.println( "Winner" );
                System.out.println( "SecretWord was : " + SecretWord );

            }
            else if (IsLose ==1) {
                String answer = GetAnswer();
                System.out.println("Game Over");
                System.out.println("SecretWord was : " + answer);
				System.out.println("\n ");

				System.out.println(Human[0]);
            }

    
            SocketOutput.writeObject("Exit");
            
         
        
            SocketOutput.close();
            SocketInput.close();
            socket.close();

        }
         catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

