
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 *
 * @author Lazarko
 */
public class Handler implements Runnable{
    private boolean isConnected;
    //private HangServer server;
    private Socket client;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    char[] hiddenUpdate;
    boolean findWord;
    char[] word;
    private String comment; 
    private final String COMMENT_DISCONNECT = "You have disconnected from server";
    private final String CLIENT_DISCONNECT = "Client has disconnected";
    private final String WELCOME_MSG = "Guess the word, to quit type: QUIT";
    private final String COMMENT_DONE = "Done, next word";
    private final String COMMENT_CORRECT = "Correct, guess again";
    Hangman hangman = new Hangman();
    
    public Handler(Socket client){
        this.client = client; 
        findWord = true;
        isConnected = true;
      
    }
    @Override
    public void run() {
        
        try{
             fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
             toClient = new PrintWriter(client.getOutputStream(), false);
        }catch(IOException ioe){
            System.out.println("FAIL");
        }
        
        try{
           if(findWord == true){
           toClient.println(WELCOME_MSG);
           toClient.flush();    
           word = hangman.findWord();
           char[] hidden = hangman.hideWord(word);
           hiddenUpdate = hidden;
           comment = "";
           }
        
           String hiddenString = Arrays.toString(hiddenUpdate)
                   .replace(',', ' ')
                   .replace('[', ' ')
                   .replace(']', ' ');
           toClient.println(hiddenString + comment);
           toClient.flush();
           System.out.println(Arrays.toString(word));// FÖR SERVERN
           String input = fromClient.readLine();
                     
           char[] inChar = input.toCharArray();
           char[] check = hangman.hangman(word, hiddenUpdate, inChar);
           if(input.startsWith("QUIT") == true){
               toClient.println(COMMENT_DISCONNECT);
               toClient.flush();
               disconnect();
           }else if(Arrays.equals(check, hiddenUpdate)  == true){
               toClient.println(hiddenString + "WRONG");
               toClient.flush();
               System.out.println("Wrong");
               comment = "WRONG!";
               findWord = false;
               this.run();
           } else if(Arrays.equals(check, word) == true){
               hiddenString = Arrays.toString(word);
               comment = "Done, next word";
               toClient.println(hiddenString + " CORRECT");
               toClient.flush();
               System.out.println("Correct");
               findWord = true;
               this.run();
           } else if(Arrays.equals(check, hiddenUpdate) == false){
               String checkString = Arrays.toString(check);
               comment = "Correct, guess again!"; 
               toClient.println(checkString);
               toClient.flush();
               hiddenUpdate = check; 
               findWord = false;
               this.run();
           }
        
         
        }catch(IOException ioe){
            System.out.print("ioe");
            
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
        isConnected = false;
    }
}
    
    
    

