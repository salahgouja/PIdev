package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ReponseDialogController {
    @FXML
    private TextArea responseTextArea;

    private Stage dialogStage;
    private boolean responseSaved = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isResponseSaved() {
        return responseSaved;
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        // Handle saving the response
        CardController.reponse = responseTextArea.getText();
        responseSaved = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        // Handle cancel action
        dialogStage.close();
    }
}
