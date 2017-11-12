/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    private static int port = 9393;
    private static String server = "localhost";
    
    public static void main(String[] args) {
        Socket client;
        try {
            Scanner input = new Scanner(System.in);
            
            client = new Socket(server, port);
            while (client.isConnected()){ //HÄÄRÄ ASDAS
            PrintWriter output = new PrintWriter(client.getOutputStream());
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String in = fromServer.readLine();
            System.out.println("Hangman: " + in);
            String write = input.nextLine();
            output.println(write);
            output.flush();
            String inputString = fromServer.readLine();
           
            System.out.println(inputString);
               
            }  
            client.close();

        } catch (IOException ioe) {
            System.out.print(ioe);
        }

    } 
    

}
