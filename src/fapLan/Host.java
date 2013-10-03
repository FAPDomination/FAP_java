package fapLan;

import fap_java.Game;

import fap_java.Params;

import java.net.ServerSocket;
import java.net.Socket;

public class Host implements Runnable {
    private Game game;
    public Host(Game game) {
        super();
        this.game = game;
    }


    public void run() {
        try{
            ServerSocket serveur = new ServerSocket(Params.port);
            System.out.println("Serveur lancé");
            while(true){
                Socket client = serveur.accept();
                GameServer t = new GameServer(client,this);
                t.start();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
