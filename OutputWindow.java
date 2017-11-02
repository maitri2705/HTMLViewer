package webviewer;


import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class OutputWindow {

    private WebView webView;
    private WebEngine engine;

    OutputWindow(String title){
        Stage stage = new Stage();
        webView = new WebView();
        stage.setScene(new Scene(new AnchorPane(webView)));
        stage.setTitle("Output for \""+title +"\"");
        stage.show();
    }

    public void run(String content){
        engine = webView.getEngine();
        engine.loadContent(content);
    }

}
