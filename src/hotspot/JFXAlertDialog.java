package hotspot;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class JFXAlertDialog {
        public static Optional<ButtonType> confirmDialog(String alertTitle, String alertHeaderText, String alertContentText){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle(alertTitle);
            Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(Coldspot10.class.getResourceAsStream("coldspot.png")));
            alert.setHeaderText(alertHeaderText);
            alert.setContentText(alertContentText);
            return alert.showAndWait();
        }
        public static Optional<ButtonType> messageDialog(String alertTitle, String alertHeaderText, String alertContentText){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(alertTitle);
            Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(Coldspot10.class.getResourceAsStream("coldspot.png")));            
            alert.setHeaderText(alertHeaderText);
            alert.setContentText(alertContentText);
            return alert.showAndWait();
        }
        public static Optional<ButtonType> errorDialog(String alertTitle, String alertHeaderText, String alertContentText){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(alertTitle);
            Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(Coldspot10.class.getResourceAsStream("coldspot.png")));            
            alert.setHeaderText(alertHeaderText);
            alert.setContentText(alertContentText);
            return alert.showAndWait();
        } 
        public static Optional<ButtonType> warningDialog(String alertTitle, String alertHeaderText, String alertContentText){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle(alertTitle);
            Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(Coldspot10.class.getResourceAsStream("coldspot.png")));            
            alert.setHeaderText(alertHeaderText);
            alert.setContentText(alertContentText);
            return alert.showAndWait();
        } 
}
