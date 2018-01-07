/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotspot;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Mugambbo
 */
public class Coldspot10 extends Application {
    private Stage accessStage;
    private SystemTray tray;
    private TrayIcon trayIcon;
    private Popup popup;
//    private Stage accessStage;
    private boolean exitApp;
    private boolean firstTime;
    
    @Override
    public void start(final Stage stage) throws Exception {
        firstTime = true;        
        Parent root = FXMLLoader.load(getClass().getResource("Hotspot.fxml"));
        Scene scene = new Scene(root);
//        stage.initStyle(StageStyle.UTILITY);        
        stage.setTitle("Coldspot 1.1");
//        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("coldspot.png")));
        stage.setScene(scene);
        
        promptActivation();
        
        if (!exitApp){
//            String date = new Date().toString();
            stage.show();            
//            if ((Integer.parseInt(date.split(" ")[2])>19 || Integer.parseInt(date.split(" ")[2])<9) || !date.split(" ")[1].equals("Aug") || (Integer.parseInt(date.split(" ")[5]) > 2015 || Integer.parseInt(date.split(" ")[5])<2015)){
//                JOptionPane.showMessageDialog(null, "Demo version expired. Get a new copy");
//                Platform.exit();
//                System.exit(0);                
//            }
//            else {
//                stage.show();
//            }
        }
        else {
//            stage.close();
//            Platform.setImplicitExit(true);
//            Platform.exit();
            System.exit(0);
        }
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            
            @Override
            public void handle(WindowEvent t) {                
                Platform.setImplicitExit(false);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.hide();
                        if (firstTime){
                            trayIcon.displayMessage("Coldspot", "Still running", TrayIcon.MessageType.INFO);
                            firstTime = false;
                        }
                    }
                });
                
                tray = SystemTray.getSystemTray();
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("coldspot_16.png"));
                trayIcon = new TrayIcon (imageIcon.getImage(), "Coldspot 1.0");
                trayIcon.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stage.show();
                                tray.remove(trayIcon);
                            }
                        });
                    }
                });
                
                PopupMenu pm = new PopupMenu();
                MenuItem exit = new MenuItem("Exit");
                MenuItem restore = new MenuItem("Restore");
                
                exit.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {                
                            Process stop = Runtime.getRuntime().exec("cmd /c netsh wlan stop hostednetwork");
                        } catch (IOException ex) {
                            JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                        }                        
                        System.exit(0);
                    }
                });
                
                restore.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stage.show();
                                tray.remove(trayIcon);                            
                            }
                        });
                    }
                });
                
                pm.add(exit);
                pm.add(restore);
                trayIcon.setPopupMenu(pm);
                
                try {
                    tray.add(trayIcon);
                } catch (AWTException ex) {
                    JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                }     
            }
        });
    }

    public void promptActivation (){
                try {
                    if (!new File(System.getenv("APPDATA")+"\\Coldspot\\coldspot.certificate").exists()){
                        Parent rootAlert = FXMLLoader.load(getClass().getResource("HotspotAccess.fxml"));
                        accessStage = new Stage();
                        Scene s = new Scene (rootAlert);
                        accessStage.setScene(s);
                        accessStage.setTitle("Activate Coldspot");
                        accessStage.initModality(Modality.APPLICATION_MODAL);
                        accessStage.initStyle(StageStyle.UTILITY);
                        accessStage.setResizable(false);
                        accessStage.toFront();
                        accessStage.centerOnScreen();
                        accessStage.showAndWait();
                        exitApp = true;
                    }
                    else {
                        //verifyIt()
                        Secure secure = new Secure();
                        File key = new File(System.getenv("APPDATA")+"\\Coldspot\\coldspot.certificate");
                        Scanner input = new Scanner(key);
                        String encryptedKeyFromFileAlph = input.nextLine().replaceAll("-", "").trim();
                        String encryptedKeyFromFileNoBase = secure.alphabetsToNo(encryptedKeyFromFileAlph);
                        String encryptedKeyFromFileNoExact = secure.convertFromBaseToBase(encryptedKeyFromFileNoBase, 7, 10);
                        
                        
                        String theKeyNo = secure.serial((System.getenv("computername")+"COLDSPOT").substring(0, 4));
                        if (encryptedKeyFromFileNoExact.equals(theKeyNo)){
                            Platform.setImplicitExit(true);
                            exitApp = false;
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Invalid Key Detected");
                            Parent rootAlert = FXMLLoader.load(getClass().getResource("HotspotAccess.fxml"));
                            accessStage = new Stage();
                            Scene s = new Scene (rootAlert);
                            accessStage.setScene(s);
                            accessStage.setTitle("Activate Coldspot");                            
                            accessStage.initModality(Modality.WINDOW_MODAL);
                            accessStage.initStyle(StageStyle.UTILITY);
                            accessStage.setResizable(false);
                            accessStage.setAlwaysOnTop(true);
                            accessStage.toFront();                            
                            accessStage.showAndWait();
                            accessStage.getScene();      
                            exitApp = true;
                        }
                        
                        if (!new File (System.getenv("APPDATA")+"\\Coldspot\\Elevate.exe").exists()){
                            CopyFiles initFiles = new CopyFiles();
                            initFiles.copyStream(getClass().getResourceAsStream("/hotspot/Elevate.exe"), 
                                    new File (System.getenv("APPDATA")+"\\Coldspot\\Elevate.exe"));
                        }

                    }
                } catch (IOException ex) {
                    JFXAlertDialog.errorDialog("Coldspot", null, "An error occured while activating Coldspot");
                }        
    }    
    
    public static void main(String[] args) {
        launch(args);
    }
}
