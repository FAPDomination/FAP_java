package gui;

import fap_java.Params;
import fap_java.Tools;

import java.awt.BorderLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class TheFrame extends JFrame {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel center;
    
    public TheFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        this.setSize(Constants.frameDimension);
        
        // Parse the configurations of the game
        Tools.parseOptions();
        //Tools.memoryMonitor();
        
        center = new PreLoadingScreen(this);
        this.getContentPane().add(center, BorderLayout.CENTER);
        
        try {
            
            FileInputStream fileIn = new FileInputStream(Constants.c.get(Constants.controlersFile));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Params.controlsList = ((int[][])in.readObject());
            //Params. = ((int[][])in.readObject());
            in.close();
            fileIn.close();
        } 
        catch(FileNotFoundException e){
            System.out.println("Couldn't load keys file");
        }
        catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Impossibru, class not found");
        }
    }
    
    public void changePanel(JPanel jp){
        
        if(center instanceof NeedingFocus){
            ((NeedingFocus) center).releaseFocus();
        }
        this.remove(center);
        center = jp;
        this.getContentPane().add(center);
        center.setFocusable(true);
        if(center instanceof NeedingFocus){
            ((NeedingFocus) center).initFocus();
        }
        this.validate();
        this.repaint();
    }


}
