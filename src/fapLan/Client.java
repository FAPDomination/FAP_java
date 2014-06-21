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
    private boolean initPlay = false;
    protected static TheFrame parent;
    
    private int playerID;

    public Client(InetAddress serv, TheFrame parent, Host host) {
        super();
        this.serveur = serv;

        this.disp = new Displayer(null,true,this,host);
        this.parent = parent;

        try {
            playerID = Integer.parseInt(send("a"));
        } catch (Exception e) {
            System.out.println("Exception in Client.java 53: "+e.toString());
            //e.printStackTrace();
        }
        //System.out.println(send("a"));

        timer = new Timer();
        int time = Params.lanDelay;
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
                            if(!initPlay && game.getMap().getFileID() != 19){
                                initPlay = true;
                                Client.disp.exitWaitingRoom();
                            }
                        }
                        if(!init){
                            Client.parent.changePanel(Client.disp, BorderLayout.CENTER);
                            init = true;
                        }
                    } 
                    catch (Exception e) {
                        System.out.println("Exception in Client.java 81: "+e.toString());
                        //e.printStackTrace();
                        //System.out.println("Perdu un message (et le jeu)");
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
            System.out.println("Exception in Client.java 111: "+e.toString());
        }
        return "error";
    }
    
    public static String sendS(String message, InetAddress serveur){
        Socket socket;
        try {
            socket = new Socket(serveur, Params.port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println(message);

            return in.readLine();
            //System.out.println(in.readLine());
        } catch (Exception e) {
            System.out.println("Exception in Client.java 127: "+e.toString());
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
            System.out.println("InvalidClassException in Client.java : "+k.getMessage());
        }
        catch (Exception e) {
            System.out.println("Exception in Client.java 160: "+e.toString());
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
