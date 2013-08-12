package gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TheFrame extends JFrame {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel north = new JPanel();
    private JPanel center = new MainMenu(this,false);
    
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
        //this.getContentPane().add(north, BorderLayout.NORTH);
        this.getContentPane().add(center, BorderLayout.CENTER);
    }
    
    public void changePanel(JPanel jp, Object layout){
        Component compo = borderLayout1.getLayoutComponent(layout);
        this.remove(compo);
        compo = jp;
        this.getContentPane().add(compo, layout);
        compo.setFocusable(true);
        if(compo instanceof NeedingFocus){
            ((NeedingFocus) compo).initFocus();
        }
        this.validate();
        this.repaint();
    }

}
