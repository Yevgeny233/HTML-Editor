package com.javarush.task.task32.task3209;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.*;

public class Controller{

   private HTMLDocument document;
   private View view ;
   private File currentFile ;

    public HTMLDocument getDocument() {
        return document;
    }

    public Controller(View view) {
        this.document = document;
        this.view = view;
        this.currentFile = currentFile;
    }
    public void init(){
        createNewDocument();
    }
    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();

    }
    public void exit(){
        System.exit(0);
    }
    public void resetDocument(){
        if(document != null)
        document.removeUndoableEditListener(view.getUndoListener());

        document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
        
    }
    public void setPlainText(String text) {
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        resetDocument();

        try (StringReader stringReader = new StringReader(text)) {
            htmlEditorKit.read(stringReader, document, 0);
        } catch ( Exception e ) {
            ExceptionHandler.log(e);
        }
    }
    public String getPlainText() {

        StringWriter stringWriter = new StringWriter();

            try {
              new HTMLEditorKit().write(stringWriter, document, 0, document.getLength());
            } catch ( Exception e ) {
                ExceptionHandler.log(e);
            }

        return stringWriter.toString();
    }

    public void createNewDocument() {
       view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        currentFile = null;

    }

    public void openDocument() {
        view.selectHtmlTab();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new HTMLFileFilter());
        boolean a = jFileChooser.showOpenDialog(view) ==  jFileChooser.OPEN_DIALOG;
        if(a){
            currentFile = jFileChooser.getSelectedFile();
            String namefile = currentFile.getName();
            view.setTitle(namefile);
            try (FileReader fileReader = new FileReader(currentFile)){
                new HTMLEditorKit().read(fileReader, document, 0);

            } catch ( FileNotFoundException e ) {
               ExceptionHandler.log(e);
            } catch ( IOException e ) {
                ExceptionHandler.log(e);
            } catch ( BadLocationException e ) {
                ExceptionHandler.log(e);
            }
            resetDocument();
            view.resetUndo();

        }



    }

    public void saveDocument() {
     view.selectHtmlTab();
     if(currentFile != null){
        try (FileWriter fileWriter = new FileWriter(currentFile)){
            new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
        } catch ( IOException e ) {
            ExceptionHandler.log(e);
        } catch ( BadLocationException e ) {
            ExceptionHandler.log(e);
        }
     }

      if(currentFile == null){
          saveDocumentAs();
      }

    }

    public void saveDocumentAs() {
        view.selectHtmlTab();
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(new HTMLFileFilter());
        if(jFileChooser.showSaveDialog(view) == jFileChooser.APPROVE_OPTION){
            currentFile = jFileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            try (FileWriter fileWriter = new FileWriter(currentFile)){
              new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());  
            }catch ( Exception e ){
                ExceptionHandler.log(e);
            }
        }

    }
}
