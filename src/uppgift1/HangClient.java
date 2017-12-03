

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Lazarko
 */
public class HangClient {

    private final static int port = 9393;
    private final static String server = "localhost";
    
    public static void main(String[] args){
        HangClient client = new HangClient();
        client.start();
     }
    
    private void start(){
        Socket socket;
        try{
             socket = new Socket(server, port);
             ClientThread cliThread = new ClientThread(socket);
             Thread thread = new Thread(cliThread);
             thread.start();
             
        } catch (IOException ex) {
            Logger.getLogger(HangClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}