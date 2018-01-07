/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotspot;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.PauseTransitionBuilder;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 *
 * @author Mugambbo
 */
public class HotspotController implements Initializable {
    
    ProcessesOutput po = new ProcessesOutput();
    
    @FXML private TextField networkName;
    @FXML private PasswordField password;
    @FXML private TextArea activity;
    @FXML private ToggleButton start;
    @FXML private MenuItem close;
    @FXML private MenuItem about;
    @FXML private ProgressIndicator progress;
    @FXML private RadioButton onOff;
    @FXML private MenuItem interfaces;
    @FXML private MenuItem drivers;
    @FXML private MenuItem networks;
    @FXML private MenuItem profiles;
    @FXML private MenuItem status;
    @FXML private MenuItem security;
    @FXML private MenuItem clients;
    @FXML private MenuItem moreInfo;
    @FXML private MenuItem save;
    @FXML private MenuItem faq;
    @FXML private MenuItem help;
    @FXML private CheckMenuItem autostart;
    @FXML private CheckMenuItem setIP;
    @FXML private ImageView tweet;
    @FXML private Label errorLabel;
    private String valueStoredBefore;
    public boolean exitApp;
    private SequentialTransition sequentialTransition;

    
    @FXML
    private void help (ActionEvent event){
        activity.setText("Help:\n\n"
                + "1. Turn on your Wireless Adapter\n"
                + "2. Turn off your firewall (if possible)\n"
                + "3. Type a network name\n"
                + "4. Type a strong password\n"
                + "5. Click the Start button\n"
                + "6. Accept the prompts to elevate Coldspot\n"
                + "7. Don't forget to save your network info.\n"
                + "8. Follow @mugambbo to learn more\n"
                + "9. Thank you for using Coldspot!");
    }
    
    @FXML
    private void faq (ActionEvent event){
        activity.setText("Frequently Asked Questions:\n\n"
                + "Why does Coldspot need to be elevated every start clicked?\n"
                + "As of Version 8, the Java Virtual Machine does not run elevated \n"
                + "on system startup on a Windows machine.\n\n"
                + "Why is the maximum number of clients limited to 50?\n"
                + "The default maximum number of clients a PC can host is limited \n"
                + "by Windows to 100. However, certain applications such as \"mHotspot\"\n"
                + "limit this value even further to about 50.\n\n"
                + "How much does Coldspot cost?\n"
                + "Coldspot is free! Although, anyone is free to donate to keep Coldspot \n"
                + "alive and updated.");        
    }
    
