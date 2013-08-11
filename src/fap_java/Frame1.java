package fap_java;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame1 extends JFrame {
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel jPanel1 = new JPanel();
    private Game game = new Game("3,6,1,1","0,1,0,0","0,5,0,0",true,5);

    public Frame1() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        this.setSize(new Dimension(900, 700));
        this.getContentPane().add(jPanel1, BorderLayout.NORTH);
        this.getContentPane().add(game, BorderLayout.CENTER);
    }

}
