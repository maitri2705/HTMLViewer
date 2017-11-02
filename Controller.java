package webviewer;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class Controller implements Initializable{


    //FXML IDs
    @FXML private TabPane tabPane;
    @FXML private Label status;
    @FXML private BorderPane window;
    @FXML private Menu windowMenu;
    @FXML private ListView listView;

    //Controller variables
    private String textToPaste;
    private final ContextMenu contextMenu = new ContextMenu();
    private Vector<TabData> tabs;
    private Vector<String> openFiles;
    private int activeTabIndex = 0;
//    private Logger logger = Logger.getLogger("logger");

    private CheckMenuItem item;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabs = new Vector<>();
        openFiles = new Vector<>();
        openNewTab();
        listView.setVisible(false);
        //keyboard shortcuts
        //new tab
        KeyCombination new_tab_comb = new KeyCodeCombination(KeyCode.N,KeyCombination.CONTROL_DOWN);
        KeyCombination open_comb = new KeyCodeCombination(KeyCode.O,KeyCombination.CONTROL_DOWN);
        KeyCombination save_comb = new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN);
        KeyCombination nextTab_comb = new KeyCodeCombination(KeyCode.TAB,KeyCombination.CONTROL_DOWN);
        KeyCombination prevTab_comb = new KeyCodeCombination(KeyCode.TAB,KeyCombination.SHIFT_DOWN,KeyCombination.CONTROL_DOWN);
        KeyCombination closeTab_comb = new KeyCodeCombination(KeyCode.W,KeyCombination.CONTROL_DOWN);


        window.setOnKeyPressed(event -> {
            if(new_tab_comb.match(event)){
                openNewTab();
            }
            if(open_comb.match(event)){
                onOpenClicked();
            }
            if(save_comb.match(event)){
                onSaveClicked();
            }
            if(nextTab_comb.match(event)){
                openNextTab();
            }
            if(prevTab_comb.match(event)){
                openPrevTab();
            }
            if(closeTab_comb.match((event))){
                closeCurrentTab();
            }
        });

        ((CheckMenuItem)windowMenu.getItems().get(activeTabIndex)).setSelected(true);

        tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int newV = (int) newValue;
                int oldV = (int) oldValue;
                activeTabIndex =  newV;
                updateActiveTabIndex();
                updateWindowMenu();
                //logger.info(Integer.toString(newV));
            }
        });


    }

    //-------------------------------------------------------TOOL BAR---------------------------------------------------------------
    //run button
    public void onRunClicked(){

        TextArea currentEditor = getCurrentEditor() ;
        if(currentEditor != null){
            String content = currentEditor.getText();
            if(!content.equals("")){
                new OutputWindow(openFiles.get(activeTabIndex)).run(content);
            }else {
                setStatus("Empty file");
            }
        }

    }

    //switch button
    public void onSwitchClicked(){
        openNextTab();
    }

    //----------------------------FILE MENU------------------------------
    //new clicked
    public void onNewClicked(){
        openNewTab();
    }

    //open clicked
    public void onOpenClicked(){
        FileChooser choose = new FileChooser();
        choose.setInitialDirectory(new File(System.getProperty("user.dir")));
        choose.setTitle("Open file");
        choose.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("HTML files (*.html)","*.html")
        );

        File selectedFile = choose.showOpenDialog(null);

        if(selectedFile != null)
            openNewTab(selectedFile);
    }


    //save file
    public void onSaveClicked(){
        FileChooser saver = new FileChooser();
        saver.setInitialDirectory(new File(System.getProperty("user.dir")));
        saver.setTitle("Save file");
        saver.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("HTML files (*.html)","*.html")
        );
        File file = saver.showSaveDialog(null);
        if(file != null){
            try{
                FileWriter fw = new FileWriter(file);
                fw.write(getCurrentEditor().getText());
                TabData selectedTab = tabs.get(activeTabIndex);
                openFiles.remove(activeTabIndex);
                openFiles.add(activeTabIndex,file.getName());
                selectedTab.setSaved(true);
                selectedTab.setFileName(file.getName());
                selectedTab.setText(selectedTab.getFileName());
                fw.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //close file
    public void onCloseClicked(){
        boolean confirm = ConfirmBox.choice("Confirm Exit","Are you sure you want to exit?");
        if(confirm){
            Stage stage = (Stage) tabPane.getScene().getWindow();
            stage.close();
        }

    }

    //----------------------------EDIT MENU--------------------------------
    //copy item
    public void onCopyClicked(){
        String copiedText = getCurrentEditor().getSelectedText();
        if(copiedText.equals("")) return;
        textToPaste = copiedText;
    }

    //cut item
    public void onCutClicked(){
        String cutText = getCurrentEditor().getSelectedText();
        int caret = getCurrentEditor().getCaretPosition();
        textToPaste = cutText;
        getCurrentEditor().deleteText(caret - cutText.length(),caret);

    }

    //paste item
    public void onPasteClicked(){
        int caret = getCurrentEditor().getCaretPosition();
        getCurrentEditor().insertText(caret,textToPaste);
    }

    //delete item
    public void onDeleteClicked(){
        String cutText = getCurrentEditor().getSelectedText();
        if(cutText.equals("")) return;
        int caret = getCurrentEditor().getCaretPosition();
        getCurrentEditor().deleteText(caret - cutText.length(),caret);
    }

    //-----------------------------SEARCH MENU-------------------------------

    //find term
    public void onFindClicked(){
        try {
            FindReplace fr = new FindReplace(this,0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //replace term
    public void onReplaceClicked(){
        try {
            FindReplace fr = new FindReplace(this,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  boolean replaceText(String oldText , String newText){
        return tabs.get(activeTabIndex).replaceText(oldText,newText);
    }

    public int findText(String text , boolean dir , boolean caseSensitive , boolean wrapAround){
        return tabs.get(activeTabIndex).findText(text , dir , caseSensitive, wrapAround);
    }

    public int countAppearances(String text){
        return tabs.get(activeTabIndex).countAppearances(text);
    }

    public String findAll(String text , boolean caseSensitive){
        return tabs.get(activeTabIndex).findAll(text , caseSensitive);
    }


    //----------------------------window menu-------------------------------
    private void updateWindowMenu(){
        windowMenu.getItems().clear();
        for(String fileName : openFiles){
            item = new CheckMenuItem(fileName);
            item.setSelected(false);
            if(!checkIfExistsInWindowMenu(fileName)){
                item.setOnAction(event -> openExistingTab(((MenuItem) event.getSource()).getText()));
                windowMenu.getItems().add(item);
            }
        }
        //logger.info("a"+Integer.toString(activeTabIndex));
        if(activeTabIndex != -1)
        ((CheckMenuItem)windowMenu.getItems().get(activeTabIndex)).setSelected(true);

    }

    private boolean checkIfExistsInWindowMenu(String fileName){

        ObservableList<MenuItem> list = windowMenu.getItems();

        for(MenuItem item : list){
            if(item.getText().equals(fileName)){
                return true;
            }
        }

        return false;

    }

    //ones blank tab
    private void openNewTab(){
        TabData newTab = new TabData(getNewTabName(),listView);
        tabPane.getTabs().add(newTab);
        tabs.add(newTab);
        openFiles.add(newTab.getFileName());
        int totalTabs = tabPane.getTabs().size();
        tabPane.getSelectionModel().select(totalTabs - 1);
        updateActiveTabIndex();
        updateWindowMenu();
//        logger.info(newTab.getFileName() + " "+ openFiles);
    }


    //opens new tab with file
    private void openNewTab(File file){

        String title = file.getName();
        if(openFiles.contains(title)){
            openExistingTab(title);
        }else{
            //logger.info(title + " " + openFiles);
            String text = "";
            String temp;
            try {
                BufferedReader bf = new BufferedReader(new FileReader(file));
                while ((temp = bf.readLine()) != null){
                    text += temp + "\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            TabData newTab = new TabData(title,text,listView);

            MenuItem cut = new MenuItem("Cut");
            cut.setOnAction(event -> onCutClicked());

            MenuItem copy = new MenuItem("Copy");
            copy.setOnAction(event -> onCopyClicked());

            MenuItem paste = new MenuItem("Paste");
            paste.setOnAction(event -> onPasteClicked());

            MenuItem delete = new MenuItem("Delete");
            delete.setOnAction(event -> onDeleteClicked());

            contextMenu.getItems().addAll(cut,copy,paste,delete);
            newTab.setContextMenu(contextMenu);

            tabPane.getTabs().add(newTab);
            tabs.add(newTab);
            openFiles.add(title);
            int totalTabs = tabPane.getTabs().size();
            tabPane.getSelectionModel().select(totalTabs - 1);
            updateActiveTabIndex();
            updateWindowMenu();

        }

    }

    private void openExistingTab(String title) {

        activeTabIndex = openFiles.indexOf(title);
        tabPane.getSelectionModel().select(activeTabIndex);
        updateActiveTabIndex();
        updateWindowMenu();
    }

    //opens next tab
    private void openNextTab(){
//        logger.info("openNextTab");
        int totalTabs = tabs.size();
        int currentTab = activeTabIndex;
        tabPane.getSelectionModel().select((currentTab + 1) % totalTabs);
        updateActiveTabIndex();
//        tabPane.getSelectionModel().getSelectedItem().getContent().requestFocus();
    }

    //opens previous tab
    private void openPrevTab(){
//        logger.info("openPrevTab");
        int totalTabs = tabs.size();
        int currentTab = activeTabIndex;
        tabPane.getSelectionModel().select(((currentTab - 1)+ totalTabs ) % totalTabs);
        updateActiveTabIndex();
//        logger.info(Integer.toString(((currentTab - 1)+ totalTabs ) % totalTabs));
//        tabPane.getSelectionModel().getSelectedItem().getContent().requestFocus();


    }

    private void closeCurrentTab(){
        TabData selectedTab = tabs.get(activeTabIndex);

//        logger.info(selectedTab.getFileName() + " " + openFiles+" "+openFiles.size() + " " + selectedTab);

        if (!selectedTab.isSaved()) {
            try {
                byte isAnswerTrue = SaveAndClose.getInstance().wantToSave(openFiles.get(activeTabIndex));
                if (isAnswerTrue == 1) {
                    onSaveClicked();
                }else if(isAnswerTrue == 3){
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        openFiles.remove(activeTabIndex);
        tabs.remove(activeTabIndex);
        tabPane.getTabs().remove(activeTabIndex);
        System.out.println("active "+activeTabIndex);
        updateActiveTabIndex();
        updateWindowMenu();
        /*logger.info(Integer.toString(tabPane.getTabs().size()));
        logger.info(tabPane.getTabs().toString());*/
        window.requestFocus();
    }

    private void updateActiveTabIndex() {

        activeTabIndex = tabPane.getSelectionModel().getSelectedIndex();
        //logger.info("b"+Integer.toString(activeTabIndex));
    }

    private String getNewTabName(){
        int count = 1;
        while(true){
            if(!openFiles.contains("new "+count)){
                return "new "+count;
            }
            count++;
        }
    }

    //returns the selected tabs editor
    private TextArea getCurrentEditor(){
        return (TextArea)tabs.get(activeTabIndex).getContent();
    }


    //sets status on status bar
    private void setStatus(String status){
        this.status.setText(status);
    }

}
