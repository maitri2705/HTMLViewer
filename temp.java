package webviewer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by Yash on 15-Sep-16.
 */

public class temp extends Application {

    boolean isBoxActive = false;

    public static void main(String args[]){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        ListView listView = new ListView();
        ObservableList items = FXCollections.observableArrayList();
        ObservableList items2 = FXCollections.observableArrayList();
        items.addAll("yash","patel","yk","bye","hey","maitri","nurul","rohan","jaay","anuja","yy","yonyayay","yyyyy");
        listView.setItems(items);
//        TextArea area = new TextArea();
//        area.setWrapText(true);
        TextField field = new TextField();

        HBox panel = new HBox(listView);
//        panel.setVisible(false);
        field.textProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue != null && !newValue.isEmpty()){
                items2.clear();
                //System.out.println("size"+items2.size());
                for (Object o : items){
                    if(((String) o).contains(newValue)) {
                        //System.out.println(o);
                        items2.add(o);
                    }
                }
                //System.out.println();
                listView.setItems(items2);
                if (items2.size() != 0){
                    listView.getSelectionModel().selectFirst();
                    isBoxActive = true;
                    panel.setVisible(true);
                }
            }else
                items2.clear();
        });
        listView.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE) && isBoxActive){
                isBoxActive = false;
                items2.clear();
                panel.setVisible(false);
                field.requestFocus();
                field.deselect();
                field.positionCaret(field.getText().length());
            }
            if (isBoxActive && event.getText().matches("\\w+")){
                //field.setText(field.getText() + event.getText());
                field.requestFocus();
                field.deselect();
                field.positionCaret(field.getText().length());
            }
            if (isBoxActive && event.getCode().equals(KeyCode.BACK_SPACE)){
                field.setText(field.getText().substring(0,field.getText().length()-1));
                field.requestFocus();
                field.deselect();
                field.positionCaret(field.getText().length());
            }
            if (event.getCode().equals(KeyCode.ENTER) && isBoxActive ){
                isBoxActive = false;
                String selectedItem = (String) listView.getSelectionModel().getSelectedItem();
                field.setText(selectedItem);
                field.requestFocus();
                field.deselect();
                field.positionCaret(selectedItem.length());
                items2.clear();
                panel.setVisible(false);
            }
        });
        field.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE) && isBoxActive){
                isBoxActive = false;
                items2.clear();
                field.requestFocus();
                panel.setVisible(false);
            }
            if (isBoxActive && event.getCode().equals(KeyCode.UP) || event.getCode().equals(KeyCode.DOWN)){
                listView.requestFocus();
            }
        });
        StackPane root = new StackPane(field,panel);
        primaryStage.setScene(new Scene(root,500,350));
        primaryStage.show();
        System.out.println(listView.getTranslateX());
    }
}

        //listView.setItems(items);
        /*listView.setVisible(false);
        listView.setMaxHeight(100);
        listView.setMaxWidth(100);
        StackPane root = new StackPane(area , listView);

        listView.setItems(items);
        listView.setTranslateY(-120);
        listView.setTranslateX(-190);


        //area.setStyle("-fx-line-s : 100px;");
        //listView.setTranslateX(listView.getLayoutX());
        //System.out.println(listView.getLayoutX());

//        System.out.println(area.getFont().getSize());
        area.textProperty().addListener((observable -> {
            int i = area.getCaretPosition();
            int temp = i;
            if(temp >= area.getText().length()){
                temp-=2;
            }
//            System.out.println("a "+temp);
            while (temp >= 0){
                if (area.getText().charAt(temp) == ' '){
                    break;
                }
                temp--;
            }
//            listView.setTranslateY(-175);
//            System.out.println("tr0"+listView.getTranslateX());
            double a;
            if ((a = -190 + new Text(area.getText()).getLayoutBounds().getWidth()) < 192.0)
            listView.setTranslateX(a);
//            listView.setTranslateY(-120 + 10);
//            System.out.println("tr"+listView.getTranslateX());

            listView.setVisible(true);
//            System.out.println("i "+temp);
        }));

        area.setOnKeyPressed(event -> {
            System.out.println("y"+listView.getTranslateY());
            System.out.println("x"+listView.getTranslateX());
            if (event.getCode().equals(KeyCode.ENTER)){
//                System.out.println(listView.getTranslateX());
                listView.setTranslateX((-190 + listView.getTranslateX()));
                listView.setTranslateY(listView.getTranslateY() + 17);
                if (listView.getTranslateY() > 135.0){

                    listView.setTranslateX(-190.0);
                    listView.setTranslateY(135.0);
                }
            }
            if (event.getCode().equals(KeyCode.BACK_SPACE)){
                if (listView.getTranslateX() == -190.0){
                    listView.setTranslateY(listView.getTranslateY() - 17);
                }
                if (listView.getTranslateY() < -120.0){
                    listView.setTranslateX(-190.0);
                    listView.setTranslateY(-120.0);
                }
            }
            if (event.getText().matches("\\w+")){

                if (listView.getTranslateX() > 190.0){
                    System.out.println("a");
                    listView.setTranslateX(-100.0);
                }
            }
        });
*/

        //listView.setLayoutY(150);
