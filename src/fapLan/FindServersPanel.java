package fapLan;

import fap_java.Game;

import fap_java.Params;

import gui.Constants;
import gui.FAPanel;
import gui.TheFrame;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class FindServersPanel extends FAPanel {
    private JButton refreshBtn = new JButton();
    private JButton hostBtn = new JButton();

    private ArrayList<InetAddress> servers = new ArrayList<InetAddress>();
    private ArrayList<JButton> jblist = new ArrayList<JButton>();

    public FindServersPanel(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);

        swordX = minxS;
        cloudsX = minxC;

        this.setLayout(null);
        this.setSize(Constants.frameDimension);

        btnGoBack.setText("Retour");
        btnGoBack.setSize(120, 40);
        btnGoBack.setLocation(20, 20);
        this.add(btnGoBack);

        refreshBtn.setText("Refresh");
        refreshBtn.setSize(120, 40);
        refreshBtn.setLocation(this.getWidth() - 2 * (30 + refreshBtn.getWidth()), 20);
        this.add(refreshBtn);

        hostBtn.setText("Host");
        hostBtn.setSize(120, 40);
        hostBtn.setLocation(this.getWidth() - (30 + hostBtn.getWidth()), 20);
        this.add(hostBtn);

        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findServers();
            }
        });

        hostBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                host();
            }
        });

        findServers();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawString("Recherche de Serveurs en cours", 50, 130);
    }

    public void findServers() {
        servers.clear();
        System.out.println("Looking for server");
        for (int j = 0; j < 1; j++) {
            for (int i = 0; i < 256; i++) {
                Spy targ;
                targ = new Spy("192.168." + j + "." + i, this, 3000);
                Thread th1 = new Thread(targ);
                targ.setTh(th1);
                th1.start();
            }
        }
    }

    public void host() {
        Game game = new Game();

        System.out.println("tryina launch");
        Host h = new Host(game);
        Thread th1 = new Thread(h);
        th1.start();

        InetAddress srv = null;

        try {
            srv = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("UnknowHostException in FindPanelServers.java : "+e.toString());
        }
        Client ci = new Client(srv, parent);

        game.pauseGame();
    }

    public void askServer(String address) {
        Socket socket;
        try {
            // Adresse serveur
            //= InetAddress.getByName("192.168.0.13");
            //serveur = InetAddress.getByName("192.168.0.13");
            //serveur = InetAddress.getLocalHost();
            // Crée la connexion
            socket = new Socket(address, Params.port);
            // in c'est ce qui revient du serveur après
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // out c'est ce que je lui envoie
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println("s");
            Boolean b = Boolean.parseBoolean(in.readLine());
            if (b) {
                System.out.println("Found server at " + address);
                InetAddress serveur = InetAddress.getByName(address);
                servers.add(serveur);
                computeButtons();
            } else {

            }
            //System.out.println(in.readLine());
        } catch (Exception e) {
            System.out.println("Exception in FindSercersPanel.java : "+e.toString());
        }
    }

    private void computeButtons() {
        for (int i = 0; i < jblist.size(); i++) {
            this.remove(jblist.get(i));
        }
        jblist.clear();
        for (int i = 0; i < servers.size(); i++) {
            JButton jb = new JButton();
            InetAddress srv = servers.get(i);
            int pCount = 0;
            try {
                pCount = Integer.parseInt(Client.sendS("h", srv));
            } catch (Exception e) {
                System.out.println("Exception in FindServerPanel.java : "+e.toString());
            }
            jb.setSize(150, 35);
            jb.setText(srv.getHostAddress() + " (" + pCount + ")");
            jb.setLocation(50, 150 + i * 40);
            jb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jb_ActionPerformed(e);
                }
            });
            jblist.add(jb);
        }
        for (int i = 0; i < jblist.size(); i++) {
            this.add(jblist.get(i));
        }
        this.validate();
        this.repaint();
    }

    public void jb_ActionPerformed(ActionEvent e) {
        JButton jb = (JButton)e.getSource();
        int index = jblist.indexOf(jb);
        InetAddress srv = servers.get(index);

        //Proceed to game
        Client ci = new Client(srv, parent);
    }
}
