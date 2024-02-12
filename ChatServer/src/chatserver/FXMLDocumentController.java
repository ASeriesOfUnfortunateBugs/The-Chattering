package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/** 
* Class: CIST 2372 Java Programming II
* Term: Fall 2022
* Instructor: Chris Bishop 
* Description: Final
* Author: Rachel Heaton
* 
* By turning in this code, I Pledge: 
* 1. That I have completed the programming assignment independently. 
* 2. I have not copied the code from a student or any source. 
* 3. I have not given my code to any student. 
*
*/

public class FXMLDocumentController implements Initializable {
    // properties
    public ServerSocket server;
    public Socket client;
    public DataOutputStream out;
    public DataInputStream in;
    
    // server constants
    final int SERVER_PORT = 9090;
    final String MSG = "Welcome to The Chattering chat service!\nType 'quit' to exit the session.";
    final String EXIT = "quit";
    
    @FXML
    private TextArea chatTextArea;

    @FXML
    private MenuItem menuClose;

    @FXML
    private TextField msgTextField;

    @FXML
    private Button sendBtn;

    @FXML
    void menuClosed_Clicked(ActionEvent event) {
        // close all threads and server
        try {
            in.close();
            out.close();
            client.close();
            server.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        // close the program
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        new Thread( () -> {
            try {
                // create server socket
                server = new ServerSocket(SERVER_PORT);
                
                // loading message
                chatTextArea.appendText("Loading chat service...\n");
                
                // waiting for connections message
                chatTextArea.appendText("Server waiting for connection...\n");

                // listen for client
                client = server.accept();
                chatTextArea.appendText("Client connected!\n");

                // send welcome string to client
                out = new DataOutputStream(client.getOutputStream());
                out.writeUTF(MSG);
                chatTextArea.appendText(MSG + "\n");
                
                sendBtn.setOnAction(e -> {
                    new Thread( () -> {
                        try {                
                            // send message text field to client
                            out.writeUTF(msgTextField.getText());

                            // add message to chat text area
                            chatTextArea.appendText("YOU: " + msgTextField.getText() + "\n");

                            // clear message text field
                            msgTextField.setText("");
                        } catch (Exception ex) {
                            // print exception information
                            chatTextArea.appendText(ex.toString() + "\n");
                        }
                    }).start();
                });
                
                // listen for messages from client
                while (true) {
                    in = new DataInputStream(client.getInputStream());
                    String input = in.readUTF();
                    
                    // quit listening if client quits
                    if (input.toLowerCase().equals(EXIT)) {
                        chatTextArea.appendText("Client has disconnected..." + ""
                                + "\n");
                        chatTextArea.appendText("Closing chat service..." + "\n");

                        // close all threads and server
                        try {
                            in.close();
                            out.close();
                            client.close();
                            server.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return;
                    }
                    
                    chatTextArea.appendText("CLIENT: " + input + "\n");
                }
            } catch (Exception ex) {
                // print exception information
                chatTextArea.appendText(ex.toString() + "\n");
            }
        }).start();
    }    
    
}
