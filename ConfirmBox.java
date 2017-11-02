package webviewer;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static boolean result;

    public static boolean choice(String title,String msg){
        Stage window=new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        Label label=new Label(msg);
        Button button1=new Button("Exit");
        button1.setOnAction(e->{
            result=true;
            window.close();
        });

        Button button2=new Button("Cancel");
        button2.setOnAction(e->{
            result=false;
            window.close();
        });

        VBox box=new VBox(20);
        box.setAlignment(Pos.CENTER);

        HBox box2=new HBox(15);
        box2.setAlignment(Pos.CENTER);
        box2.getChildren().addAll(button1,button2);

        box.getChildren().addAll(label,box2);
        Scene scene=new Scene(box,300,100);
        window.setScene(scene);
        window.showAndWait();
        return result;
    }
}
