package webviewer;


import com.sun.javafx.tk.Toolkit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;


public class TabData extends Tab{
    private boolean isSaved;
    private String fileName;
    private  TextArea textArea;
    int startpos,currentpos;
    int prevCounter = -1;

    private String textData,tag;

    //    @FXML ListView listView;
    //suggestion box properties
    private double letterCounter = 0, rowCount = 1;
    double charHeight = 23,tempAddition=0;
    double xPos=-200;
    String term="",text,beforeText,afterText,backSpaceTerm;
    int currentPos,startPos,listCounter=0,tempCount=0,i;
    boolean isSpace=false,hasEnter=false;
    ListView listView;

    public TabData(String title,String data,ListView listView){
        this(title,new TextArea(data),listView);
        isSaved = false;
    }


    public TabData(String title,ListView listView){
        this(title,new TextArea(),listView);
        isSaved = false;
    }

    public TabData(String title, Node content,ListView list)
    {
        super(title,content);
        textArea = (TextArea) content;
        textArea.setWrapText(true);

//        listView = new ListView();
        listView = list;
        listView.setVisible(false);
//        listView.setVisible(false);
        listView.setMaxSize(200.0, 200.0);

        ObservableList allItems = FXCollections.observableArrayList();
        ObservableList actualItems = FXCollections.observableArrayList();
        allItems.addAll("<!DOCTYPE>","<a>","<abbr>","<address>","<area>","<article>","<aside>","<audio>","<b>","<base>","<bdi>",
                "<bdo>","<blockquote>","<body>","<br>","<button>","<canvas>","<caption>","<cite>","<code>","<col>","<colgroup>","<command>",
                "<datalist>","<dd>","<del>","<details>","<dfn>","<div>","<dl>","<dt>","<em>","<embed>","<fieldset>","<figcaption>",
                "<figure>","<footer>","<form>","<h1>","<h2>","<h3>","<h4>","<h5>","<h6>","<head>","<header>","<hgroup>","<hr>","<html>",
                "<i>","<iframe>","<img>","<input>","<ins>","<kbd>","<keygen>","<label>","<legend>","<li>","<link>","<map>","<mark>","<menu>",
                "<meta>","<meter>","<noscript>","<object>","<ol>","<optgroup>","<option>","<output>","<p>","<param>","<pre>","<progress>","<q>",
                "<rp>","<rt>","<ruby>","<s>","<samp>","<script>","<section>","<select>","<small>","<source>","<span>","<strong>","<style>",
                "<sub>","<summary>","<sup>","<table>","<tbody>","<td>","<textarea>","<tfoot>","<th>","<thead>","<time>","<title>","<tr>","<track>",
                "<u>","<ul>","<var>","<video>","<wbr>");

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
                tempAddition= Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(term);
                term="";
                listView.setVisible(false);
            }

