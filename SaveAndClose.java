package webviewer;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SaveAndClose{

    @FXML private Label saveFileLabel;
    @FXML private Button saveButton;
    @FXML private Button notSaveButton;
    @FXML private Button cancelButton;
    static SaveAndClose obj;
    private byte flag;

    private SaveAndClose(){}


    public static SaveAndClose getInstance(){
        if(obj == null){
            obj = new SaveAndClose();
        }
        return obj;
    }

    public byte wantToSave(String fileName) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("save_and_close.fxml"));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        saveFileLabel.setText("Save file \""+fileName+"\"?");
        saveButton.setOnAction(event -> {flag=1;stage.close();});
        notSaveButton.setOnAction(event -> {flag=2;stage.close();});
        cancelButton.setOnAction(event -> {flag=3;stage.close();});

        stage.setScene(new Scene(root));
        stage.showAndWait();
        return flag;

    }

}
