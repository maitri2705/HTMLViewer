package webviewer;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import sun.font.FontManagerNativeLibrary;

import java.io.IOException;


public class SuggestionBox extends Application{

    private double sceneWidth = -1;
    private double sceneHeight = -1;
    private double letterCounter = 0, rowCount = 1;
    private double sum = 0;
    double charHeight = 17,t=0;
    double xPos;
    double yPos;
    String term,text,beforeText,afterText,backSpaceTerm;
    int currentPos,startPos,listCounter=0,tempCount=0;
    boolean isSpace=false;

    @Override
    public void start(Stage primaryStage) throws Exception{
        ListView listView = new ListView();
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        Button button = new Button("press");
        button.setOnAction(event -> {
            System.out.print(textArea.getText().contains("\n"));
            System.out.print(Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(textArea.getText()));
        });
        StackPane root = new StackPane(textArea , listView);
        root.setPrefSize(500.0,500.0);

        sceneWidth = root.getPrefWidth();
        sceneHeight = root.getPrefHeight();

        ObservableList allItems = FXCollections.observableArrayList();
        allItems.addAll("yash","patel","yk","bye","hey","maitri","nurul","rohan","jaay","anuja","yy","yonyayay","yyyyy");
        ObservableList actualItems=FXCollections.observableArrayList();

        listView.setVisible(false);

    //   listView.setPrefSize(200.0 , 200.0);
        listView.setPrefWidth(200.0);
        listView.setMaxSize(200.0, 200.0);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);


        textArea.textProperty().addListener((observable, oldValue, newValue) ->  {
            if(newValue.charAt(oldValue.length())!= ' ') {
                actualItems.clear();
                listCounter = 0;
                listView.setMaxHeight(200.0);

                currentPos = textArea.getCaretPosition() + tempCount;
                tempCount = 0;
                startPos = currentPos;
                text = textArea.getText();
                System.out.println(startPos);
                while (startPos > 0 && ((text.charAt(startPos) != '\n') || (text.charAt(startPos) != ' '  ))) {
                    //System.out.print(text.charAt(startPos));
                    startPos--;
                }

                //backspace
                if (oldValue.length() > newValue.length()) {
                    if (oldValue.charAt(textArea.getCaretPosition() - 1) == ' ') {
                        System.out.print("m " + term);
                        t = (Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(backSpaceTerm));
                        backSpaceTerm = "";
                    }
                }


                if (!text.isEmpty()) {
                    System.out.println(currentPos+" "+startPos);
                    if (text.charAt(startPos) == ' ')
                        term = text.substring(startPos + 1, currentPos + 1);
                    else
                        term = text.substring(startPos, currentPos + 1);
                }
                String termCopy = "";
                term=term.trim();
                System.out.println(term + " " + term.length());
                if (letterCounter == 0 && !term.isEmpty()) {
                    termCopy = term.substring(term.length() - 1, term.length());
                }
                else {
                    termCopy = term;
                }
                System.out.println(termCopy + " " + termCopy.length());

                for (Object o : allItems) {
                    //  if(((String)o).contains(termCopy)) System.out.print("hi");
                    if (!termCopy.isEmpty() && ((String) o).contains(termCopy)) {
                        //System.out.println(o);
                        actualItems.add(o);
                        listCounter++;
                    }
                }
                listView.setItems(actualItems);
                if (actualItems.size() != 0) {
                    listView.getSelectionModel().selectFirst();
/*                    if(listCounter<8) {
                        listView.setTranslateY(listView.getTranslateY()-(listCounter * 25.0));
                        listView.setMaxHeight(listCounter * 25.0);
                    }*/
                    listView.setVisible(true);
                    //   listView.requestFocus();
                }
           /* else{
                    listView.setVisible(false);
                }*/
            }
        });
        listView.setOnKeyPressed(event -> {
            beforeText="";
            afterText="";
            if(event.getText().matches("\\w+")||event.getCode().equals(KeyCode.SPACE) ){
                textArea.requestFocus();
            }
            /*if(event.getCode().equals(KeyCode.BACK_SPACE)){
                textArea.requestFocus();
              //  System.out.println(textArea.getCaretPosition());
                textArea.positionCaret(textArea.getCaretPosition()-2);
                text=text.substring(0,textArea.getCaretPosition()+1);
                System.out.println(textArea.getCaretPosition()+" "+text);
                if((textArea.getCaretPosition())!=text.length()){
                    text+=text.substring(textArea.getCaretPosition()+1);
                    System.out.println(text);
                }
                textArea.setText(text);
                textArea.positionCaret(text.length());
                textArea.requestFocus();
            }*/

            if(event.getCode().equals(KeyCode.ESCAPE)){
                listView.setVisible(false);
                textArea.requestFocus();
            }
            if(event.getCode().equals(KeyCode.ENTER)){
                term=listView.getSelectionModel().getSelectedItem().toString();

                if(startPos==0){
                    beforeText=listView.getSelectionModel().getSelectedItem().toString();
                }
                else {
                    beforeText = text.substring(0, startPos + 1);
                    //  System.out.println(beforeText);
                    beforeText += listView.getSelectionModel().getSelectedItem().toString();
                }
               // System.out.println(beforeText);
                if(text.length()>textArea.getCaretPosition()){
                    afterText=text.substring(textArea.getCaretPosition(),text.length());
                  //  System.out.println(afterText);
                }
                if(!afterText.isEmpty())
                textArea.setText(beforeText+afterText);
                else
                    textArea.setText(beforeText);
                textArea.positionCaret(beforeText.length());
              //  term="";
                t=Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(term);
                listView.setVisible(false);
               // System.out.println(beforeText);
            }
        });

        textArea.setOnKeyPressed(event -> {

          //  double t = Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(event.getText());
            if ((letterCounter + t) >= 485){
                textArea.appendText("\n");
                listView.setTranslateX(-120);
                rowCount++;
                letterCounter = 0.0;
            }
            letterCounter += t;
//           System.out.println(rowCount+" ");
            xPos=-145  + letterCounter;
               if ((listView.getTranslateX() + 100) < 242.0) {

                    listView.setTranslateX(xPos);
                }

            if (event.getText().matches("\\w+")){
             //   listView.setVisible(false);
                /*textArea.requestFocus();
                textArea.positionCaret(textArea.getCaretPosition());
             //*/ //  listView.setVisible(true);
                t=0;
            }
            if((rowCount*charHeight+listView.getMaxHeight())>484){
             //   System.out.print("exit");
                listView.setTranslateY(-140+(rowCount-1)*charHeight-listView.getMaxHeight()-10);
            }
            else {
                listView.setTranslateY(-140 + (rowCount * charHeight));
            }
            if (event.getCode().equals(KeyCode.ENTER)){
                listView.setVisible(false);
                rowCount++;
                letterCounter = 0.0;
                listView.setTranslateX(-120);
                term="";
            }
            if(event.getCode().equals(KeyCode.SPACE)){
                term+=" ";
                t=Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(term);
                term="";
                listView.setVisible(false);
            }
            if(event.getCode().equals(KeyCode.DOWN)){
                listView.requestFocus();
            }
            if(event.getCode().equals(KeyCode.BACK_SPACE)){

                if(text.length()>1)
                    tempCount=-2;
                else
                    tempCount=-1;
               // backSpaceTerm=event.getCharacter();

             //   textArea.requestFocus();
            }
        });



        primaryStage.show();
    }

    double getStringWidth(String content , int position , Font myFont){
        String before = content.substring(0 , position);
        FontMetrics fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(myFont);
        return fontMetrics.computeStringWidth(before);
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }


}
