package fapLan;

import fap_java.Game;

import fap_java.Params;

import gui.Displayer;

import gui.TheFrame;

import java.awt.BorderLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

import java.net.InetAddress;

import java.net.InetSocketAddress;
import java.net.Socket;

import java.net.UnknownHostException;

import java.nio.channels.SocketChannel;

import java.util.Timer;
import java.util.TimerTask;

public class Client {
    private InetAddress serveur;
    private Timer timer;
    private static Displayer disp;
    private Game game;
    private boolean init = false;
    protected static TheFrame parent;
    
    private int playerID;

    public Client(InetAddress serv, TheFrame parent) {
        super();
        this.serveur = serv;
        boolean host = false;
        try {
            if(serveur.getHostAddress().equals(InetAddress.getLocalHost().getHostAddress())){
            host = true;
        }
        } catch (UnknownHostException e) {
        }
        this.disp = new Displayer(null,true,this,host);
        this.parent = parent;

        playerID = Integer.parseInt(send("a"));
        //System.out.println(send("a"));

        timer = new Timer();
        int time = 300;
        // Ce timer récupère les données constamment
        timer.schedule(new TimerTask() {
                public void run() {
                    try {
                        game = askForGame();
                        if (game != null) {
                            game.setDisplayer(Client.disp);
                            game.getThread().setRunning(false);
                            Client.disp.setGame(game);
                            Client.disp.repaint();
                        }
                        if(!init){
                            Client.parent.changePanel(Client.disp, BorderLayout.CENTER);
                            init = true;
                        }
                    } 
                    catch (Exception e) {
                        System.out.println("Perdu un message (et le jeu)");
                        return;
                    }
                }
            }, time, time);
    }

    public String send(String message) {
        //System.out.println("Sending "+message);
        // Envoie la string via protocole TCP
        Socket socket;
        try {
            // Adresse serveur
            //= InetAddress.getByName("192.168.0.13");
            //serveur = InetAddress.getByName("192.168.0.13");

            //serveur = InetAddress.getLocalHost();
            // Crée la connexion
            socket = new Socket(serveur, Params.port);
            // in c'est ce qui revient du serveur après
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // out c'est ce que je lui envoie
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println(message);

            return in.readLine();
            //System.out.println(in.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    public Game askForGame() {
        // Envoie la string via protocole TCP
        SocketChannel sChannel;
        try {
            sChannel = SocketChannel.open();
            sChannel.configureBlocking(true);
            // Adresse serveur
            //= InetAddress.getByName("192.168.0.13");
            //serveur = InetAddress.getByName("192.168.0.13");

            //serveur = InetAddress.getLocalHost();
            // Crée la connexion
            sChannel.connect(new InetSocketAddress(serveur, Params.port));
            // in c'est ce qui revient du serveur après
            //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // out c'est ce que je lui envoie
            PrintStream out = new PrintStream(sChannel.socket().getOutputStream());
            out.println("g");
            ObjectInputStream in = new ObjectInputStream(sChannel.socket().getInputStream());
            Game ga = (Game)in.readObject();
            return ga;
            //System.out.println(in.readLine());
        } 
        catch(InvalidClassException k){
            System.out.println(k.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }
}
