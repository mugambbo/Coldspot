package hotspot;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

/**
 *
 * @author Mugambbo
 */
public class HotspotAboutController implements Initializable {
    @FXML private ImageView tweetAbout;    
    @FXML private ImageView mailAbout;    
    
//    @FXML
//    void tweet (ActionEvent evt){
//        if (Desktop.isDesktopSupported()){
//            try {
//                Desktop.getDesktop().browse(new URL ("http://www.twitter.com/mugambbo").toURI());
//            } catch (MalformedURLException | URISyntaxException ex) {
//                Logger.getLogger(HotspotAboutController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(HotspotAboutController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
    
//    @FXML
//    void mail (ActionEvent evt){
//        if (Desktop.isDesktopSupported()){
//            try {
//                Desktop.getDesktop().mail(new URI("to:mugambbo@gmail.com", "subject:Coldspot User", 
//                        "body: Hi. Just want to inform you about my love for coldspot!"));
//            } catch (MalformedURLException | URISyntaxException ex) {
//                Logger.getLogger(HotspotAboutController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(HotspotAboutController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tweetAbout.setOnMouseClicked(new EventHandler(){

            @Override
            public void handle(Event event) {
                if (Desktop.isDesktopSupported()){
                    try {
                        Desktop.getDesktop().browse(new URL ("http://www.twitter.com/mugambbo").toURI());
                    } catch (MalformedURLException | URISyntaxException ex) {
                        JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                    } catch (IOException ex) {
                        JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                    }
                }
            }
        });
        mailAbout.setOnMouseClicked(new EventHandler(){

            @Override
            public void handle(Event event) {
                if (Desktop.isDesktopSupported()){
                    try {
                        URI uri = URI.create("mailto:mugambbo@gmail.com?subject=Coldspot");                        
                        Desktop.getDesktop().mail(uri);
                    } catch (MalformedURLException ex) {
                        JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                    } catch (IOException ex) {
                        JFXAlertDialog.errorDialog("Coldspot", null, ex.getMessage());
                    }
                }
            }
        });
    }
    
}
