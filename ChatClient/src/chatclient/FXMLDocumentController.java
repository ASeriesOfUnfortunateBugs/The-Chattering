package chatclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
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
    public Socket server;
    public DataInputStream fromServer;
    public DataOutputStream toServer;
    
    // server constants
    final String SERVER_IP = "localhost";
    final int SERVER_PORT = 9090;
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
            fromServer.close();
            toServer.close();
            server.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
            
        // close the program
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        // msg informing client of connection to server
        chatTextArea.setText("Connecting to server...\n");
            
        new Thread( () -> {
            try {
                // creat server connection
                server = new Socket(SERVER_IP, SERVER_PORT);
            
                // create server connection
                chatTextArea.appendText("Connected!\n");
                
                sendBtn.setOnAction(e -> {
                    new Thread ( () -> {
                        try {
                            // create stream to server
                            toServer = new DataOutputStream(server.getOutputStream());
                            
                            // send message to server
                            toServer.writeUTF(msgTextField.getText());
                        
                            // add message to chat text area
                            chatTextArea.appendText("YOU: " + msgTextField.getText() + "\n");
                            
                            // check if EXIT is used
                            if (msgTextField.getText().equals(EXIT)) {
                                chatTextArea.appendText("You have exited the chat!\n" +
                                        "Thanks for using The Chattering chat service!\n");
                            }
                        
                            // clear message text field
                            msgTextField.setText("");
                        } catch (Exception ex) {
                            chatTextArea.appendText(ex.toString() + "\n");
                        }
                    }).start();
                });
                
                // listen for messages from server
                while (true) {
                    fromServer = new DataInputStream(server.getInputStream());
                    String input = fromServer.readUTF() + "\n";
                    
                    chatTextArea.appendText("SERVER: " + input);
                }
            } catch (EOFException ignore) {
                
            } catch (Exception ex) {
                chatTextArea.appendText(ex.toString() + "\n");
            }
        }).start();
    }    
    
}
