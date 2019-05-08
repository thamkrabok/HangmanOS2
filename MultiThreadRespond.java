
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MultiThreadRespond implements Runnable{

    private ServerSocket server;
    private int port ;
    private ObjectOutputStream SocketOutput;
    private ObjectInputStream SocketInput;

    private int LP = 5;

    public MultiThreadRespond(int port){
        this.port = port;
        try{
            server = new ServerSocket(port);
        }catch(Exception e){

        }

    }

    @Override
    public void run(){
        String  [] Word = {  "Avatar","Titanic","Jurassic World",
		"The Avengers","Black Panther","Frozen,Beauty and the Beast","Incredibles",
		"Iron Man","Minions","Aquaman",
		"Captain Marvel","Skyfall","The Dark Knight Rises","Toy Story",
		"Pirates of the Caribbean","Despicable Me","Jurassic Park","Finding Dory",
		"Star Wars","Alice in Wonderland","Zootopia","The Hobbit","The Dark Knight",
		"The Lion King","The Jungle Book","Finding Nemo"};
        String RandomWord;
        char[] SecretWord;
        String User = "";
        int MissCount = 0;
        char[] Miss = new char[5];
        boolean LetterF = false, solved = false;
        RandomWord = Word[ (int)(Math.random() * Word.length) ].toLowerCase();
        SecretWord = new char[RandomWord.length()];

        int IsWin = 0;
        int IsLose = 0;

        for (int i = 0; i < RandomWord.length(); i++) {
            if (RandomWord.charAt(i) == ' ') {
                SecretWord[i] = ' ';
            } else {
                SecretWord[i] = '_';
            }
        }

        StringBuilder RespondSecretWord = new StringBuilder();
        int  RespondMissCount = MissCount;
        StringBuilder RespondMiss = new StringBuilder();


        ////   SecretWord|miss_count|Miss|IsWin|IsLose

        System.out.println("Start Games : " + RandomWord);
        while(true){
            try{
                Socket socket = server.accept();
                SocketInput = new ObjectInputStream(socket.getInputStream());
                SocketOutput = new ObjectOutputStream(socket.getOutputStream());
                boolean running = true;
                while (running){


                    System.out.print("\nHidden Word: ");
                    RespondSecretWord.delete(0,  RespondSecretWord.length());
                    for (int i = 0; i < RandomWord.length(); i++) {
                        System.out.print(SecretWord[i] + " ");
                        RespondSecretWord.append(SecretWord[i]).append(" ");
                    }
                    System.out.print("\nMisses: ");
                    RespondMiss.delete(0,  RespondMiss.length());
                    for (int i = 0; i < Miss.length; i++) {
                        System.out.print(Miss[i]);
                        RespondMiss.append(Miss[i]).append(" ");
                    }


                    /// check action which Clint do
                    String action =  (String) SocketInput.readObject();
                    System.out.println(action);
                    if (action.equals("Start") || action.equals("GetStatus")){
                        /// response games status
                        RespondMissCount = MissCount;
                        SocketOutput.writeObject(RespondSecretWord + "@" + RespondMissCount + "@" + RespondMiss + "@" + IsWin + "@" + IsLose );

                    }else if (action.equals("GetAnswer") && IsLose == 1) {
                        SocketOutput.writeObject(RandomWord);
                    }else if (action.equals("Exit")) {
                        SocketOutput.close();
                        SocketInput.close();
                        socket.close();
                    }else if (action.substring(0,5).equals("send:")){
                        /// get Client input
                        User = action.substring(5,6);
                        System.out.print("\nGuess: " + User);

                        /// Game Logical
                        LetterF = false;
                        for (int i = 0; i < RandomWord.length(); i++) {
                            if (User.toLowerCase().charAt(0) == RandomWord.toLowerCase().charAt(i)) {
                                SecretWord[i] = RandomWord.charAt(i);
                                LetterF = true;
                            }
                        }
                        if (!LetterF) {
                            Miss[MissCount] = User.charAt(0);
                            MissCount++;
                        }
                        int hidden_count = 0;
                        for (int i = 0; i < RandomWord.length(); i++) {
                            if ('_' == SecretWord[i])
                                hidden_count++;
                        }
                        if (hidden_count > 0) {
                            solved = false;
                        } else{
                            solved = true;
                        }
                    }

                    /// check win or lose
                    if (MissCount >= LP){
                        IsLose = 1;

                    }
                    if (solved){
                        IsWin = 1;
                    }
                }




            }catch(Exception e){
                System.out.println(e);
            }

        }
    }

}
