package webviewer;

import com.sun.javafx.tk.Toolkit;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by MAITRI SHAH on 31/01/2017.
 */
public class SuggestionBoxUpdate extends Application{
    private double letterCounter = 0, rowCount = 1;
    double charHeight = 15.96,tempAddition=0;
    double xPos=-140;
    String term="",text,beforeText,afterText,backSpaceTerm;
    int currentPos,startPos,listCounter=0,tempCount=0,i;
    boolean isSpace=false,hasEnter=false;

    public void start(Stage primaryStage) throws Exception {
        ListView listView = new ListView();
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        StackPane root = new StackPane(textArea , listView);
        root.setPrefSize(500.0,500.0);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        ObservableList allItems = FXCollections.observableArrayList();
        allItems.addAll("yash","patel","yk","y","yy","yyy","yyyy","yyyyyyy","bye","hey","maitri","nurul","rohan","jaay","anuja","yy","yonyayay","yyyyy");

        ObservableList actualItems=FXCollections.observableArrayList();

        listView.setVisible(false);
        listView.setMaxSize(200.0, 200.0);



       /* textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.charAt(oldValue.length())!= ' ') {
                actualItems.clear();
                listCounter = 0;
                listView.setMaxHeight(200.0);
                System.out.println("Space term"+term+"end");
                text=textArea.getText();
                for (Object o : allItems) {
                    if (!term.isEmpty() && ((String) o).contains(term)){
                        actualItems.add(o);
                        letterCounter++;
                    }
                }
                if(actualItems.size()!=0){
                    listView.setItems(actualItems);
                    listView.getSelectionModel().selectFirst();
                    listView.setVisible(true);
                }

            }
        });*/


        listView.setOnKeyPressed(event -> {
            text=textArea.getText();
            beforeText="";
            afterText="";
            if(event.getText().matches("\\w+")||event.getCode().equals(KeyCode.SPACE) ){
                textArea.requestFocus();
            }
            if(event.getCode().equals(KeyCode.ESCAPE)){
                listView.setVisible(false);
                textArea.requestFocus();
            }
            if(event.getCode().equals(KeyCode.SPACE)){
                System.out.println("Term is="+term);
                tempAddition=Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(term);
                term="";
                listView.setVisible(false);
            }

            if(event.getCode().equals(KeyCode.ENTER)){

                if(!isSpace && !hasEnter){
                    term=listView.getSelectionModel().getSelectedItem().toString();
                    beforeText=term;
                }

                else {
                    beforeText = text.substring(0, (textArea.getCaretPosition()-term.length()));
                    term=listView.getSelectionModel().getSelectedItem().toString();
                  //  System.out.println("beforre"+beforeText);
                    beforeText += term;
                }
                if(text.length()>textArea.getCaretPosition()){
                    afterText=text.substring(textArea.getCaretPosition(),text.length());
                }

                if(!afterText.isEmpty())
                    textArea.setText(beforeText+afterText);
                else
                    textArea.setText(beforeText);

                textArea.positionCaret(beforeText.length());
              //  tempAddition=Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(term);
                listView.setVisible(false);
                textArea.requestFocus();
            }
        });

        textArea.setOnMouseClicked(event -> {

        });
        textArea.setOnKeyPressed(event -> {
            text=textArea.getText();
            if ((letterCounter + tempAddition ) >= 485){
                hasEnter=true;
                textArea.appendText("\n");
                xPos=-120;
                rowCount++;
                letterCounter = 0.0;
                charHeight+=0.05;
            }
           /* if(!isSpace) {
               // System.out.print(textArea.getScrollTop());
                listView.setTranslateX(-140);
                System.out.print("row"+rowCount);
                if(rowCount>16)
                    listView.setTranslateY(-140-220+ (rowCount * charHeight));
                else
                listView.setTranslateY(-140+ (rowCount * charHeight));
            }*/
            //listView.setTranslateX(xPos);

        //    if ((listView.getTranslateX() + 100) < 242.0) {
                listView.setTranslateX(xPos);
                if(rowCount>16)
                    listView.setTranslateY(-140-220+ (rowCount * charHeight));
                else
                    listView.setTranslateY(-140+ (rowCount * charHeight));
         //   }

            if (event.getText().matches("\\w+")){
          //      tempAddition=0;

                currentPos=textArea.getCaretPosition();
                //  if(hasEnter){
                startPos = currentPos;
                for(i=(textArea.getCaretPosition()-1);i>0;i--){
                    if(text.charAt(i)!='\n' && startPos!=0) startPos--;
                    else break;
                }
                //       term=text.substring(startPos,currentPos-1);
                // }
              /*  else{
                   startPos=0;
               }*/
                System.out.println("start"+startPos+"Current"+currentPos+"pos"+text.substring(startPos,currentPos)+"end");
                tempAddition = Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(text.substring(startPos,currentPos)+" ");
                letterCounter = tempAddition;
                listView.setTranslateY(-140 + (rowCount * charHeight));
                xPos = -140 + letterCounter;

                term = "";

                term+=event.getText();
                System.out.print("the term is"+term+"end");
                actualItems.clear();
                listCounter = 0;
                listView.setMaxHeight(200.0);
                System.out.println("Space term"+term+"end");
                for (Object o : allItems) {
                    if (!term.isEmpty() && ((String) o).contains(term)){
                        actualItems.add(o);
                        listCounter++;
                    }
                }
                if(actualItems.size()!=0){
                    listView.setItems(actualItems);
                    listView.getSelectionModel().selectFirst();
                    listView.setVisible(true);
                }
                // listView.setVisible(true);
            }

            if(event.getCode().equals(KeyCode.SPACE)){
                isSpace = true;
                term+=" ";
                //   System.out.println(Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth());
                if(term.isEmpty()) {
                    term+=" ";
                }
                System.out.println("Term is" + term + "end");
                //tempAddition = Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(term);
              currentPos=textArea.getCaretPosition();
             if(hasEnter){
                   startPos = currentPos;
                   for(i=(textArea.getCaretPosition()-1);i>0;i--){
                       if(text.charAt(i)!='\n' && startPos!=0) startPos--;
                       else break;
                   }
            //       term=text.substring(startPos,currentPos-1);
              // }
              // else{
                   startPos=0;
               }
                System.out.println("start"+startPos+"Current"+currentPos+"pos"+text.substring(startPos,currentPos)+"end");
                tempAddition = Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(text.substring(startPos,currentPos)+" ");
                letterCounter = tempAddition;
                listView.setTranslateY(-140 + (rowCount * charHeight));
                xPos = -140 + letterCounter;

                term = "";
                listView.setVisible(false);
            }

            if(event.getCode().equals(KeyCode.DOWN )){
                listView.requestFocus();
            }

            if(event.getCode().equals(KeyCode.ENTER )){
                System.out.println("le"+letterCounter+"term="+term);
                rowCount++;
                charHeight+=0.05;
                letterCounter = 0;
                xPos=-140;
                term="";
                listView.setVisible(false);
                hasEnter=true;

            }

            if(event.getCode().equals(KeyCode.BACK_SPACE)){
                listView.setVisible(false);
                startPos=textArea.getCaretPosition();
                currentPos=startPos;
                for(i=(textArea.getCaretPosition()-1);i>0;i--){
                    if(text.charAt(i)!=' ') startPos--;
                    else break;
                }
                term=text.substring(startPos,currentPos-1);
                System.out.println("backSpace"+term+"end");
            }
        });
        primaryStage.show();
    }
}
