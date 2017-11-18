
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author Lazarko
 */
public class HangClient {
    private final static String QUIT_MSG = "You have disconnected from server";
    private final static int port = 9393;
    private final static String server = "localhost";
    
    public static void main(String[] args) {
        Socket client;
        try {
            Scanner input = new Scanner(System.in);
            
            client = new Socket(server, port);
            while (client.isConnected()){ 
                
            PrintWriter output = new PrintWriter(client.getOutputStream());
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String in = fromServer.readLine();
            System.out.println(in);
            String write = input.nextLine();
            output.println(write);
            output.flush();
            String inputString = fromServer.readLine();
            if(inputString.startsWith(QUIT_MSG) == true){
                client.close();
                
            }
            System.out.println(inputString);
               
            }  
            client.close();

        } catch (IOException ioe) {
            System.out.print(ioe);
        }catch(NullPointerException e){
            System.out.print(e);
        }

    } 
    

}