            if(event.getCode().equals(KeyCode.ENTER)){
                if(!isSpace && !hasEnter){
                    term=listView.getSelectionModel().getSelectedItem().toString();
                    beforeText=term;
                }

                else {
                    System.out.println("CurrentPos="+textArea.getCaretPosition()+" Len="+term.length()+" term="+term);
                    beforeText = text.substring(0, (textArea.getCaretPosition()-1-term.length()));
                    term=listView.getSelectionModel().getSelectedItem().toString();
                    System.out.println("beforre"+beforeText);
                    beforeText += term;
                }
                if(text.length()>textArea.getCaretPosition()){
                    afterText=text.substring(textArea.getCaretPosition(),text.length());
                }
                System.out.println("BeforeText="+beforeText+" AfterText="+afterText);
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
            listView.setVisible(false);
            text = textArea.getText();

            //To define term
            currentPos = textArea.getCaretPosition();
            startPos = currentPos;
            for (i = (textArea.getCaretPosition() - 1); i >= 0; i--) {
                if (text.charAt(i) != ' ' && startPos != -1) startPos--;
                else break;
            }
            //  System.out.println("Stsrt"+startPos);
            term = text.substring(startPos, currentPos);
            System.out.println("Clicked" + term + "end");

            //to change Xpos
            currentPos = startPos;
            startPos = currentPos;
            for (i = (textArea.getCaretPosition() - 1); i > 0; i--) {
                if (text.charAt(i) != '\n' && startPos != 0) {
                    startPos--;
                }
                else break;
            }
            System.out.println("Stsrt"+startPos+"current"+currentPos);
            letterCounter = Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(text.substring(startPos, currentPos));

            //To count rowcount of suggestion box
            if (hasEnter)
            {
                rowCount = 1;
                for (i = (textArea.getCaretPosition() - 1); i > 0; i--) {
                    if (text.charAt(i) == '\n') {
                        rowCount++;
                    }
                    else break;
                }
            }
        });

        textArea.setOnKeyPressed(event -> {

            textArea.setWrapText(true);
            //after completion of line
            if ((letterCounter + tempAddition ) >= 485){
                hasEnter=true;
                textArea.appendText("\n");
                xPos=-200;
                rowCount++;
                letterCounter = 0.0;
                charHeight+=0.05;
            }

            //to set position of suggestion box
            listView.setTranslateX(xPos);
            if(rowCount>16)
                listView.setTranslateY(-45-220+ (rowCount * charHeight));
            else
                listView.setTranslateY(-45+(rowCount * charHeight));

            if (event.getText().matches("\\w+")){
                tempAddition=0;
                term+=event.getText();
                System.out.println("the term is"+term+"end");

                //add items in suggestion box
                actualItems.clear();
                listCounter = 0;
                listView.setMaxHeight(200.0);
                //System.out.println("Space term"+term+"end");
                for (Object o : allItems) {
                    if (!term.isEmpty() && ((String) o).contains(term)){
                        actualItems.add(o);
                        listCounter++;
                    }
                }
                if(actualItems.size()!=0){
                    listView.setItems(actualItems);
                    listView.getSelectionModel().selectFirst();
                    System.out.println("list view"+actualItems);
                    listView.setVisible(true);
                }
                else
                    listView.setVisible(false);
            }

            if(event.getCode().equals(KeyCode.ENTER )){
                // System.out.println("le"+letterCounter+"term="+term);
                rowCount++;
                charHeight+=0.05;
                letterCounter = 0;
                xPos=-200;
                term="";
                listView.setVisible(false);
                hasEnter=true;
            }

            if(event.getCode().equals(KeyCode.SPACE)) {
                listView.setVisible(false);
                isSpace = true;
                term += " ";
                tempAddition = Toolkit.getToolkit().getFontLoader().getFontMetrics(textArea.getFont()).computeStringWidth(term);
                letterCounter+=tempAddition;
                xPos=-200+letterCounter;
                term="";
            }

            if(event.getCode().equals(KeyCode.DOWN )){
                listView.requestFocus();
            }

            if(event.getCode().equals(KeyCode.BACK_SPACE)){
                listView.setVisible(false);
            }


        });




        textArea.setOnKeyReleased(event -> {
            textData=textArea.getText();
            // System.out.print(textData.contains("\n"));
            if(event.getCode().equals(KeyCode.SLASH)){
                currentpos=textArea.getCaretPosition();
                currentpos--;
                while(textData.charAt(currentpos)!='>'){
                    currentpos--;
                }
                //System.out.println(currentpos);
                startpos=currentpos;
                while(textData.charAt(startpos)!='<'){
                    startpos--;
                }
                startpos++;
                tag=textData.substring(startpos,currentpos);
                if(tag.contains(" ")){
                    tag=tag.split(" ")[0];
                }
                System.out.print(tag);
                if(!tag.startsWith("/")){
                    onKeyP();
                    textArea.positionCaret(currentpos+1);
                }
            }

            if(event.getCode().equals(KeyCode.ENTER)){

            }
        });

        //suggestion box
        textArea.textProperty().addListener((observable) -> {
            //System.out.println("a");

        });
        setFileName(title);
    }


    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public int findText(String text , boolean dir , boolean caseSensitive , boolean wrapAround){
        if(!text.isEmpty()){
            int cursorPosition = textArea.getCaretPosition();
            int highlightIndex = -1;

            //find all positions of text in textarea
            String indexesString = findAll(text , caseSensitive);
            if(indexesString.length() == 0){
                return -1;
            }
            String[] indexArray = indexesString.split(" ");
            int[] indexes = new int[indexArray.length];
            int i = 0;
            for(String s : indexArray){
                indexes[i] = Integer.valueOf(s);
                i++;
            }

            textArea.setStyle("-fx-highlight-fill: aqua");

            //down direction
            if (dir){
                highlightIndex = cursorPosition;
                int counter = 0;

                while(counter < indexes.length && (highlightIndex) > indexes[counter]){
                    if ((counter + 1) >= indexes.length){
                        if (wrapAround){
                            counter = ++counter % indexes.length;
                        }else{
                            counter = indexes.length - 1;
                        }
                        break;
                    }
                    counter++;
                }
                if (prevCounter == counter){
                    if (wrapAround){
                        counter = ++counter % indexes.length;
                    }else{
                        if (!((counter + 1) >= indexes.length)){
                            counter++;
                        }
                    }

                }

                highlightIndex = indexes[counter];
                textArea.positionCaret(highlightIndex);
                textArea.selectRange(highlightIndex + text.length() , highlightIndex);
                prevCounter = counter;
                System.out.println(highlightIndex);
            }else if (!dir){
                highlightIndex = cursorPosition;
                int counter = indexes.length - 1;

                while(counter >= 0 && highlightIndex <= indexes[counter]){
                    if ((counter - 1) < 0){
                        if (wrapAround){
                            counter = (--counter + indexes.length) % indexes.length;
                        }else {
                            counter = 0;
                        }
                        break;
                    }
                    counter--;
                }
                highlightIndex = indexes[counter];
                textArea.positionCaret(highlightIndex);
                textArea.selectRange(highlightIndex + text.length() , highlightIndex );
                System.out.println(highlightIndex);
            }


            return highlightIndex;
        }
        return -2;

        /*String content = textArea.getText();
        int index = -1;
        int startIndex = textArea.getCaretPosition();

        StringTokenizer st = new StringTokenizer(findAll(text , caseSensitive)," ");
        int indexes[] = new int[st.countTokens()];
        int counter = 0;
        while(st.hasMoreTokens()){
            indexes[counter] = Integer.valueOf(st.nextToken());
            counter++;
        }

        System.out.println(Arrays.toString(indexes));
        counter = 0;
        if(indexes.length > 0){
            textArea.setStyle("-fx-highlight-fill: aqua");
            //down direction
            if (dir) {
                //if last found term is found and user has not changed the cursor position
                if(lastFoundTerm != -1 && indexes[lastFoundTerm] == (startIndex - text.length())){
                    if (wrapAround){
                         lastFoundTerm = ++lastFoundTerm % indexes.length;
                    }else{
                        if((lastFoundTerm+1) < indexes.length)
                            lastFoundTerm++;
                    }

                    index = indexes[lastFoundTerm];
                    textArea.positionCaret(index + text.length());
                   // System.out.println("if"+lastFoundTerm+ " "+ index);
                }
                //if user has changed cursor position
                else{
                    index = content.indexOf(text, startIndex);
                    if (index != -1) {
                        textArea.positionCaret(index + text.length());
                        while (index != indexes[counter]) counter++;
                        lastFoundTerm = counter;
                      //  System.out.println("else"+lastFoundTerm+ " "+ index);
                    }
                }
                System.out.println("a"+textArea.getCaretPosition());
                //textArea.selectRange(index + text.length() , index);
                //textArea.positionCaret(index + text.length() + 1);
                System.out.println("a2 "+textArea.getCaretPosition());
            }

            //up direction
            else {
                if(lastFoundTerm != -1 && indexes[lastFoundTerm] == (startIndex - text.length())) {
                    if (wrapAround){
                        lastFoundTerm = (--lastFoundTerm + indexes.length) % indexes.length;
                    }else{
                        if((lastFoundTerm-1) < indexes.length && (lastFoundTerm-1) >= 0)
                            lastFoundTerm--;
                    }
                    index = indexes[lastFoundTerm];
                    textArea.positionCaret(index + text.length());
                  //  System.out.println("if" + lastFoundTerm + " " + index + " " + startIndex);
                }else {
                    boolean flag = false;
                    while (counter < indexes.length && startIndex > indexes[counter]) {
                        counter++;
                        if (!false) flag = true;
                    }
                    //decrease the counter by 1 because of extra iteration
                    counter--;
                    if (flag) {
                        index = indexes[counter];
                        lastFoundTerm = counter;
                        textArea.positionCaret(index + text.length());
                       // System.out.println("else" + lastFoundTerm + " " + index + " " + startIndex);
                    }
                }

                textArea.selectRange(index - text.length()-1 ,index-1);
            }
        }

        return index;*/

    }

    public String findAll(String text , boolean caseSensitive){
        String content = textArea.getText();
        if (!caseSensitive){
            content = textArea.getText().toLowerCase();
            text = text.toLowerCase();
        }


        StringBuilder indexes = new StringBuilder();
        int tempIndex = 0;
        while (tempIndex <= content.length()){
            tempIndex = content.indexOf(text,tempIndex);

            if(tempIndex == -1){
                break;
            }
            indexes.append(tempIndex + " ");
            tempIndex = tempIndex + text.length();
        }

        return indexes.toString();

    }

    public int countAppearances(String text){
        int count = 0;
        String content = textArea.getText();
        StringTokenizer tokenizer = new StringTokenizer(content," ");
        while(tokenizer.hasMoreTokens()){
//            System.out.print(text);
            if (tokenizer.nextToken().equals(text)){
                count++;
            }
        }

        return count;
    }

    public boolean replaceText(String oldText , String newText){
        String content = textArea.getText();
        if(content.contains(oldText)){
            int index = content.indexOf(oldText);
            textArea.replaceText(index , oldText.length() + index , newText);
            return true;
        }else{
            return false;
        }
    }

    public void onKeyP(){
        textData=textArea.getText();
        int pos=textArea.getCaretPosition();
        StringBuilder stringBuilder=new StringBuilder(textData);
        stringBuilder.insert(pos,tag+">");
        textArea.setText("");
        textArea.positionCaret(pos);
        textArea.setText(stringBuilder.toString());
    }
}
