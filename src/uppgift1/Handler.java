
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Lazarko
 */
public class Handler implements Runnable{
    private Socket client;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    char[] hiddenUpdate;
    boolean findWord;
    boolean first;
    char[] word;
    private final String COMMENT_DISCONNECT = "You have disconnected from server";
    private final String CLIENT_DISCONNECT = "Client has disconnected";
    private final String QUIT_MSG = "QUIT";
    private final String Wrong_MSG = "Hangman: Wrong!";
    private final String WELCOME_MSG = "Hangman: Welcome! Guess the word. To quit type QUIT";
    private final String CORRECT_MSG = "Hangman: Correct, guess again";
    private final String DONE_MSG = "Hangman: Done, next word";
    Hangman hangman = new Hangman();
    
    public Handler(Socket client){
        this.client = client; 
        findWord = true;
        first = true;
    }
    @Override
    public void run() {
        
        try{
             fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
             toClient = new PrintWriter(client.getOutputStream(), false);
        }catch(IOException ioe){
            System.out.println(ioe);
        }
        
        try{
           if(findWord == true){  
           word = hangman.findWord();
           char[] hidden = hangman.hideWord(word);
           hiddenUpdate = hidden;
           }
           
           firstMSG(first);
           first = false;
        
           String hiddenString = Arrays.toString(hiddenUpdate)
                   .replace(',', ' ')
                   .replace('[', ' ')
                   .replace(']', ' ');
           toClient.println(hiddenString); 
           toClient.flush();
           System.out.println(Arrays.toString(word));// FÖR SERVERN
           String input = fromClient.readLine();
                     
           char[] inChar = input.toCharArray();
           char[] check = hangman.hangman(word, hiddenUpdate, inChar);
           if(input.startsWith(QUIT_MSG) == true){
               toClient.println(COMMENT_DISCONNECT);
               toClient.flush();
               disconnect();
           }else if(Arrays.equals(check, hiddenUpdate)  == true){
               toClient.println(hiddenString + Wrong_MSG);
               toClient.flush();
               System.out.println(Wrong_MSG);
               findWord = false;
               this.run();
           } else if(Arrays.equals(check, word) == true){
               hiddenString = Arrays.toString(word);
               toClient.println(hiddenString + DONE_MSG);
               toClient.flush();
               System.out.println(DONE_MSG);
               findWord = true;
               this.run();
           } else if(Arrays.equals(check, hiddenUpdate) == false){
               String checkString = Arrays.toString(check);
               toClient.println(checkString + CORRECT_MSG);
               toClient.flush();
               hiddenUpdate = check; 
               findWord = false;
               this.run();
           }
        
         
        }catch(IOException ioe){
            System.out.print(ioe);
            
        }catch(NullPointerException e){
            System.out.println(CLIENT_DISCONNECT);
        }finally{
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void disconnect(){
        try{
            client.close();
        }catch(IOException ioe){
            System.out.print(ioe);
        }
        System.out.println(CLIENT_DISCONNECT);
    }
    
    private void firstMSG(boolean first){
        if(first == true){
            toClient.println(WELCOME_MSG);
            toClient.flush();
        }
    }
}
    
    
    

