/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotspot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Mugambbo
 */
public class Activity extends Service<String> {
    
    private final String networkName;
    private final String password;
    private final boolean isStarted;
    private String line;
    private int a;
    private StringBuilder sb;
    String elevater;
    
    public Activity (String networkName, String password, boolean isStarted){
//        elevater = "C:/Users/Mugambbo/Desktop";
        elevater = System.getenv("APPDATA")+"\\Coldspot\\";
        this.networkName = networkName;
        this.password = password;
        this.isStarted = isStarted;
    }
    private boolean verify (){
        return networkName.length() >= 3 && networkName.length() <= 32 && password.length() >= 8 && password.length() <= 32;
    }
    
    private String setupNetwork () {
        sb = new StringBuilder ();
        boolean verify = verify();
                 
        
        if (verify == true && isStarted==true){                             
            try {
                File file = new File ("C:/Program Files/Coldspot/hotspot.test");
                file.mkdir();
                file.deleteOnExit();
                FileWriter fw = new FileWriter(file);
                fw.write(" ");
                theSetupNotElevated();
            }
            catch (IOException err){
                try {                   
                    theSetupElevated();
                }
                catch (IOException | InterruptedException er){
                    JFXAlertDialog.errorDialog("Coldspot", null, "Unable to establish network in elevated mode.");
                }
            }
            catch (InterruptedException err){
                JFXAlertDialog.errorDialog("Coldspot", null, "The process was interrupted. Unable to setup the connection.");                
            }
        }
        
        else if (!verify && isStarted){
            JFXAlertDialog.errorDialog("Coldspot", null, "Please ensure that the network name and password are correct.");                
        }
        else if (!isStarted){
            try{
                theStop();
            }
            catch (IOException | InterruptedException err){
                JFXAlertDialog.errorDialog("Coldspot", null, err.getMessage());                                
            }
        }
        
        return sb.toString();
    }
    
    private void theSetupElevated () throws IOException, InterruptedException{  
        Process setElevated = Runtime.getRuntime().exec(elevater+"\\Elevate.exe -wait -k prog netsh wlan set hostednetwork mode=allow ssid=\""+networkName+"\" key=\""+password+"\" & cls & exit");
                a = setElevated.waitFor();               
        Process startElevated = Runtime.getRuntime().exec(elevater+"\\Elevate.exe -wait -k prog netsh wlan start hostednetwork & cls & exit");            
                a = startElevated.waitFor();                       
        Process showElevated = Runtime.getRuntime().exec("cmd /c netsh wlan show hostednetwork");            
                a = showElevated.waitFor();  
        BufferedReader bre;
        try (BufferedReader bri = new BufferedReader(new InputStreamReader(showElevated.getInputStream()))) {
            bre = new BufferedReader(new InputStreamReader(showElevated.getErrorStream()));
            while ((line=bri.readLine())!=null){
                sb.append(line).append("\n");
            }
            while ((line = bre.readLine())!=null){
                sb.append(line).append("\n");
            }
        }
                bre.close();  

                
    }
    
    
    private void theSetupNotElevated () throws IOException, InterruptedException{
        Process setElevated = Runtime.getRuntime().exec("cmd /c netsh wlan set hostednetwork mode=allow ssid=\""+networkName+"\" key=\""+password+"\"");
                a = setElevated.waitFor();               
        Process startElevated = Runtime.getRuntime().exec("cmd /c netsh wlan start hostednetwork");            
                a = startElevated.waitFor();               
        Process showElevated = Runtime.getRuntime().exec("cmd /c netsh wlan show hostednetwork");                    
                a = showElevated.waitFor();               
        BufferedReader bre;
        try (BufferedReader bri = new BufferedReader(new InputStreamReader(showElevated.getInputStream()))) {
            bre = new BufferedReader(new InputStreamReader(showElevated.getErrorStream()));
            while ((line=bri.readLine())!=null){
                sb.append(line).append("\n");
            }

            while ((line = bre.readLine())!=null){
                sb.append(line).append("\n");                  
            }
        }
                bre.close();  

    }
    
    private void theStop () throws IOException, InterruptedException{
        Process stop = Runtime.getRuntime().exec("cmd /c netsh wlan stop hostednetwork");
                a = stop.waitFor();               
        BufferedReader bre;
        try (BufferedReader bri = new BufferedReader(new InputStreamReader(stop.getInputStream()))) {
            bre = new BufferedReader(new InputStreamReader(stop.getErrorStream()));
            while ((line=bri.readLine())!=null){
                sb.append(line).append("\n");
            }

            while ((line = bre.readLine())!=null){
                sb.append(line).append("\n");                  
            }
        }
                bre.close();  

    }
    

    

    @Override
    protected Task<String> createTask() {
        return new Task<String>(){
            @Override
            protected String call() {//start method call              
                    String val = setupNetwork();                
                return val;
            }//end method call
            
        }; //end new instance of Task
    } //end method createTask
}
