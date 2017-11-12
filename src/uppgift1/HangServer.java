
import java.io.IOException;
import java.net.*;

/**
 *
 * @author Lazarko
 */
public class HangServer {
   
   
    private final int port = 9393;
    Handler handler;

    public HangServer() {
    }
   
 
    public static void main(String[] args){
        
        HangServer server = new HangServer();
        server.init();
    }
     
    private void init(){
        try{
            ServerSocket listener = new ServerSocket(port);
            while(true){
                Socket client = listener.accept();
                client.setSoLinger(true, 8000);
                client.setKeepAlive(true);
                startHandler(client);
            }
        }catch(IOException e){
            System.out.print("SERVER FAIL");
        }
    }
    
    private void startHandler(Socket socket) throws SocketException{
        handler = new Handler(socket);
        Thread ht = new Thread(handler);
        ht.start();
    }
    
}
