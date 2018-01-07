/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotspot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Muzzi5x
 */
public class HotspotAccessController implements Initializable {
    @FXML private Button activateBtn;
    @FXML private Text activationCode;
    @FXML private TextField keyCode;
    @FXML private ImageView tweetAbout;
    @FXML private ImageView mailAbout;
    
    public void act (){
        
        if (keyCode.getText() != null){
            String keyFromFieldAlph = keyCode.getText().replaceAll("-", "").trim();
            
            
            Secure secure = new Secure ();
            String encryptedKeyFromFieldNoBase = secure.alphabetsToNo(keyFromFieldAlph);
            String encryptedKeyFromFieldNoExact = secure.convertFromBaseToBase(encryptedKeyFromFieldNoBase, 7, 10);            
            
            
            String theKey = secure.serial((System.getenv("computername")+"COLDSPOT").substring(0, 4));
            if (encryptedKeyFromFieldNoExact != null){
                if (encryptedKeyFromFieldNoExact.equals(theKey)){
                    FileWriter keyWriter = null;
                    try {
                        File keyFolder = new File(System.getenv("APPDATA")+"\\Coldspot");
                        keyFolder.mkdir();
                        File keyFile = new File(System.getenv("APPDATA")+"\\Coldspot\\coldspot.certificate");
                        File dataFile = new File(System.getenv("APPDATA")+"\\Coldspot\\coldspot.data");
                        keyFile.createNewFile();
                        dataFile.createNewFile();
                        keyWriter = new FileWriter(keyFile, false);
                        keyWriter.write(keyFromFieldAlph);
                        JFXAlertDialog.messageDialog("Coldspot: Activated", null, "Thank you for registering "
                                + "Coldspot! Restart Coldspot to begin.");
                        Platform.setImplicitExit(true);
                    } catch (IOException ex) {
                        JFXAlertDialog.messageDialog("Coldspot", null, "Failed to authenticate key");
                    } finally {
                        try {
                            keyWriter.flush();
                            keyWriter.close();
                            System.exit(0);                            
                        } catch (IOException ex) {
                            JFXAlertDialog.errorDialog("Coldspot", null, "An error occured while authenticating key");
                        }
                    }
                }
                else {
                    JFXAlertDialog.messageDialog("Coldspot", null, "Invalid key, please verify the key");
                }
            }
        }
        
        else {
            JFXAlertDialog.messageDialog("Coldspot", null, "No can do!");
        }
    }
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Secure secure = new Secure();
        BigInteger bi = new BigInteger (secure.serial((System.getenv("computername")+"COLDSPOT").substring(0, 4)));
        activationCode.setText(activationCode.getText()+" "+bi.multiply(new BigInteger ("2")).toString());
        
        activateBtn.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                act();
            }
        });
    }    
    
}
