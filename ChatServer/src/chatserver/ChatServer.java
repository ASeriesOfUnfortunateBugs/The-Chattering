package chatserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

public class ChatServer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        // load FXML GUI file
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        // create a new scene using FXML file
        Scene scene = new Scene(root);
        
        // set title + scene
        stage.setTitle("The Chattering");
        stage.setScene(scene);
        
        // show stage
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
