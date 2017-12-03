
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lazarko
 */
public class ClientThread implements Runnable{
    
    private final static String QUIT_MSG = "You have disconnected from server";
    PrintWriter output;
    BufferedReader fromServer;
    String in;
    String write;
    String inputString;
    private boolean isConnected;
    private Socket socket;
    
    public ClientThread(Socket socket){
        this.socket = socket;
        isConnected = true;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner (System.in);
        try{
            
            while(isConnected){
               output = new PrintWriter(socket.getOutputStream());
               fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               in = fromServer.readLine();
               System.out.println(in);
               write = sc.nextLine();
               output.println(write);
               output.flush();
               
               inputString = fromServer.readLine();
               if(inputString.equalsIgnoreCase("Quit")){
                   disconnect();
               }
               System.out.println(inputString);     
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    private void disconnect() throws IOException{
            socket.close();
            System.out.println(QUIT_MSG);
    }
  
    
}
