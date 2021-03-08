package com.javarush.task.task32.task3209;

import com.javarush.task.task32.task3209.listeners.FrameListener;
import com.javarush.task.task32.task3209.listeners.TabbedPaneChangeListener;
import com.javarush.task.task32.task3209.listeners.UndoListener;
import com.sun.glass.ui.Accessible;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class View extends JFrame implements ActionListener {

  private   Controller controller;
  private JTabbedPane tabbedPane = new JTabbedPane();
  private JTextPane htmlTextPane = new JTextPane();
   private JEditorPane plainTextPane = new JEditorPane();
   private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    public UndoListener getUndoListener() {
        return undoListener;
    }

    public void setUndoListener(UndoListener undoListener) {
        this.undoListener = undoListener;
    }
    public void undo() {
        try {
            undoManager.undo();
        }catch ( Exception e ){
            ExceptionHandler.log(e);
        }
    }
    public void redo() {
        try {
            undoManager.redo();
        }catch ( Exception e ){
            ExceptionHandler.log(e);
        }
    }

    public View() {
        try {
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }


    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
  public void init(){
      initGui();
      addWindowListener(new FrameListener(this));

      setVisible(true);

  }

    @Override
    public void actionPerformed(ActionEvent e) {
        String komanda = e.getActionCommand();
        if(komanda.equals("Новый")){
            controller.createNewDocument();
        }else if (komanda.equals("Открыть")){
            controller.openDocument();
        }else if(komanda.equals("Сохранить")){
           controller.saveDocument();
        }else if(komanda.equals("Сохранить как...")){
            controller.saveDocumentAs();
        }else if(komanda.equals("Выход")){
            controller.exit();
        }else if(komanda.equals("О программе")){
            showAbout();
        }

    }
    public void exit(){
      controller.exit();

    }
   public void initMenuBar(){
       JMenuBar jMenuBar = new JMenuBar();
       MenuHelper.initFileMenu(this, jMenuBar);
       MenuHelper.initEditMenu(this, jMenuBar);
       MenuHelper.initStyleMenu(this, jMenuBar);
       MenuHelper.initAlignMenu(this, jMenuBar);
       MenuHelper.initColorMenu(this, jMenuBar);
       MenuHelper.initFontMenu(this, jMenuBar);
       MenuHelper.initHelpMenu(this, jMenuBar);
       getContentPane().add(jMenuBar, BorderLayout.NORTH);
   }
   public void initEditor(){
       htmlTextPane.setContentType("text/html");
       JScrollPane jScrollPane = new JScrollPane(htmlTextPane);
       tabbedPane.add("HTML", jScrollPane);

       JScrollPane jScrollPane1 = new JScrollPane(plainTextPane);
       tabbedPane.add(jScrollPane);
       tabbedPane.add("Текст", jScrollPane1);
       tabbedPane.setPreferredSize(new Dimension(100, 100));
       tabbedPane.addChangeListener(new TabbedPaneChangeListener(this));
       getContentPane().add(tabbedPane, BorderLayout.CENTER);

   }
     public void initGui(){
         initMenuBar();
         initEditor();
         pack();
     }
    public void selectedTabChanged(){
       int x = tabbedPane.getSelectedIndex();
        if(x == 0){
          controller.setPlainText(plainTextPane.getText());

        }if(x == 1){
            plainTextPane.setText(controller.getPlainText());
        }
        resetUndo();
    }
    public boolean canUndo() {
      return undoManager.canUndo();

    }

    public boolean canRedo(){
       return undoManager.canRedo();
    }


    public void resetUndo(){
        undoManager.discardAllEdits();
    }
    public boolean isHtmlTabSelected(){
        if(tabbedPane.getSelectedIndex() == 0){
            return true;
        }else
        return false;
    }
    public void selectHtmlTab(){
        tabbedPane.setSelectedIndex(0);
    resetUndo();

    }
    public void update(){
        htmlTextPane.setDocument(controller.getDocument());
    }
    public void showAbout(){
        JOptionPane.showMessageDialog(this, "Лучший HTML редактор", "О программе", JOptionPane.INFORMATION_MESSAGE);
    }

}
