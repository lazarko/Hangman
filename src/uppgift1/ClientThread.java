
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
public class ClientThread implements Runnable {

    private final static String QUIT_MSG = "You have disconnected from server";
    PrintWriter output;
    BufferedReader fromServer;
    String in;
    String inputString;
    private boolean isConnected;
    private Socket socket;
    public String msg;

    public ClientThread(Socket socket) {
        this.socket = socket;
        isConnected = true;

    }

    @Override
    public void run() {
        try {
            output = new PrintWriter(socket.getOutputStream());
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                Scanner sc = new Scanner(System.in);
                while (true) {
                    msg = sc.nextLine();
                    output.println(msg);
                    output.flush();
                }
            }).start();
            while (isConnected) {
                in = fromServer.readLine();
                printText(in);
                inputString = fromServer.readLine();
                if (inputString.equalsIgnoreCase("Quit")) {
                    disconnect();
                }
                printText(inputString);
            }

        } catch (IOException | NullPointerException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void disconnect() throws IOException {
        socket.close();
        printText(QUIT_MSG);
    }

    private void printText(String messageFromServer) {
        new Thread(() -> {
            System.out.println(messageFromServer);
        }).start();
    }
}
