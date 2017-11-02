package webviewer;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class FindReplace {

    @FXML public TextField searchTerm;
    @FXML public TextField searchTermR;
    @FXML public TextField replaceTerm;
    @FXML public TabPane tabPane;
    @FXML public Label answerLabel;
    @FXML public RadioButton upDirection;
    @FXML public RadioButton upDirectionR;
    @FXML public RadioButton downDirection;
    @FXML public RadioButton downDirectionR;
    @FXML public ToggleGroup replaceRadioGroup;
    @FXML public CheckBox wrapAround;
    @FXML public CheckBox caseSensitive;
    @FXML public CheckBox caseSensitiveR;

    private Stage stage;
    private Controller controller;
    boolean findDirection = true;
    boolean replaceDirection = true;
    boolean isWrapAround = false;
    boolean isCaseSensitive = true;
    boolean isCaseSensitiveR = true;

    public FindReplace(Controller c,int tabIndex) throws IOException {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("find_replace.fxml"));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        downDirection.setSelected(true);
        downDirectionR.setSelected(true);
        wrapAround.setSelected(isWrapAround);
        caseSensitive.setSelected(isCaseSensitive);
        caseSensitiveR.setSelected(isCaseSensitiveR);
        controller = c;
        tabPane.getSelectionModel().select(tabIndex);
        stage.showAndWait();
    }

    public void onFindClicked(){
        String term = searchTerm.getText();
        boolean direction;
        setAnswer("");
        if(tabPane.getSelectionModel().getSelectedIndex() == 0){
            direction = findDirection;
        }else{
            direction = replaceDirection;
        }
        int index = controller.findText(term , direction , isCaseSensitive, isWrapAround);
        if (index == -1){
            setAnswer("Term not found!");
        }else if (index == -2){
            setAnswer("Term cannot be empty");
        }
    }

    public void onFindAllClicked(){
        String term = searchTerm.getText();
        String indexes = controller.findAll(term , isCaseSensitive);
        setAnswer(indexes);
    }

    public void onCountClicked(){
        String term = searchTerm.getText();
        int count = controller.countAppearances(term);
        setAnswer("Count: "+count);
    }

    public void onReplaceClicked(){
        String sTerm = searchTermR.getText();
        String rTerm = replaceTerm.getText();
        controller.replaceText(sTerm,rTerm);
    }

    public void onReplaceAllClicked(){
        String sTerm = searchTermR.getText();
        String rTerm = replaceTerm.getText();
        while(controller.findText(sTerm,true,isCaseSensitiveR , true) != -1){
            controller.replaceText(sTerm,rTerm);
        }

    }

    public void onCloseClicked(){
        stage.close();
    }

    private void setAnswer(String answer){
        answerLabel.setText(answer);
    }


    public void setFindUpDirection(){
        findDirection = false;
    }

    public void setReplaceUpDirection(){
        replaceDirection = false;
    }

    public void setFindDownDirection(){
        findDirection = true;
    }

    public void setReplaceDownDirection(){
        replaceDirection = true;
    }

    public void setWrapAround(){
        isWrapAround = !isWrapAround;
    }

    public void setCaseSensitive(){
        isCaseSensitive = !isCaseSensitive;
    }

    public void setCaseSensitiveR(){
        isCaseSensitiveR = !isCaseSensitiveR;
    }
}
