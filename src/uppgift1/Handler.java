
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
           word = findWord();
           char[] hidden = hideWord(word);
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
           char[] check = hangman(word, hiddenUpdate, inChar);
         
           if(Arrays.equals(check, hiddenUpdate)  == true){
               toClient.println(hiddenString + "WRONG");
               toClient.flush();
               System.out.println("Wrong");
               comment = "WRONG!";
               findWord = false;
               this.run();
           } else if(Arrays.equals(check, word) == true){
               String wordString = Arrays.toString(word);
               comment = "Done";
               toClient.println(wordString + " CORRECT");
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
            System.out.println("Client has disconnected");
        }finally{
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

  
     private char[] hideWord(char[] word){
        int length = word.length;
           Character c = '_';
           char[] hiddenWord = new char[length];
           for(int i = 0; i < length; i++){
               hiddenWord[i] = c.charValue();
           }
           return hiddenWord;
       }
   
    
       private char[] findWord()throws FileNotFoundException{
           char[] NewWord = findRandomWord();
           return NewWord;
       }
    
    
       //OBS tänk senare på stora bokstäver vs små 
       private char[] hangman(char[] word, char[] hidden, char[] guess){
           if(guess.length == 1){
             char[] newHidden = new char[hidden.length];  
             char c = guess[0];
             for(int i = 0; i < hidden.length; i++){
                 if(word[i] == c){
                     newHidden[i] = c;
                 }else {
                     newHidden[i] = hidden[i];
                 }
             }
             return newHidden;
           }else if(Arrays.equals(guess, word) == true){
               return word;
           }else{
               return hidden; // Komihåg om den returnerar hidden så ska ett ppoäng avdras
           }
       }
        
        // Den här metodet returnerar ett ord från "words.txt" filen 
        // mha readFromFile() metoden, och därmed returnerar en char[]
        private char[] findRandomWord() throws FileNotFoundException{
            ArrayList<String> listWords = readFromFile();
            int NO_WORDS = listWords.size();
            Random rand = new Random();
            int randIndex = rand.nextInt(NO_WORDS) + 1;
            String s = listWords.get(randIndex);
            char[] word = s.toCharArray();
            return word;
        }
        // Läser av innehållet från "words.txt" och sparar varje ord i en 
        // ArrayList<String> och returnerar det
        private ArrayList<String> readFromFile() throws FileNotFoundException{
            ArrayList<String> words;
            Scanner sc = new Scanner(new File("words.txt"));
            words = new ArrayList<String>();
            while(sc.hasNextLine()){
                String word = sc.nextLine();
                words.add(word);
            }
            return words;
        }

   
        
    }
    
    
    