    @FXML
    private void autostart(ActionEvent event) {

            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    if (autostart.isSelected()){            
                        try {                
                            CopyFiles initFiles = new CopyFiles();
                            String startupFolder = System.getenv("APPDATA")+"\\Microsoft\\Windows\\Start Menu\\Programs\\"
                                    + "Startup\\";
                            if (!new File(startupFolder).exists()){
                                new File(startupFolder).mkdirs();
                            }
                            initFiles.copyStream(new FileInputStream(System.getProperty("user.dir")+"\\Coldspot 1.1.exe"),
                                    new File (System.getenv("APPDATA")+"\\Microsoft\\Windows\\Start Menu\\Programs\\"            
                                            + "Startup\\Coldspot 1.1.exe"));
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(HotspotController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(HotspotController.class.getName()).log(Level.SEVERE, null, ex);
                        }          
                    } else {
                        new File (System.getenv("APPDATA")+"\\Microsoft\\Windows\\Start Menu\\Programs\\"            
                                            + "Startup\\Coldspot 1.1.exe").deleteOnExit();
                    }

                }
            });
    }
    
    @FXML
    private void start(ActionEvent event) {
        final Activity active = new Activity(networkName.getText(), password.getText(), start.isSelected());
        progress.visibleProperty().bind(active.runningProperty());     
        if ((password.getText()==null || networkName.getText()==null) && start.isSelected()){
            errorLabel.setText("Invalid network name or password");
        }
        else {
            active.start();
            errorLabel.setText("Please wait...");
            sequentialTransition.play();
            
            active.setOnReady(new EventHandler<WorkerStateEvent>(){                
                @Override
                public void handle(WorkerStateEvent t) {
                    errorLabel.setText("Preparing...");
                    sequentialTransition.play();                
                }
            });
            
            
            active.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
                @Override
                public void handle(WorkerStateEvent t) {
                    String activeVal = active.getValue();
                    activity.setText(activeVal);
                    errorLabel.setText("");
               
                    if (active.getValue().equals("The hosted network stopped. \n\n")){
                        start.setText("Start");
                        start.setSelected(false);
//                        start.setStyle("");
                        networkName.setEditable(true);
                        password.setEditable(true);
                        errorLabel.setText("Done");
                        sequentialTransition.play();
                    }
                    else {
                        start.setText("Stop");                        
                        start.setSelected(true);
//                        start.setStyle("-fx-base: gray;");
                        networkName.setEditable(false);
                        password.setEditable(false);
                        errorLabel.setText("Done!");
                        sequentialTransition.play();
                    }

                    if (activeVal.contains("Not started") || activeVal.contains("Not Available")){
                        onOff.setSelected(false);
                        onOff.setText("OFF");                        
                        start.setText("Start");
                        start.setSelected(false);                        
                        errorLabel.setText("Issues: Check Wireless Adapter");
                        JFXAlertDialog.messageDialog("Coldspot", null, "Oops! Ensure your wireless adapter is "
                                + "turned on and functioning. Disconnect all active wireless networks. If this issue persist, "
                                + "restart your PC and try again.");
                        try {                
                            Process stop = Runtime.getRuntime().exec("cmd /c netsh wlan stop hostednetwork");
                        } catch (IOException ex) {
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                        }                                                
                    }      
                    else {
                        onOff.setSelected(true);
                        onOff.setText("ON");          
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            });
            
            active.setOnFailed(new EventHandler<WorkerStateEvent>(){
                
                @Override
                public void handle(WorkerStateEvent t) {
                    
                    errorLabel.setText("Unable to create a network");
                    sequentialTransition.play();
                }
            });
        }
        


    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onOff.setDisable(true);
        activity.setEditable(false);
        activity.setText("Before you Begin:\n\n"
                + "1. Turn on your Wireless Adapter\n"
                + "2. Turn off your firewall (if possible)\n"
                + "3. Type a network name\n"
                + "4. Type a strong password\n"
                + "5. Click the Start button\n"
                + "6. Accept the prompts to elevate Coldspot\n"
                + "7. Don't forget to save your network info.\n"
                + "8. Connect with @mugambbo on twitter\n"
                + "9. Thank you for using Coldspot!");
        
        save.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                saveData();
            }
        });

        
        checkAutostartMenuItem();
        
        close.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                                
                try{
                    String closeMsg = "Quiting Coldspot disconnects all active connections and shuts down the network. Continue Anyway?";
                    Optional <ButtonType> closeOption = JFXAlertDialog.confirmDialog("Coldspot", null, closeMsg);
                    if (closeOption.get() == ButtonType.OK){
                        Process stop = Runtime.getRuntime().exec("cmd /c netsh wlan stop hostednetwork");    
                        System.exit(0);
                    }

                } catch (IOException ex) {
                    JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                }
                
            }
            
        });
        
        interfaces.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            errorLabel.setText("Please wait...");
                            Process showInterfaces = Runtime.getRuntime().exec("cmd /c netsh wlan show interfaces");            
                            activity.setText(po.output(showInterfaces));                    
                            errorLabel.setText("Interfaces");                    
                            sequentialTransition.play();                    
                        } catch (IOException ex) {                  
                            errorLabel.setText("Cannot display interfaces");
                            sequentialTransition.play();
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());                    
                        }
                    }
                });

            }
        });
        
        drivers.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            errorLabel.setText("Please wait...");
                            Process showDrivers = Runtime.getRuntime().exec("cmd /c netsh wlan show drivers");            
                            activity.setText(po.output(showDrivers));        
                            errorLabel.setText("Drivers");
                            sequentialTransition.play();                                        
                        } catch (IOException ex) {
                            errorLabel.setText("Cannot display drivers");
                            sequentialTransition.play();
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());                    
                        }
                    }
                });

            }
        });
        
        networks.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Platform.runLater(new Runnable () {

                    @Override
                    public void run() {
                        try {
                            errorLabel.setText("Please wait...");
                            Process showNetworks = Runtime.getRuntime().exec("cmd /c netsh wlan show networks");            
                            activity.setText(po.output(showNetworks));
                            errorLabel.setText("Networks");
                            sequentialTransition.play();                    
                        } catch (IOException ex) {
                            errorLabel.setText("Cannot display Netowrks");
                            sequentialTransition.play();                    
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                        }
                    }
                });

            }
        });
        
        profiles.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Platform.runLater(new Runnable () {
                    @Override
                    public void run() {
                        try {
                            errorLabel.setText("Please wait...");                    
                            Process showProfiles = Runtime.getRuntime().exec("cmd /c netsh wlan show profiles");            
                            activity.setText(po.output(showProfiles)); 
                            errorLabel.setText("Profiles");
                            sequentialTransition.play();                    
                        } catch (IOException ex) {
                            errorLabel.setText("Cannot display profiles");
                            sequentialTransition.play();                    
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                        }
                    }
                });

            }
        });
        
        status.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            errorLabel.setText("Please wait...");                    
                            Process showStatus = Runtime.getRuntime().exec("cmd /c netsh wlan show profiles");            
                            activity.setText(po.output(showStatus));        
                            errorLabel.setText("Status");
                            sequentialTransition.play();                       
                        } catch (IOException ex) {
                            errorLabel.setText("Cannot display status");
                            sequentialTransition.play();                    
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                        }
                    }
                });

            }
        });
        
        security.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            errorLabel.setText("Please wait...");                    
                            Process showSecurity = Runtime.getRuntime().exec("cmd /c netsh wlan show hostednetwork setting=security");            
                            activity.setText(po.output(showSecurity));
                            errorLabel.setText("Security Infomation");
                            sequentialTransition.play();                    
                        } catch (IOException ex) {
                            errorLabel.setText("Cannot display security info");
                            sequentialTransition.play();                    
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                        }
                    }
                });

            }
        });
        
        clients.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            errorLabel.setText("Please wait...");                    
                            Process showClients = Runtime.getRuntime().exec("cmd /c arp -a");            
                            activity.setText(po.output(showClients));        
                            errorLabel.setText("Clients");
                            sequentialTransition.play();                    
                        } catch (IOException ex) {
                            errorLabel.setText("Cannot display list of clients");
                            sequentialTransition.play();                    
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                        }
                    }
                });

            }
        });
        
        moreInfo.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            errorLabel.setText("Please wait...");                    
                            Process showMoreInfo = Runtime.getRuntime().exec("cmd /c netsh wlan show hostednetwork");            
                            activity.setText(po.output(showMoreInfo));        
                            errorLabel.setText("Hosted Network Info");
                            sequentialTransition.play();                    
                        } catch (IOException ex) {
                            errorLabel.setText("Cannot display hosted network info");
                            sequentialTransition.play();                    
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                        }
                    }
                });
            }
        });
        
        setIP.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            String elevater = System.getenv("APPDATA")+"\\Coldspot";                    
                            if (setIP.isSelected()){
                                errorLabel.setText("Please wait...");                        
                                Process setIPElevated = Runtime.getRuntime().exec(elevater+"\\Elevate.exe -wait -k prog netsh interface ip set address \"Wireless Network Connection 2\" static 192.168.159.1 255.255.255.0 196.168.159.1 & cls & exit");            
                                int a = setIPElevated.waitFor();
                                activity.setText(po.output(setIPElevated));       
                                errorLabel.setText("Static IP assigned to wireless adapter");
                                sequentialTransition.play();                        
                            } 
                            else {
                                errorLabel.setText("Please wait...");                        
                                Process setIPElevated = Runtime.getRuntime().exec(elevater+"\\Elevate.exe -wait -k prog netsh interface ip set address \"Wireless Network Connection 2\" dhcp & exit");            
                                int a = setIPElevated.waitFor();
                                activity.setText(po.output(setIPElevated));                                
                                errorLabel.setText("Dynamic IP assigned to wireless adapter");
                                sequentialTransition.play();                                               
                            }
                        } catch (IOException | InterruptedException ex) {
                            errorLabel.setText("Error assigning IP to network adapter");
                            sequentialTransition.play();                                           
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());                    
                        }
                    }
                });
            }
        });

        about.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Parent rootAbout = FXMLLoader.load(getClass().getResource("HotspotAbout.fxml"));                
                            Stage aboutStage = new Stage();
                            aboutStage.getIcons().add(new Image(Coldspot10.class.getResourceAsStream("coldspot.png")));                             
                            aboutStage.setScene(new Scene (rootAbout));
                            aboutStage.initModality(Modality.APPLICATION_MODAL);
                            aboutStage.initStyle(StageStyle.UNIFIED);
                            aboutStage.setTitle("Coldspot: About");                            
                            aboutStage.setResizable(false);
                            aboutStage.show();
                            errorLabel.setText("About");
                            sequentialTransition.play();                                           
                        } catch (IOException ex) {
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                        }
                    }
                });
                
            }
            
        });
        
        onTransit();
        errorLabel.setText("Welcome Back");
        sequentialTransition.play();                       

        tweetListeners();
        Tooltip tweetTooltip = new Tooltip("Tell us what you think!");
        Tooltip.install(tweet, tweetTooltip);
        setData();
        
        
        //activity.setVisible(true);
        
    }
    
    private void onTransit (){
            TranslateTransition translateTransition =
                TranslateTransitionBuilder.create()
                .duration(Duration.seconds(2))
                .fromX(0)
                .toX(40)
                .cycleCount(2)
                .autoReverse(true)
                .build();        
            PauseTransitionBuilder.create()
                .duration(Duration.seconds(1))
                .build();
           FadeTransition fadeTransition =
                FadeTransitionBuilder.create()
                .duration(Duration.seconds(1))
                .fromValue(1)
                .toValue(0.3)
                .cycleCount(2)
                .autoReverse(true)
                .build();
           
            sequentialTransition = SequentialTransitionBuilder.create()
                .node(errorLabel)
                .children(fadeTransition, translateTransition)
                .cycleCount(1)
                .autoReverse(true)
                .build();           
    }
    
    public void tweetListeners (){
        tweet.setOnMouseEntered(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                valueStoredBefore = errorLabel.getText();
                errorLabel.setText("@Mugambbo");
            }
        });
        tweet.setOnMouseExited(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                errorLabel.setText(valueStoredBefore);
            }
        });
        tweet.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                SepiaTone sp = new SepiaTone();
                sp.setLevel(0.5d);
                tweet.setEffect(sp);
                if(Desktop.isDesktopSupported()){
                    try {
                        Desktop.getDesktop().browse(new URL ("http://www.twitter.com/mugambbo").toURI());
                    } catch (IOException | URISyntaxException ex) {
                        errorLabel.setText("Oops! An error occured.");
                        JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());                        
                    }
                }
            }
        });
    }
    
    public void setData (){
        try {
            File dataFile = new File (System.getenv("APPDATA")+"\\Coldspot\\coldspot.data");
            Scanner dataInput = new Scanner(dataFile);
            String detailsFromFile = null;
            while (dataInput.hasNextLine()){
                detailsFromFile = dataInput.nextLine();
            }
            if (detailsFromFile != null){
                String networkNameFromFile = detailsFromFile.split("%, ")[0];
                String passwordFromFile = detailsFromFile.split("%, ")[1];
                this.networkName.setText(networkNameFromFile);
                this.password.setText(passwordFromFile);
            }
            
        } catch (FileNotFoundException ex) {
            errorLabel.setText("Couldn't remember your network details");
        }
        
    }
    
    public void saveData (){
        if (!new File(System.getenv("APPDATA")+"\\Coldspot\\coldspot.data").exists()){
            try {
                new File(System.getenv("APPDATA")+"\\Coldspot\\coldspot.data").createNewFile();
            } catch (IOException ex) {
               errorLabel.setText("Error 101: Cannot save your info");
            }
        }
        if (networkName.getText().length() >= 3 && password.getText().length() >= 8){
            FileWriter dataWriter = null;
            try {
                File dataFile = new File(System.getenv("APPDATA")+"\\Coldspot\\coldspot.data");
                dataWriter = new FileWriter(dataFile, false);
                dataWriter.write(this.networkName.getText()+"%, ");
                dataWriter.write(this.password.getText());
                errorLabel.setText("Network details saved.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error writing network details");
            } finally {
                try {
                    dataWriter.flush();
                    dataWriter.close();
                } catch (IOException ex) {
                    errorLabel.setText("Oops! Cannot save network details");
                    JFXAlertDialog.errorDialog("Coldspot", null, "Make sure Coldspot has write permissions to your hard drive.");                    
                }
            }
        }        
        else {

                errorLabel.setText("Oops! Network details must be correct.");
                JFXAlertDialog.errorDialog("Coldspot", null, "Make sure your network name and password are typed correctly.");
        }
    }
    
    public void checkAutostartMenuItem (){
        if (new File (System.getenv("APPDATA")+"\\Microsoft\\Windows\\Start Menu\\Programs\\"            
                                + "Startup\\Coldspot 1.1.exe").exists()){
         autostart.setSelected(true);
        } else {
            autostart.setSelected(false);            
        }        
    }
    
    
    
}
